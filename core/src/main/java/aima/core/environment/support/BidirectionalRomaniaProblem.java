package aima.core.environment.support;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.map2d.Map2DFunctionFactory;
import aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania;
import aima.core.search.api.ActionsFunction;
import aima.core.search.api.BidirectionalProblem;
import aima.core.search.api.Problem;
import aima.core.search.api.ResultFunction;
import aima.core.search.api.StepCostFunction;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author wormi
 */
public class BidirectionalRomaniaProblem implements BidirectionalProblem<GoAction, InState> {

  private final SimplifiedRoadMapOfPartOfRomania map = new SimplifiedRoadMapOfPartOfRomania();
  private final String initialLocation;
  private final String[] goalLocations;
  private final ResultFunction<GoAction, InState> resultFunction;

  private final ActionsFunction<GoAction, InState> actionsFunction;
  private final StepCostFunction<GoAction, InState> stepCostFunction;

  {
    actionsFunction = Map2DFunctionFactory.getActionsFunction(map);
    resultFunction = Map2DFunctionFactory.getResultFunction(map);
    stepCostFunction = Map2DFunctionFactory.getStepCostFunction(map);
  }

  BidirectionalRomaniaProblem(String initialLocation, String... goalLocations) {
    this.initialLocation = initialLocation;
    this.goalLocations = goalLocations;
  }

  @Override
  public Problem<GoAction, InState> getOriginalProblem() {
    return new MyOriginalProblem();
  }

  @Override
  public List<Problem<GoAction, InState>> getReverseProblems() {
    List<Problem<GoAction, InState>> problems = new LinkedList<>();
    for (String goalLocation : goalLocations) {
      problems.add(new MyReverseProblem(goalLocation));
    }
    return problems;
  }


  private class MyOriginalProblem implements Problem<GoAction, InState> {

    @Override
    public InState initialState() {
      return new InState(initialLocation);
    }

    @Override
    public boolean isGoalState(InState state) {
      return Stream.of(goalLocations).anyMatch(s ->  s.equals(state.getLocation()));
    }

    @Override
    public InState result(InState inState, GoAction goAction) {
      return resultFunction.result(inState, goAction);
    }

    @Override
    public double stepCost(InState inState, GoAction goAction, InState sPrime) {
      return stepCostFunction.stepCost(inState, goAction, sPrime);
    }

    @Override
    public List<GoAction> actions(InState inState) {
      return actionsFunction.actions(inState);
    }
  }

  private class MyReverseProblem implements Problem<GoAction, InState>{

    private final InState reverseInitialState;

    private MyReverseProblem(String goalLocation) {
      this.reverseInitialState = new InState(goalLocation);
    }

    @Override
    public InState initialState() {
      return reverseInitialState;
    }

    @Override
    public boolean isGoalState(InState state) {
      return initialLocation.equals(state.getLocation());
    }

    @Override
    public InState result(InState inState, GoAction goAction) {
      return resultFunction.result(inState, goAction);
    }

    @Override
    public double stepCost(InState inState, GoAction goAction, InState sPrime) {
      return stepCostFunction.stepCost(inState, goAction, sPrime);
    }

    @Override
    public List<GoAction> actions(InState inState) {
      return actionsFunction.actions(inState);
    }
  }

}
