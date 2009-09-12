package aima.gui.framework;

/**
 * In this framework a graphical agent application consists of at least
 * three parts: An {@link AgentAppModel}, an {@link AgentAppFrame}, and
 * an {@link AgentAppController}. This class demonstrates, how this three
 * parts are plugged together. The easiest way to create a new graphical agent
 * application is to create subclasses of the three parts as needed, and
 * then to subclass this class and override the three factory methods. 
 * @author R. Lunde
 */
public class SimpleAgentAppDemo {
	/**
	 * Creates an agent application, makes the parts know
	 * each other, and finally sets the frame visible.
	 */
	public void startApplication() {
		AgentAppModel model = createModel();
		AgentAppFrame frame = createFrame();
		AgentAppController controller = createController();
		frame.setController(controller);
		frame.setModel(model);
		controller.setFrame(frame);
		controller.setModel(model);
		model.addModelChangedListener(frame);
		frame.setVisible(true);
		frame.setDefaultSelection();
	}
	
	/** Factory method, responsible for creating the model. */
	public AgentAppModel createModel() {
		return new AgentAppModel();
	}
	
	/**
	 * Factory method, responsible for creating the frame. This
	 * implementation shows how the {@code AgentAppFrame} can be
	 * configured with respect to the needs of the application
	 * even without creating a subclass.
	 */
	public AgentAppFrame createFrame() {
		AgentAppFrame result = new AgentAppFrame();
		result.setAgentView(new AgentView());
		result.setSelectors(
				new String[]{"XSelect", "YSelect"},
				new String[]{"Select X", "Select Y"});
		result.setSelectorItems("XSelect", new String[]{"X1 (Small)", "X2 (Large)"}, 1);
		result.setSelectorItems("YSelect", new String[]{"Y=1", "Y=2", "Y=3"}, 0);
		result.setTitle("Demo Agent Application");
		result.setSplitPaneResizeWeight(0.5); // puts split bar in center position.
		result.setSize(600, 400);
		result.setUpdateDelay(500);
		return result;
	}
	
	/** Factory method, responsible for creating the controller. */
	public AgentAppController createController() {
		return new AgentAppController();
	}

	
	/////////////////////////////////////////////////////////////////
	// main method for testing
	
	/**
	 * Starts a simple test frame application.
	 */
	public static void main(String args[]) {
		new SimpleAgentAppDemo().startApplication();
	}
}

