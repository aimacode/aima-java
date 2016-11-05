package aima.gui.fx.applications.agent;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Agent;
import aima.core.environment.map.AdaptableHeuristicFunction;
import aima.core.environment.map.ExtendableMap;
import aima.core.environment.map.MapAgent;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.Scenario;
import aima.core.environment.map.SimplifiedRoadMapOfAustralia;
import aima.core.environment.map.SimplifiedRoadMapOfPartOfRomania;
import aima.core.util.CancelableThread;
import aima.core.util.math.geom.shapes.Point2D;
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
	protected aima.core.search.framework.Search search;
	/** Heuristic function to be used when performing informed search. */
	protected AdaptableHeuristicFunction heuristic;

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

		Parameter[] params = createParameters();

		SimulationPaneBuilder builder = new SimulationPaneBuilder();
		builder.defineParameters(params);
		builder.defineStateView(envView);
		builder.defineInitMethod(this::initialize);
		builder.defineSimMethod(this::simulate);
		simPaneCtrl = builder.getResultFor(root);

		return root;
	}

	protected Parameter[] createParameters() {
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
		p4.setDependency(PARAM_SEARCH, "Depth First", "Breadth First", "Uniform Cost", "Greedy Best First", "A*");
		Parameter p5 = new Parameter(PARAM_HEURISTIC, "0", "SLD");
		p5.setDependency(PARAM_SEARCH, "Greedy Best First", "A*", "Recursive Best First",
				"Recursive Best First No Loops", "Hill Climbing");
		p5.setDefaultValueIndex(1);
		return new Parameter[] { p1, p2r, p2a, p3, p4, p5 };
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
			heuristic = new H1();
			break;
		default:
			heuristic = new H2();
		}
		heuristic.adaptToGoal(destinations.get(0), scenario.getAgentMap());

		search = SearchFactory.getInstance().createSearch(simPaneCtrl.getParamValueIndex(PARAM_SEARCH),
				simPaneCtrl.getParamValueIndex(PARAM_Q_SEARCH_IMPL), heuristic);

		String goal = destinations.get(0);
		agent = new MapAgent(env.getMap(), search, new String[] { goal });
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
		simPaneCtrl.setStatus("Search metrics: " + search.getMetrics());
		envViewCtrl.notify("pathCost=" + search.getMetrics().get("pathCost"));
	}

	@Override
	public void finalize() {
		simPaneCtrl.cancelSimulation();
	}

	// helper classes...

	/**
	 * Returns always the heuristic value 0.
	 */
	static class H1 extends AdaptableHeuristicFunction {

		public double h(Object state) {
			return 0.0;
		}
	}

	/**
	 * A simple heuristic which interprets <code>state</code> and {@link #goal}
	 * as location names and uses the straight-line distance between them as
	 * heuristic value.
	 */
	static class H2 extends AdaptableHeuristicFunction {

		public double h(Object state) {
			double result = 0.0;
			Point2D pt1 = map.getPosition((String) state);
			Point2D pt2 = map.getPosition((String) goal);
			if (pt1 != null && pt2 != null)
				result = pt1.distance(pt2);
			return result;
		}
	}
}
