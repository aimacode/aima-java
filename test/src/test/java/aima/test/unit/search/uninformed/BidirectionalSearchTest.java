package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.BidirectionalActions;
import aima.core.search.api.Problem;
import aima.core.search.basic.uninformed.BidirectionalSearch;
import aima.core.util.datastructure.Pair;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.ARAD;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.BUCHAREST;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.FAGARAS;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.ORADEA;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.SIBIU;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.TIMISOARA;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.URZICENI;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.ZERIND;
import static org.junit.Assert.assertEquals;

/**
 * @author manthan.
 */
@RunWith(Parameterized.class)
public class BidirectionalSearchTest {
    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> implementations() {
        return Arrays.asList(new Object[][]{{"BidirectionalSearch"}});
    }

    @Parameterized.Parameter
    public String searchFunctionName;

    public <A, S> BidirectionalActions<A> searchForActions(Pair<Problem<A, S>, Problem<A, S>> pair) {
        BidirectionalSearch<A, S> search = new BidirectionalSearch<>();
        return search.apply(pair.getFirst(), pair.getSecond());
    }

    @Test
    public void aradToArad() throws Exception {
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> pair =
            ProblemFactory.getSimpleBidirectionalSearchProblem(ARAD, ARAD);

        assertEquals(Collections.EMPTY_LIST, searchForActions(pair).fromInitialStatePart());
    }

    @Test
    public void timisoaraToUrziceni() throws Exception {
      //Result will contain all intermediate nodes excluding initial and final nodes.
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> pair =
            ProblemFactory.getSimpleBidirectionalSearchProblem(TIMISOARA, URZICENI);
        final BidirectionalActions<GoAction> solution = searchForActions(pair);

        assertEquals(Arrays.asList(
            new GoAction(ARAD),new GoAction(SIBIU), new GoAction(FAGARAS)),
            solution.fromInitialStatePart());

        assertEquals(Arrays.asList(new GoAction(BUCHAREST)),
            solution.fromGoalStatePart());

        assertEquals(Arrays.asList(
            new GoAction(ARAD),
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST),
            new GoAction(URZICENI)),
            solution.fromInitialStateToGoalState());

        assertEquals(Arrays.asList(
            new GoAction(BUCHAREST),
            new GoAction(FAGARAS),
            new GoAction(SIBIU),
            new GoAction(ARAD),
            new GoAction(TIMISOARA)),
            solution.fromGoalStateToInitialState());
    }

    // TODO - fix bidirectional search to address this.
    @Test @Ignore("fails, actual is [null, Go(Oradea), null]")
    public void zerindToSibiu() throws Exception {
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> pair =
            ProblemFactory.getSimpleBidirectionalSearchProblem(ZERIND, SIBIU);
        final BidirectionalActions<GoAction> solution = searchForActions(pair);

        assertEquals(Arrays.asList(
            new GoAction(ORADEA)),
            solution.fromInitialStateToGoalState());

    }
}