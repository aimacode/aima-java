package aima.core.logic.planning;

import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.logic.planning.hierarchicalsearch.HighLevelAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A problem factory to generate planning problems.
 *
 * @author samagra
 */
public class PlanningProblemFactory {

    /**
     * Generates air cargo problem. Artificial Intelligence A Modern Approach (3rd Edition):Figure 10.1 page 369.<br>
     * <p>
     * Init(At(C1, SFO) ∧ At(C2, JFK) ∧ At(P1, SFO) ∧ At(P2, JFK)
     *   ∧ Cargo(C1) ∧ Cargo(C2) ∧ Plane(P1) ∧ Plane(P2)
     *   ∧ Airport(JFK) ∧ Airport(SFO))
     * Goal(At(C1, JFK) ∧ At(C2, SFO))
     * Action(Load(c, p, a),
     *  PRECOND: At(c, a) ∧ At(p, a) ∧ Cargo(c) ∧ Plane(p) ∧ Airport(a)
     *  EFFECT: ¬ At(c, a) ∧ In(c, p))
     * Action(Unload(c, p, a),
     *  PRECOND: In(c, p) ∧ At(p, a) ∧ Cargo(c) ∧ Plane(p) ∧ Airport(a)
     *  EFFECT: At(c, a) ∧ ¬ In(c, p))
     * Action(Fly(p, from, to),
     *  PRECOND: At(p, from) ∧ Plane(p) ∧ Airport(from) ∧ Airport(to)
     *  EFFECT: ¬ At(p, from) ∧ At(p, to))
     *
     * @return A PDDL description of an air cargo transportation planning problem.
     */
    public static Problem airCargoTransportProblem() {
        State initialState = new State("At(C1,SFO)^At(C2,JFK)^At(P1,SFO)" +
                "^At(P2,JFK)^Cargo(C1)^Cargo(C2)^Plane(P1)^Plane(P2)^Airport(JFK)^Airport(SFO)");
        State goalState = new State("At(C1,JFK)^At(C2,SFO)");
        Variable c = new Variable("c");
        Variable p = new Variable("p");
        Variable a = new Variable("a");
        Variable from = new Variable("from");
        Variable to = new Variable("to");
        ArrayList variables = new ArrayList<>(Arrays.asList(c, p, a));
        ArrayList flyVars = new ArrayList<>(Arrays.asList(p, from, to));
        ActionSchema loadAction = new ActionSchema("Load", variables,
                "At(c,a)^At(p,a)^Cargo(c)^Plane(p)^Airport(a)",
                "~At(c,a)^In(c,p)");
        ActionSchema unloadAction = new ActionSchema("Unload", variables,
                "In(c,p)^At(p,a)^Cargo(c)^Plane(p)^Airport(a)",
                "At(c,a)^~In(c,p)");
        ActionSchema flyAction = new ActionSchema("Fly", flyVars,
                "At(p,from)^Plane(p)^Airport(from)^Airport(to)",
                "~At(p,from)^At(p,to)");

        return new Problem(initialState, goalState, loadAction, unloadAction, flyAction);
    }

    /**
     * Generates spare tire problem. Artificial Intelligence A Modern Approach (3rd Edition): Figure 10.2 page 370.<br>
     * <p>
     * Init(Tire(Flat) ∧ Tire(Spare) ∧ At(Flat, Axle) ∧ At(Spare, Trunk))
     * Goal(At(Spare, Axle))
     * Action(Remove(obj, loc),
     *  PRECOND: At(obj, loc)
     *  EFFECT: ¬ At(obj, loc) ∧ At(obj, Ground))
     * Action(PutOn(t, Axle),
     *  PRECOND: Tire(t) ∧ At(t, Ground) ∧ ¬ At(Flat, Axle)
     *  EFFECT: ¬ At(t, Ground) ∧ At(t, Axle))
     * Action(LeaveOvernight,
     *  PRECOND:
     *  EFFECT: ¬ At(Spare, Ground) ∧ ¬ At(Spare, Axle) ∧ ¬ At(Spare, Trunk)
     *      ∧ ¬ At(Flat, Ground) ∧ ¬ At(Flat, Axle) ∧ ¬ At(Flat, Trunk))
     *
     * @return The spare tire problem.
     */
    public static Problem spareTireProblem() {
        State initialState = new State("Tire(Flat)^Tire(Spare)^At(Flat,Axle)" +
                "^At(Spare,Trunk)");
        State goalState = new State("At(Spare,Axle)");
        Variable obj = new Variable("obj");
        Variable loc = new Variable("loc");
        Variable t = new Variable("t");
        Constant Axle = new Constant("Axle");
        ArrayList removeVars = new ArrayList<>(Arrays.asList(obj, loc));
        ArrayList putOnVars = new ArrayList<>(Arrays.asList(t, Axle));
        ActionSchema removeAction = new ActionSchema("Remove", removeVars,
                "At(obj,loc)",
                "~At(obj,loc)^At(obj,Ground)");
        ActionSchema putOnAction = new ActionSchema("PutOn", putOnVars,
                "Tire(t)^At(t,Ground)^~At(Flat,Axle)",
                "~At(t,Ground)^At(t,Axle)");
        ActionSchema leaveOvernightAction = new ActionSchema("LeaveOvernight", null,
                "",
                "~At(Spare,Ground)^~At(Spare,Axle)^~At(Spare,Trunk)" +
                        "^~At(Flat,Ground)^~At(Flat,Axle)^~At(Flat,Trunk)");
        return new Problem(initialState, goalState, removeAction, putOnAction, leaveOvernightAction);
    }

    /**
     * Generates go to SanFrancisco airport . Artificial Intelligence A Modern Approach (3rd Edition): Figure 11.4 page 407.<br>
     * <p>
     * Refinement(Go(Home, SFO),
     * STEPS : [Drive(Home, SFOLongTermParking),
     * Shuttle(SFOLongTermParking, SFO)] )
     * Refinement(Go(Home, SFO),
     * STEPS : [Taxi (Home, SFO)] )
     *
     * @return The San Francisco Airport problem.
     */
    public static Problem goHomeToSFOProblem() {
        State initialState = new State("At(Home)");
        State goalState = new State("At(SFO)");
        ActionSchema driveAction = new ActionSchema("Drive", null,
                "At(Home)",
                "~At(Home)^At(SFOLongTermParking)");
        ActionSchema shuttleAction = new ActionSchema("Shuttle", null,
                "At(SFOLongTermParking)",
                "~At(SFOLongTermParking)^At(SFO)");
        ActionSchema taxiAction = new ActionSchema("Taxi", null,
                "At(Home)",
                "~At(Home)^At(SFO)");
        return new Problem(initialState, goalState, driveAction, shuttleAction, taxiAction);

    }

    /**
     * Generates the Act HLA for a problem.
     *
     * @param problem
     * @return The Act HLA.
     */
    public static HighLevelAction getHlaAct(Problem problem) {
        List<List<ActionSchema>> refinements = new ArrayList<>();
        HighLevelAction act = new HighLevelAction("Act", null, "", "", refinements);
        for (ActionSchema primitiveAction :
                problem.getPropositionalisedActions()) {
            act.addRefinement(new ArrayList<>(Arrays.asList(primitiveAction, act)));
        }
        act.addRefinement(new ArrayList<>());
        return act;
    }
}
