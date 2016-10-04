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

import java.util.List;

/**
 * @author wormi
 */
class BidirectionalRomaniaProblem implements BidirectionalProblem<GoAction, InState> {

  private final SimplifiedRoadMapOfPartOfRomania map = new SimplifiedRoadMapOfPartOfRomania();
  private final String initialLocation;
  private final String goalLocation;
  private final ResultFunction<GoAction, InState> resultFunction;

  private final ActionsFunction<GoAction, InState> actionsFunction;
  private final StepCostFunction<GoAction, InState> stepCostFunction;

  {
    actionsFunction = Map2DFunctionFactory.getActionsFunction(map);
    resultFunction = Map2DFunctionFactory.getResultFunction(map);
    stepCostFunction = Map2DFunctionFactory.getStepCostFunction(map);
  }

  BidirectionalRomaniaProblem(String initialLocation, String goalLocation) {
    this.initialLocation = initialLocation;
    this.goalLocation = goalLocation;
  }

  @Override
  public Problem<GoAction, InState> getOriginalProblem() {
    return new Problem<GoAction, InState>() {
      @Override
      public InState initialState() {
        return new InState(initialLocation);
      }

      @Override
      public boolean isGoalState(InState state) {
        return goalLocation.equals(state.getLocation());
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
    };
  }

  @Override
  public Problem<GoAction, InState> getReverseProblem() {
    return new Problem<GoAction, InState>() {
      @Override
      public InState initialState() {
        return new InState(goalLocation);
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
    };
  }
}
