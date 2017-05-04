package aima.gui.fx.framework;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import aima.core.util.CancelableThread;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Controller class for integrated applications. Integrated applications are
 * built by the {@link IntegratedAppBuilder} and provide access to
 * {@link IntegrableApplication} instances and console applications. This
 * controller class is responsible starting applications, integrating their GUI
 * into the window of the integrated application, title update, and for scale
 * property management.
 * 
 * @author Ruediger Lunde
 */
public class IntegratedAppPaneCtrl {

	private BorderPane pane;
	private Stage stage;
	private String title;

	private TextArea messageArea;
	private ScrollPane messageScrollPane;
	private MessagePaneCtrl messagePaneCtrl;

	private DoubleProperty scale = new SimpleDoubleProperty();
	private Optional<IntegrableApplication> currApp = Optional.empty();
	private Optional<CancelableThread> currDemoThread = Optional.empty();

	public IntegratedAppPaneCtrl() {
		messageArea = new TextArea();
		messageArea.setFont(Font.font("monospaced"));
		messageScrollPane = new ScrollPane(messageArea);
		messageScrollPane.setFitToWidth(true);
		messageScrollPane.setFitToHeight(true);
		messagePaneCtrl = new MessagePaneCtrl(messageArea);
		scale.set(1.0);
	}

	public void setContext(BorderPane pane, Stage stage, String title) {
		this.pane = pane;
		this.stage = stage;
		this.title = title;
		updateStageTitle();
	}

	public DoubleProperty scaleProperty() {
		return scale;
	}

	public void startApp(Class<? extends IntegrableApplication> appClass) {
		stopRunningAppsAndDemo();
		try {
			currApp = Optional.of(appClass.newInstance());
			Pane appPane = currApp.get().createRootPane();
			pane.setCenter(appPane);
			updateStageTitle();
			currApp.get().initialize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startDemo(Class<?> demoClass) {
		stopRunningAppsAndDemo();
		if (pane.getCenter() != messageScrollPane)
			pane.setCenter(messageScrollPane);
		messageArea.clear();
		updateStageTitle();
		// redirect the standard output into the text area
		PrintStream pStream = messagePaneCtrl.getPrintStream();
		System.setOut(pStream);
		// System.setErr(messagePaneCtrl.getPrintStream());
		currDemoThread = Optional.of(new CancelableThread(() -> {
			startMain(demoClass);
			pStream.flush();
		}));
		currDemoThread.get().setDaemon(true);
		currDemoThread.get().start();
	}

	private void startMain(Class<?> demoClass) {
		try {
			Method m = demoClass.getMethod("main", String[].class);
			m.invoke(null, (Object) new String[] {});
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}

	private void stopRunningAppsAndDemo() {
		if (currApp.isPresent()) {
			currApp.get().cleanup();
			currApp = Optional.empty();
		}
		if (currDemoThread.isPresent()) {
			currDemoThread.get().cancel();
			currDemoThread = Optional.empty();
		}
	}

	/**
	 * Updates the window title using the title of the integrated application
	 * and the current embedded application (if any).
	 */
	private void updateStageTitle() {
		stage.setTitle(title + (currApp.isPresent() ? " - " + currApp.get().getTitle() : ""));
	}
}
