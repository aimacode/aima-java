package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Term;
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
     * <pre>
     * Init(At(C1, SFO) ∧ At(C2, JFK) ∧ At(P1, SFO) ∧ At(P2, JFK)
     *  ∧ Cargo(C1) ∧ Cargo(C2) ∧ Plane(P1) ∧ Plane(P2)
     *  ∧ Airport(JFK) ∧ Airport(SFO))
     * Goal(At(C1, JFK) ∧ At(C2, SFO))
     * Action(Load(c, p, a),
     *  PRECOND: At(c, a) ∧ At(p, a) ∧ Cargo(c) ∧ Plane(p) ∧ Airport(a)
     *  EFFECT: ¬ At(c, a) ∧ In(c, p))
     * Action(Unload(c, p, a),
     *  PRECOND: In(c, p) ∧ At(p, a) ∧ Cargo(c) ∧ Plane(p) ∧ Airport(a)
     *  EFFECT: At(c, a) ∧ ¬ In(c, p))
     * Action(Fly(p, from, to),
     *  PRECOND: At(p, from) ∧ Plane(p) ∧ Airport(from) ∧ Airport(to)
     *  EFFECT: ¬ At(p, from) ∧ At(p, to))
     *  </pre>
     *
     * @return A PDDL description of an air cargo transportation planning problem.
     */
    public static PlanningProblem airCargoTransportProblem() {
        State initialState = new State("At(C1,SFO)^At(C2,JFK)^At(P1,SFO)" +
                "^At(P2,JFK)^Cargo(C1)^Cargo(C2)^Plane(P1)^Plane(P2)^Airport(JFK)^Airport(SFO)");
        List<Literal> goal = Utils.parse("At(C1,JFK)^At(C2,SFO)");
        Variable c = new Variable("c");
        Variable p = new Variable("p");
        Variable a = new Variable("a");
        Variable from = new Variable("from");
        Variable to = new Variable("to");
        ArrayList<Term> loadVars = new ArrayList<>(Arrays.asList(c, p, a));
        ArrayList<Term> flyVars = new ArrayList<>(Arrays.asList(p, from, to));
        ActionSchema loadAction = new ActionSchema("Load", loadVars,
                "At(c,a)^At(p,a)^Cargo(c)^Plane(p)^Airport(a)",
                "~At(c,a)^In(c,p)");
        ActionSchema unloadAction = new ActionSchema("Unload", loadVars,
                "In(c,p)^At(p,a)^Cargo(c)^Plane(p)^Airport(a)",
                "At(c,a)^~In(c,p)");
        ActionSchema flyAction = new ActionSchema("Fly", flyVars,
                "At(p,from)^Plane(p)^Airport(from)^Airport(to)",
                "~At(p,from)^At(p,to)");

        return new PlanningProblem(initialState, goal, loadAction, unloadAction, flyAction);
    }

    /**
     * Generates spare tire problem. Artificial Intelligence A Modern Approach (3rd Edition): Figure 10.2 page 370.<br>
     * <p>
     * <pre>
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
     * </pre>
     *
     * Note: This problem goes beyond STRIPS in that it uses a negated precondition!
     *
     * @return The spare tire problem.
     */
    public static PlanningProblem spareTireProblem() {
        State initialState = new State("Tire(Flat)^Tire(Spare)^At(Flat,Axle)^At(Spare,Trunk)");
        List<Literal> goal = Utils.parse("At(Spare,Axle)");
        Variable obj = new Variable("obj");
        Variable loc = new Variable("loc");
        Variable t = new Variable("t");
        ArrayList<Term> removeVars = new ArrayList<>(Arrays.asList(obj, loc));
        ArrayList<Term> putOnVars = new ArrayList<>(List.of(t));
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
        return new PlanningProblem(initialState, goal, removeAction, putOnAction, leaveOvernightAction);
    }

    /**
     * Generates go to SanFrancisco airport . Artificial Intelligence A Modern Approach (3rd Edition): Figure 11.4 page 407.<br>
     * <p>
     * <pre>
     * Refinement(Go(Home, SFO),
     * STEPS : [Drive(Home, SFOLongTermParking),
     * Shuttle(SFOLongTermParking, SFO)] )
     * Refinement(Go(Home, SFO),
     * STEPS : [Taxi (Home, SFO)] )
     * </pre>
     *
     * @return The San Francisco Airport problem.
     */
    public static PlanningProblem goHomeToSFOProblem() {
        State initialState = new State("At(Home)");
        List<Literal> goal = Utils.parse("At(SFO)");
        ActionSchema driveAction = new ActionSchema("Drive", null,
                "At(Home)",
                "~At(Home)^At(SFOLongTermParking)");
        ActionSchema shuttleAction = new ActionSchema("Shuttle", null,
                "At(SFOLongTermParking)",
                "~At(SFOLongTermParking)^At(SFO)");
        ActionSchema taxiAction = new ActionSchema("Taxi", null,
                "At(Home)",
                "~At(Home)^At(SFO)");
        return new PlanningProblem(initialState, goal, driveAction, shuttleAction, taxiAction);

    }

    /**
     * Generates the Act HLA for a problem.
     *
     * @param problem
     * @return The Act HLA.
     */
    public static HighLevelAction getHlaAct(PlanningProblem problem) {
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
