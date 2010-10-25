package aima.gui.framework;

/**
 * In this framework a graphical agent application consists at least of three
 * parts: An {@link AgentAppEnvironmentView}, an {@link AgentAppFrame}, and an
 * {@link AgentAppController}. This class plugs the three parts together. Note
 * that no support for model creation is included here. Environments are used as
 * models and may depend on current selector settings. So typically, the
 * controller will be responsible for creating them (in method
 * {@link AgentAppController#prepare(String)}) and making the view know them
 * (by calling method
 * {@link AgentAppEnvironmentView#setEnvironment(aima.core.agent.Environment)}).
 * <p>
 * The easiest way to create a new graphical agent application is to create
 * subclasses of the three parts as needed, and then to create a subclass of
 * this class and override the three factory methods.
 * </p>
 * 
 * @author Ruediger Lunde
 */
public class SimpleAgentApp {
	/**
	 * Constructs an agent application and sets the application frame visible.
	 */
	public void startApplication() {
		AgentAppFrame frame = constructApplicationFrame();
		frame.centerPane.setDividerLocation(frame.centerPane.getResizeWeight());
		frame.setVisible(true);
	}

	/**
	 * Creates all parts of an agent application and makes the parts know each
	 * other. Part construction is delegated to factory methods.
	 */
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
		result.setTitle("Demo Agent Application");
		result.setSelectors(new String[] { "XSelect", "YSelect" },
				new String[] { "Select X", "Select Y" });
		result.setSelectorItems("XSelect", new String[] { "X1 (Small)",
				"X2 (Large)" }, 1);
		result.setSelectorItems("YSelect",
				new String[] { "Y=1", "Y=2", "Y=3" }, 0);
		result.setSplitPaneResizeWeight(0.5); // puts split bar in center
		result.setSize(600, 400);
		return result;
	}

	/** Factory method, responsible for creating the controller. */
	public AgentAppController createController() {
		return new DemoController();
	}

	// ///////////////////////////////////////////////////////////////
	// main method for testing

	/**
	 * Starts a simple test frame application.
	 */
	public static void main(String args[]) {
		new SimpleAgentApp().startApplication();
	}
}
