package aima.gui.fx.applications.search;

import java.util.Arrays;
import java.util.List;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.BacktrackingStrategy;
import aima.core.search.csp.CSP;
import aima.core.search.csp.CSPStateListener;
import aima.core.search.csp.Domain;
import aima.core.search.csp.ImprovedBacktrackingStrategy;
import aima.core.search.csp.MinConflictsStrategy;
import aima.core.search.csp.SolutionStrategy;
import aima.core.search.csp.examples.MapCSP;
import aima.gui.fx.framework.IntegrableApplication;
import aima.gui.fx.framework.Parameter;
import aima.gui.fx.framework.SimulationPaneBuilder;
import aima.gui.fx.framework.SimulationPaneCtrl;
import aima.gui.fx.views.CspViewCtrl;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Application which demonstrates basic constraint algorithms based on map
 * coloring problems. It shows the constraint graph, lets the user select a
 * solution strategy, and allows then to follow the progress step by step.
 *
 * @author Ruediger Lunde
 */
public class CspMapColoringApp extends IntegrableApplication {

    public static void main(String[] args) {
        launch(args);
    }

    public final static String PARAM_MAP = "map";
    public final static String PARAM_STRATEGY = "strategy";

    private CspViewCtrl stateViewCtrl;
    private SimulationPaneCtrl simPaneCtrl;

    private CSP csp;
    private SolutionStrategy strategy;
    private int stepCounter;

    public CspMapColoringApp() {
    }

    @Override
    public String getTitle() {
        return "CSP Map Coloring App";
    }

    /**
     * Defines state view, parameters, and call-back functions and calls the
     * simulation pane builder to create layout and controller objects.
     */
    @Override
    public Pane createRootPane() {
        BorderPane root = new BorderPane();

        StackPane stateView = new StackPane();
        stateViewCtrl = new CspViewCtrl(stateView);

        List<Parameter> params = createParameters();

        SimulationPaneBuilder builder = new SimulationPaneBuilder();
        builder.defineParameters(params);
        builder.defineStateView(stateView);
        builder.defineInitMethod(this::initialize);
        builder.defineSimMethod(this::simulate);
        simPaneCtrl = builder.getResultFor(root);

        return root;
    }

    protected List<Parameter> createParameters() {
        Parameter p1 = new Parameter(PARAM_MAP, "Map of Australia",
                "Map of Australia NSW=BLUE (for LCV)",
                "Map of Australia WA=RED (for LCV)");
        Parameter p2 = new Parameter(PARAM_STRATEGY, "Backtracking",
                "Backtracking + MRV & DEG",
                "Backtracking + Forward Checking",
                "Backtracking + Forward Checking + MRV",
                "Backtracking + Forward Checking + LCV",
                "Backtracking + AC3",
                "Backtracking + AC3 + MRV & DEG + LCV",
                "Min-Conflicts (50)");
        return Arrays.asList(p1, p2);
    }

    /**
     * Displays the selected function on the state view.
     */
    @Override
    public void initialize() {
        csp = null;
        switch (simPaneCtrl.getParamValueIndex(PARAM_MAP)) {
            case 0:
                csp = new MapCSP();
                break;
            case 1: // three moves
                csp = new MapCSP();
                csp.setDomain(MapCSP.NSW, new Domain(new Object[]{MapCSP.BLUE}));
                break;
            case 2: // three moves
                csp = new MapCSP();
                csp.setDomain(MapCSP.WA, new Domain(new Object[]{MapCSP.RED}));
                break;
        }

        stateViewCtrl.clearMappings();
        int c = 15;
        stateViewCtrl.setPositionMapping(MapCSP.WA, c*5, c*10);
        stateViewCtrl.setPositionMapping(MapCSP.NT, c*15, c*3);
        stateViewCtrl.setPositionMapping(MapCSP.SA, c*20, c*15);
        stateViewCtrl.setPositionMapping(MapCSP.Q, c*30, c*5);
        stateViewCtrl.setPositionMapping(MapCSP.NSW, c*35, c*15);
        stateViewCtrl.setPositionMapping(MapCSP.V, c*30, c*23);
        stateViewCtrl.setPositionMapping(MapCSP.T, c*33, c*30);

        stateViewCtrl.setColorMapping(MapCSP.RED, Color.RED);
        stateViewCtrl.setColorMapping(MapCSP.GREEN, Color.GREEN);
        stateViewCtrl.setColorMapping(MapCSP.BLUE, Color.BLUE);

        ImprovedBacktrackingStrategy iStrategy = null;
        switch (simPaneCtrl.getParamValueIndex(PARAM_STRATEGY)) {
            case 0:
                strategy = new BacktrackingStrategy();
                break;
            case 1: // MRV + DEG
                strategy = new ImprovedBacktrackingStrategy
                        (true, true, false, false);
                break;
            case 2: // FC
                iStrategy = new ImprovedBacktrackingStrategy();
                iStrategy.setInference(ImprovedBacktrackingStrategy
                        .Inference.FORWARD_CHECKING);
                break;
            case 3: // MRV + FC
                iStrategy = new ImprovedBacktrackingStrategy
                        (true, false, false, false);
                iStrategy.setInference(ImprovedBacktrackingStrategy
                        .Inference.FORWARD_CHECKING);
                break;
            case 4: // FC + LCV
                iStrategy = new ImprovedBacktrackingStrategy
                        (false, false, false, true);
                iStrategy.setInference(ImprovedBacktrackingStrategy
                        .Inference.FORWARD_CHECKING);
                break;
            case 5: // AC3
                strategy = new ImprovedBacktrackingStrategy
                        (false, false, true, false);
                break;
            case 6: // MRV + DEG + AC3 + LCV
                strategy = new ImprovedBacktrackingStrategy
                        (true, true, true, true);
                break;
            case 7:
                strategy = new MinConflictsStrategy(50);
                break;
        }
        if (iStrategy != null)
            strategy = iStrategy;

        strategy.addCSPStateListener(new CSPStateListener() {

            @Override
            public void stateChanged(Assignment assignment, CSP csp) {
                stepCounter++;
                updateStateView(csp, assignment);
            }

            @Override
            public void stateChanged(CSP csp) {
                stepCounter++;
                updateStateView(csp, null);
            }
        });

        stateViewCtrl.initialize(csp);
    }

    @Override
    public void finalize() {
        simPaneCtrl.cancelSimulation();
    }

    /**
     * Starts the experiment.
     */
    public void simulate() {
        stepCounter = 0;
        strategy.solve(csp.copyDomains());
    }

    /**
     * Caution: While the background thread should be slowed down, updates of
     * the GUI have to be done in the GUI thread!
     */
    private void updateStateView(CSP csp, Assignment assignment) {
        Platform.runLater(() -> updateStateViewLater(csp, assignment));
        simPaneCtrl.waitAfterStep();
    }

    /**
     * Must be called by the GUI thread!
     */
    private void updateStateViewLater(CSP csp, Assignment assignment) {
        stateViewCtrl.update(csp, assignment);
        String txt1 = "Step " + stepCounter + ": ";
        String txt2 = "Domain reduced";
        if (assignment != null)
            txt2 = assignment.toString() + (assignment.isSolution(csp) ? " (Solution)" : "");
        simPaneCtrl.setStatus(txt1 + txt2);
    }
}
