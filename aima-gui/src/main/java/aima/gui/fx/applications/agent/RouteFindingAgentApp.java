package aima.gui.fx.applications.agent;

import aima.core.agent.Agent;
import aima.core.agent.impl.DynamicPercept;
import aima.core.environment.map.*;
import aima.core.search.framework.Node;
import aima.core.search.framework.SearchForActions;
import aima.core.util.Tasks;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.TaskExecutionPaneBuilder;
import aima.gui.fx.framework.TaskExecutionPaneCtrl;
import aima.gui.fx.views.MapEnvironmentViewCtrl;
import aima.gui.util.SearchFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;

/**
 * Integrable application which demonstrates how different kinds of search
 * algorithms perform in a route finding scenario.
 *
 * @author Ruediger Lunde
 *
 */
public class RouteFindingAgentApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	public static String PARAM_SCENARIO = "scenario";
	public static String PARAM_DESTINATION_R = "destinationR";
	public static String PARAM_DESTINATION_A = "destinationA";
	public static String PARAM_SEARCH = "search";
	public static String PARAM_Q_SEARCH_IMPL = "qsearch";
	public static String PARAM_HEURISTIC = "heuristic";

	private TaskExecutionPaneCtrl taskPaneCtrl;
	private MapEnvironmentViewCtrl envViewCtrl;
	protected MapEnvironment env = null;
	protected Agent<DynamicPercept, MoveToAction> agent = null;

	/** A scenario. */
	protected Scenario scenario;
	/**
	 * Some location names. For route finding problems, only one location should
	 * be specified.
	 */
	protected List<String> destinations;
	/** Search method to be used. */
	protected SearchForActions<String, MoveToAction> search;
	/** Heuristic function to be used when performing informed search. */
	protected ToDoubleFunction<Node<String, MoveToAction>> heuristic;

	@Override
	public String getTitle() {
		return "Route Finding Agent App";
	}

	/**
	 * Defines state view, parameters, and call-back functions and calls the
	 * simulation pane builder to create layout and controller objects.
	 */
	@Override
	public Pane createRootPane() {
		BorderPane root = new BorderPane();

		StackPane envView = new StackPane();
		envViewCtrl = new MapEnvironmentViewCtrl(envView);

		List<Parameter> params = createParameters();

		TaskExecutionPaneBuilder builder = new TaskExecutionPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(envView);
		builder.defineInitMethod(this::initialize);
		builder.defineTaskMethod(this::startExperiment);
		taskPaneCtrl = builder.getResultFor(root);

		return root;
	}

	protected List<Parameter> createParameters() {
		Parameter p1 = new Parameter(PARAM_SCENARIO, "Romania, from Arad", "Romania, from Lugoj",
				"Romania, from Fagaras", "Australia, from Sydney", "Australia, from Random");
		p1.setValueNames("Romania, from Arad", "Romania, from Lugoj", "Romania, from Fagaras", "Australia, from Sydney",
				"Australia, from Random");
		Parameter p2r = new Parameter(PARAM_DESTINATION_R, "to Bucharest", "to Eforie", "to Neamt", "to Random");
		p2r.setValueNames("to Bucharest", "to Eforie", "to Neamt", "to Random");
		p2r.setDependency(PARAM_SCENARIO, "Romania, from Arad", "Romania, from Lugoj", "Romania, from Fagaras");

		Parameter p2a = new Parameter(PARAM_DESTINATION_A, "to Port Hedland", "to Albany", "to Melbourne", "to Random");
		p2a.setValueNames("to Port Hedland", "to Albany", "to Melbourne", "to Random");
		p2a.setDependency(PARAM_SCENARIO, "Australia, from Sydney", "Australia, from Random");

		Parameter p3 = new Parameter(PARAM_SEARCH, (Object[]) SearchFactory.getInstance().getSearchStrategyNames());
		p3.setDefaultValueIndex(5);
		Parameter p4 = new Parameter(PARAM_Q_SEARCH_IMPL, (Object[]) SearchFactory.getInstance().getQSearchImplNames());
		p4.setDefaultValueIndex(2);
		p4.setDependency(PARAM_SEARCH, "Depth First", "Breadth First", "Uniform Cost", "Greedy Best First", "A*");
		Parameter p5 = new Parameter(PARAM_HEURISTIC, "0", "SLD");
		p5.setDefaultValueIndex(1);
		p5.setDependency(PARAM_SEARCH, "Greedy Best First", "A*", "Recursive Best First",
				"Recursive Best First No Loops", "Hill Climbing");
		return Arrays.asList(p1, p2r, p2a, p3, p4, p5);
	}

	/** Is called after each parameter selection change. */
	@Override
	public void initialize() {
		ExtendableMap map = new ExtendableMap();
		env = new MapEnvironment(map);
		String agentLoc = null;
		switch (taskPaneCtrl.getParamValueIndex(PARAM_SCENARIO)) {
		case 0:
			SimplifiedRoadMapOfRomania.initMap(map);
			agentLoc = SimplifiedRoadMapOfRomania.ARAD;
			break;
		case 1:
			SimplifiedRoadMapOfRomania.initMap(map);
			agentLoc = SimplifiedRoadMapOfRomania.LUGOJ;
			break;
		case 2:
			SimplifiedRoadMapOfRomania.initMap(map);
			agentLoc = SimplifiedRoadMapOfRomania.FAGARAS;
			break;
		case 3:
			SimplifiedRoadMapOfAustralia.initMap(map);
			agentLoc = SimplifiedRoadMapOfAustralia.SYDNEY;
			break;
		case 4:
			SimplifiedRoadMapOfAustralia.initMap(map);
			agentLoc = map.randomlyGenerateDestination();
			break;
		}
		scenario = new Scenario(env, map, agentLoc);

		destinations = new ArrayList<>();
		if (taskPaneCtrl.isParamVisible(PARAM_DESTINATION_R)) {
			switch (taskPaneCtrl.getParamValueIndex(PARAM_DESTINATION_R)) {
			case 0:
				destinations.add(SimplifiedRoadMapOfRomania.BUCHAREST);
				break;
			case 1:
				destinations.add(SimplifiedRoadMapOfRomania.EFORIE);
				break;
			case 2:
				destinations.add(SimplifiedRoadMapOfRomania.NEAMT);
				break;
			case 3:
				destinations.add(map.randomlyGenerateDestination());
				break;
			}
		} else if (taskPaneCtrl.isParamVisible(PARAM_DESTINATION_A)) {
			switch (taskPaneCtrl.getParamValueIndex(PARAM_DESTINATION_A)) {
			case 0:
				destinations.add(SimplifiedRoadMapOfAustralia.PORT_HEDLAND);
				break;
			case 1:
				destinations.add(SimplifiedRoadMapOfAustralia.ALBANY);
				break;
			case 2:
				destinations.add(SimplifiedRoadMapOfAustralia.MELBOURNE);
				break;
			case 3:
				destinations.add(map.randomlyGenerateDestination());
				break;
			}
		}

		switch (taskPaneCtrl.getParamValueIndex(PARAM_HEURISTIC)) {
		case 0:
			heuristic = (node) -> 0.0;
			break;
		default:
			heuristic = MapFunctions.createSLDHeuristicFunction(destinations.get(0), scenario.getAgentMap());
		}

		search = SearchFactory.getInstance().createSearch(taskPaneCtrl.getParamValueIndex(PARAM_SEARCH),
				taskPaneCtrl.getParamValueIndex(PARAM_Q_SEARCH_IMPL), heuristic);

		String goal = destinations.get(0);
		agent = new SimpleMapAgent(env.getMap(), search, goal);
		env.addAgent(agent, scenario.getInitAgentLocation());
		env.addEnvironmentListener(envViewCtrl);
		envViewCtrl.setGoal(goal);
		envViewCtrl.initialize(env);
	}

	/** Starts the experiment. */
	public void startExperiment() {
		while (!env.isDone() && !Tasks.currIsCancelled()) {
			env.step();
			taskPaneCtrl.waitAfterStep();
		}
		taskPaneCtrl.setStatus(search.getMetrics().toString());
		envViewCtrl.notify("pathCost=" + search.getMetrics().get("pathCost"));
	}

	@Override
	public void cleanup() {
		taskPaneCtrl.cancelExecution();
	}
}
