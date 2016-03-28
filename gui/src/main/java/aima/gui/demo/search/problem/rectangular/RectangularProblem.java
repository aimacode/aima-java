package aima.gui.demo.search.problem.rectangular;

import aima.core.search.BasicProblem;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Ciaran O'Reilly
 */
public class RectangularProblem extends BasicProblem<String, AtVertex> {

    public RectangularProblem(int xSize, int ySize, AtVertex initialState, Collection<AtVertex> goals) {
        super(initialState,
                RectangularProblem.actions(xSize, ySize),
                RectangularProblem::resultOf,
                RectangularProblem.goalTest(goals));
    }

    public static Function<AtVertex, Set<String>> actions(final int xSize, int ySize)  {
        return atVertex -> {
            Set<String> result = new LinkedHashSet<>();
            if (atVertex.y > 0) {
                result.add(go(atVertex.x, atVertex.y-1));
            }
            if (atVertex.x < xSize) {
                result.add(go(atVertex.x + 1, atVertex.y));
            }
            if (atVertex.y < ySize) {
                result.add(go(atVertex.x, atVertex.y+1));
            }
            if (atVertex.x > 0) {
                result.add(go(atVertex.x-1, atVertex.y));
            }

            return result;
        };
    }

    public static AtVertex resultOf(AtVertex at, String a) {
        int goX = Integer.parseInt(a.substring(a.indexOf("(") + 1, a.indexOf(",")));
        int goY = Integer.parseInt(a.substring(a.indexOf(",")+1,a.indexOf(")")));
        return new AtVertex(goX, goY);
    }

    public static Predicate<AtVertex> goalTest(Collection<AtVertex> goals) {
        final Set<AtVertex> testGoals = new HashSet<>(goals);
        return testGoals::contains;
    }

    public static String go(int x, int y) {
        return "Go("+x+","+y+")";
    }
}
