package aima.core.logic.planning;

import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Variable;

import java.util.ArrayList;
import java.util.Arrays;

public class PlanningProblemFactory {
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
                "At(t,Ground)^At(t,Axle)");
        ActionSchema leaveOvernightAction = new ActionSchema("LeaveOvernight", null,
                "",
                "~At(Spare,Ground)^~At(Spare,Axle)^At(Spare,Trunk)" +
                        "^~At(Flat,Ground)^~At(Flat,Axle)^~At(Flat,Trunk)");
        return new Problem(initialState, goalState, removeAction, putOnAction, leaveOvernightAction);
    }
}
