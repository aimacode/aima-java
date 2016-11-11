package aima.gui.fx.applications.agent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.core.agent.Agent;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MapFunctionFactory;
import aima.core.environment.map.Scenario;
import aima.core.environment.map.SimpleMapAgent;
import aima.core.environment.map.SimplifiedRoadMapOfAustralia;
import aima.core.environment.map.SimplifiedRoadMapOfPartOfRomania;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.evalfunc.HeuristicFunction;
import aima.core.util.CancelableThread;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.SimulationPaneBuilder;
import aima.gui.fx.framework.SimulationPaneCtrl;
import aima.gui.fx.views.MapEnvironmentViewCtrl;
import aima.gui.util.SearchFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Integrable application which demonstrates how different kinds of search
 * algorithms perform an a route finding scenario.
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

	private SimulationPaneCtrl simPaneCtrl;
	private MapEnvironmentViewCtrl envViewCtrl;
	protected MapEnvironment env = null;
	protected Agent agent = null;

	/** A scenario. */
	protected Scenario scenario;
	/**
	 * Some location names. For route finding problems, only one location should
	 * be specified.
	 */
	protected List<String> destinations;
	/** Search method to be used. */
	protected SearchForActions search;
	/** Heuristic function to be used when performing informed search. */
	protected HeuristicFunction heuristic;

	public RouteFindingAgentApp() {
	}

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

		SimulationPaneBuilder builder = new SimulationPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(envView);
		builder.defineInitMethod(this::initialize);
		builder.defineSimMethod(this::simulate);
		simPaneCtrl = builder.getResultFor(root);

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
		p4.setDefaultValueIndex(1);
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
		switch (simPaneCtrl.getParamValueIndex(PARAM_SCENARIO)) {
		case 0:
			SimplifiedRoadMapOfPartOfRomania.initMap(map);
			agentLoc = SimplifiedRoadMapOfPartOfRomania.ARAD;
			break;
		case 1:
			SimplifiedRoadMapOfPartOfRomania.initMap(map);
			agentLoc = SimplifiedRoadMapOfPartOfRomania.LUGOJ;
			break;
		case 2:
			SimplifiedRoadMapOfPartOfRomania.initMap(map);
			agentLoc = SimplifiedRoadMapOfPartOfRomania.FAGARAS;
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

		destinations = new ArrayList<String>();
		if (simPaneCtrl.isParamVisible(PARAM_DESTINATION_R)) {
			switch (simPaneCtrl.getParamValueIndex(PARAM_DESTINATION_R)) {
			case 0:
				destinations.add(SimplifiedRoadMapOfPartOfRomania.BUCHAREST);
				break;
			case 1:
				destinations.add(SimplifiedRoadMapOfPartOfRomania.EFORIE);
				break;
			case 2:
				destinations.add(SimplifiedRoadMapOfPartOfRomania.NEAMT);
				break;
			case 3:
				destinations.add(map.randomlyGenerateDestination());
				break;
			}
		} else if (simPaneCtrl.isParamVisible(PARAM_DESTINATION_A)) {
			switch (simPaneCtrl.getParamValueIndex(PARAM_DESTINATION_A)) {
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

		switch (simPaneCtrl.getParamValueIndex(PARAM_HEURISTIC)) {
		case 0:
			heuristic = MapFunctionFactory.getZeroHeuristicFunction();
			break;
		default:
			heuristic = MapFunctionFactory.getSLDHeuristicFunction(destinations.get(0), scenario.getAgentMap());
		}

		search = SearchFactory.getInstance().createSearch(simPaneCtrl.getParamValueIndex(PARAM_SEARCH),
				simPaneCtrl.getParamValueIndex(PARAM_Q_SEARCH_IMPL), heuristic);

		String goal = destinations.get(0);
		agent = new SimpleMapAgent(env.getMap(), search, new String[] { goal });
		env.addAgent(agent, scenario.getInitAgentLocation());
		env.addEnvironmentView(envViewCtrl);
		envViewCtrl.setGoal(goal);
		envViewCtrl.initialize(env);
	}

	/** Starts the experiment. */
	public void simulate() {
		while (!env.isDone() && !CancelableThread.currIsCanceled()) {
			env.step();
			simPaneCtrl.waitAfterStep();
		}
		simPaneCtrl.setStatus(search.getMetrics().toString());
		envViewCtrl.notify("pathCost=" + search.getMetrics().get("pathCost"));
	}

	@Override
	public void finalize() {
		simPaneCtrl.cancelSimulation();
	}
}
