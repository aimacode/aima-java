package aima.gui.fx.framework;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Builder for integrated applications. To create an integrated application,
 * just create a builder, define a title, register apps (integrable JavaFX
 * applications) and demos (command line applications) and place the result into a
 * scene of a stage.
 * 
 * @author Ruediger Lunde
 */
public class IntegratedAppPaneBuilder {

	private MenuBar menuBar = new MenuBar();
	private Menu appsMenu = new Menu("Apps");
	private Menu demosMenu = new Menu("Demos");
	private String title = "";

	private IntegratedAppPaneCtrl paneCtrl;

	public IntegratedAppPaneBuilder() {
		paneCtrl = new IntegratedAppPaneCtrl();
		final DoubleProperty scale = paneCtrl.scaleProperty();
		MenuItem incScaleItem = new MenuItem("Inc Scale");
		incScaleItem.setOnAction(ev -> scale.set(trunc(scale.get() * 1.3)));
		incScaleItem.setAccelerator(new KeyCodeCombination(KeyCode.PLUS, KeyCombination.CONTROL_DOWN));
		MenuItem decScaleItem = new MenuItem("Dec Scale");
		decScaleItem.setOnAction(ev -> scale.set(trunc(scale.get() / 1.3)));
		decScaleItem.setAccelerator(new KeyCodeCombination(KeyCode.MINUS, KeyCombination.CONTROL_DOWN));
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(ev -> Platform.exit());

		Menu fileMenu = new Menu("File");
		fileMenu.getItems().addAll(incScaleItem, decScaleItem, exitItem);
		menuBar.getMenus().addAll(fileMenu, appsMenu, demosMenu);
		menuBar.styleProperty().bind(Bindings.concat("-fx-font-size: ",
				paneCtrl.scaleProperty().multiply(Font.getDefault().getSize()).asString()));
	}

	public void defineTitle(String title) {
		this.title = title;
	}

	public void registerApp(Class<? extends IntegrableApplication> appClass) {
		final IntegratedAppPaneCtrl ctrl = paneCtrl;
		MenuItem item = new MenuItem(appClass.getSimpleName());
		item.setOnAction(ev -> ctrl.startApp(appClass));
		addToMenu(appsMenu, appClass.getPackage().getName(), item);
	}

	public void registerDemo(Class<?> demoClass) {
		final IntegratedAppPaneCtrl ctrl = paneCtrl;
		MenuItem item = new MenuItem(demoClass.getSimpleName());
		item.setOnAction(ev -> ctrl.startProg(demoClass));
		addToMenu(demosMenu, demoClass.getPackage().getName(), item);
	}

	/**
	 * Adds a menu bar and a scalable container pane to the provided root pane and
	 * returns a controller instance containing user interface logic.
	 */
	public IntegratedAppPaneCtrl getResultFor(BorderPane root, Stage stage) {

		// create a pane, content is affected by scale
		final DoubleProperty scale = paneCtrl.scaleProperty();
		BorderPane appPane = new BorderPane();
		appPane.scaleXProperty().bind(scale);
		appPane.scaleYProperty().bind(scale);

		Pane appPaneContainer = new Pane();
		appPaneContainer.getChildren().add(appPane);
		appPane.prefWidthProperty().bind(appPaneContainer.widthProperty().divide(scale));
		appPane.prefHeightProperty().bind(appPaneContainer.heightProperty().divide(scale));
		appPane.translateXProperty()
				.bind(appPaneContainer.widthProperty().subtract(appPane.prefWidthProperty()).divide(2));
		appPane.translateYProperty()
				.bind(appPaneContainer.heightProperty().subtract(appPane.prefHeightProperty()).divide(2));

		paneCtrl.setContext(appPane, stage, title);

		root.setTop(menuBar);
		root.setCenter(appPaneContainer);
		// just in case, the builder is called twice...
		IntegratedAppPaneCtrl result = paneCtrl;
		paneCtrl = new IntegratedAppPaneCtrl();
		return result;
	}

	/**
	 * Adds a new starter item to the specified menu.
	 */
	private MenuItem addToMenu(Menu menu, String packageName, MenuItem item) {
		Menu subMenu = null;
		ObservableList<MenuItem> menuComps = menu.getItems();
		int i;
		for (i = 0; i < menuComps.size(); i++) {
			Menu comp = (Menu) menuComps.get(i);
			if (comp.getText().equals(packageName))
				subMenu = comp;
			else if (comp.getText().compareTo(packageName) > 0)
				break;
		}
		if (subMenu == null) {
			subMenu = new Menu(packageName);
			menu.getItems().add(i, subMenu);
		}
		subMenu.getItems().add(item);
		return item;
	}

	private double trunc(double num) {
		return Math.round(num * 2) / 2.0;
	}
}
