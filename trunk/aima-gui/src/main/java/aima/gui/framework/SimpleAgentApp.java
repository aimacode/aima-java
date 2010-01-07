package aima.gui.framework;

/**
 * In this framework a graphical agent application consists at least of three
 * parts: An {@link AgentAppEnvironmentView}, an {@link AgentAppFrame}, and an
 * {@link AgentAppController}. This class demonstrates, how this three parts are
 * plugged together. The easiest way to create a new graphical agent application
 * is to create subclasses of the three parts as needed, and then to create a
 * subclass this class and override the three factory methods.
 * 
 * @author R. Lunde
 */
public class SimpleAgentApp {
	/**
	 * Creates an agent application, makes the parts know each other, and
	 * finally sets the frame visible.
	 */
	public void startApplication() {
		AgentAppFrame frame = constructApplicationFrame();
		frame.setVisible(true);
	}

	public AgentAppFrame constructApplicationFrame() {
		AgentAppEnvironmentView envView = createEnvironmentView();
		AgentAppFrame frame = createFrame();
		AgentAppController controller = createController();
		frame.setEnvView(envView);
		envView.setMessageLogger(frame.getMessageLogger());
		frame.setController(controller);
		controller.setFrame(frame);
		frame.setDefaultSelection();
		return frame;
	}
	
	/** Factory method, responsible for creating the environment view. */
	public AgentAppEnvironmentView createEnvironmentView() {
		return new EmptyEnvironmentView();
	}

	/**
	 * Factory method, responsible for creating the frame. This implementation
	 * shows how the {@code AgentAppFrame} can be configured with respect to the
	 * needs of the application even without creating a subclass.
	 */
	public AgentAppFrame createFrame() {
		AgentAppFrame result = new AgentAppFrame();
		result.setSelectors(new String[] { "XSelect", "YSelect" },
				new String[] { "Select X", "Select Y" });
		result.setSelectorItems("XSelect", new String[] { "X1 (Small)",
				"X2 (Large)" }, 1);
		result.setSelectorItems("YSelect",
				new String[] { "Y=1", "Y=2", "Y=3" }, 0);
		result.setTitle("Demo Agent Application");
		result.setSplitPaneResizeWeight(0.5); // puts split bar in center
		result.setSize(600, 400);
		return result;
	}

	/** Factory method, responsible for creating the controller. */
	public AgentAppController createController() {
		return new DemoController();
	}

	/////////////////////////////////////////////////////////////////
	// main method for testing

	/**
	 * Starts a simple test frame application.
	 */
	public static void main(String args[]) {
		new SimpleAgentApp().startApplication();
	}
}
