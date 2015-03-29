package aima.gui.demo.search.problem.rectangular;

import aima.core.api.agent.Action;
import aima.core.search.BasicProblem;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ciaran O'Reilly
 */
public class RectangularProblem extends BasicProblem<AtVertex> {

    public RectangularProblem(int xSize, int ySize, AtVertex initialState, Collection<AtVertex> goals) {
        super(initialState,
                RectangularProblem.actions(xSize, ySize),
                RectangularProblem::resultOf,
                RectangularProblem.goalTest(goals));
    }

    public static Function<AtVertex, Set<Action>> actions(final int xSize, int ySize)  {
        return atVertex -> {
            Set<Action> result = new LinkedHashSet<>();
            if (atVertex.x > 0) {
                result.add(go(atVertex.x-1, atVertex.y));
            }
            if (atVertex.x < xSize) {
                result.add(go(atVertex.x+1, atVertex.y));
            }
            if (atVertex.y > 0) {
                result.add(go(atVertex.x, atVertex.y-1));
            }
            if (atVertex.y < ySize) {
                result.add(go(atVertex.x, atVertex.y+1));
            }

            return result;
        };
    }

    public static AtVertex resultOf(AtVertex at, Action a) {
        int goX = Integer.parseInt(a.name().substring(a.name().indexOf("(") + 1, a.name().indexOf(",")));
        int goY = Integer.parseInt(a.name().substring(a.name().indexOf(",")+1,a.name().indexOf(")")));
        return new AtVertex(goX, goY);
    }

    public static Predicate<AtVertex> goalTest(Collection<AtVertex> goals) {
        final Set<AtVertex> testGoals = new HashSet<>(goals);
        return testGoals::contains;
    }

    public static Action go(int x, int y) {
        return Action.newNamedAction("Go("+x+","+y+")");
    }
}
