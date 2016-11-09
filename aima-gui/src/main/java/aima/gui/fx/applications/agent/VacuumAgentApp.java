package aima.gui.fx.applications.agent;

import java.util.Arrays;
import java.util.List;

import aima.core.agent.impl.AbstractAgent;
import aima.core.environment.vacuum.FullyObservableVacuumEnvironmentPerceptToStateFunction;
import aima.core.environment.vacuum.ModelBasedReflexVacuumAgent;
import aima.core.environment.vacuum.NondeterministicVacuumAgent;
import aima.core.environment.vacuum.NondeterministicVacuumEnvironment;
import aima.core.environment.vacuum.ReflexVacuumAgent;
import aima.core.environment.vacuum.SimpleReflexVacuumAgent;
import aima.core.environment.vacuum.TableDrivenVacuumAgent;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.VacuumWorldActions;
import aima.core.environment.vacuum.VacuumWorldGoalTest;
import aima.core.environment.vacuum.VacuumWorldResults;
import aima.core.search.framework.problem.DefaultStepCostFunction;
import aima.core.search.nondeterministic.NondeterministicProblem;
import aima.core.util.CancelableThread;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.SimulationPaneBuilder;
import aima.gui.fx.framework.SimulationPaneCtrl;
import aima.gui.fx.views.SimpleEnvironmentViewCtrl;
import aima.gui.fx.views.VacuumEnvironmentViewCtrl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Integrable application which demonstrates how different kinds of vacuum
 * cleaner agents behave in a two square environment.
 * 
 * @author Ruediger Lunde
 *
 */
public class VacuumAgentApp extends IntegrableApplication {

	public static void main(String[] args) {
		launch(args);
	}

	public final static String PARAM_ENV = "environment";
	public final static String PARAM_AGENT = "agent";

	private SimulationPaneCtrl simPaneCtrl;
	private SimpleEnvironmentViewCtrl envViewCtrl;
	protected VacuumEnvironment env = null;
	protected AbstractAgent agent = null;

	public VacuumAgentApp() {

	}

	@Override
	public String getTitle() {
		return "Vacuum Agent App";
	}

	/**
	 * Defines state view, parameters, and call-back functions and calls the
	 * simulation pane builder to create layout and controller objects.
	 */
	@Override
	public Pane createRootPane() {
		BorderPane root = new BorderPane();

		StackPane envView = new StackPane();
		envViewCtrl = new VacuumEnvironmentViewCtrl(envView);

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
		Parameter p1 = new Parameter(PARAM_ENV, "A/B Deterministic Environment", "A/B Non-Deterministic Environment");
		Parameter p2 = new Parameter(PARAM_AGENT, "TableDrivenVacuumAgent", "ReflexVacuumAgent",
				"SimpleReflexVacuumAgent", "ModelBasedReflexVacuumAgent", "NondeterministicVacuumAgent");
		return Arrays.asList(p1, p2);
	}

	/** Is called after each parameter selection change. */
	@Override
	public void initialize() {
		switch (simPaneCtrl.getParamValueIndex(PARAM_ENV)) {
		case 0:
			env = new VacuumEnvironment();
			break;
		case 1:
			env = new NondeterministicVacuumEnvironment();
			break;
		}
		agent = null;
		switch (simPaneCtrl.getParamValueIndex(PARAM_AGENT)) {
		case 0:
			agent = new TableDrivenVacuumAgent();
			break;
		case 1:
			agent = new ReflexVacuumAgent();
			break;
		case 2:
			agent = new SimpleReflexVacuumAgent();
			break;
		case 3:
			agent = new ModelBasedReflexVacuumAgent();
			break;
		case 4:
			agent = createNondeterministicVacuumAgent();
			break;
		}
		if (env != null && agent != null) {
			envViewCtrl.initialize(env);
			env.addEnvironmentView(envViewCtrl);
			env.addAgent(agent);
			if (agent instanceof NondeterministicVacuumAgent) {
				// Set the problem now for this kind of agent
				((NondeterministicVacuumAgent) agent).setProblem(createNondeterministicProblem());
			}
		}
	}

	/** Starts the experiment. */
	public void simulate() {
		while (!env.isDone() && !CancelableThread.currIsCanceled()) {
			env.step();
			simPaneCtrl.setStatus("Performance=" + env.getPerformanceMeasure(agent));
			simPaneCtrl.waitAfterStep();
		}
		envViewCtrl.notify("Performance=" + env.getPerformanceMeasure(agent));
	}

	@Override
	public void finalize() {
		simPaneCtrl.cancelSimulation();
	}
	
	// helper methods...
	
	private NondeterministicVacuumAgent createNondeterministicVacuumAgent() {
		NondeterministicVacuumAgent agent = new NondeterministicVacuumAgent(
				new FullyObservableVacuumEnvironmentPerceptToStateFunction());
		return agent;
	}

	private NondeterministicProblem createNondeterministicProblem() {
		NondeterministicProblem problem = new NondeterministicProblem(env.getCurrentState(), new VacuumWorldActions(),
				new VacuumWorldResults(agent), new VacuumWorldGoalTest(agent), new DefaultStepCostFunction());
		return problem;
	}
}
