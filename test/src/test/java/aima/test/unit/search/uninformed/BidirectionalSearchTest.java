package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.environment.support.ProblemFactory;
import aima.core.search.api.BidirectionalActions;
import aima.core.search.api.Problem;
import aima.core.search.basic.uninformed.BidirectionalSearch;
import aima.core.util.datastructure.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    public <A, S> List<A> searchForActions(Pair<Problem<A, S>, Problem<A, S>> pair) {
        BidirectionalSearch<A, S> search = new BidirectionalSearch<>();
        return search.apply(pair);
    }

    @Test
    public void aradToArad() throws Exception {
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> pair =
            ProblemFactory.getSimpleBidirectionalSearchProblem(ARAD, ARAD);

        assertEquals(Collections.EMPTY_LIST, searchForActions(pair));
    }

    @Test
    public void timisoaraToUrziceni() throws Exception {
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> pair =
            ProblemFactory.getSimpleBidirectionalSearchProblem(TIMISOARA, URZICENI);
        final List<GoAction> solution = searchForActions(pair);

        assertEquals(Arrays.asList(
            new GoAction(ARAD),
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST),
            new GoAction(URZICENI)),
            solution);
    }

    @Test
    public void zerindToSibiu() throws Exception {
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> pair =
            ProblemFactory.getSimpleBidirectionalSearchProblem(ZERIND, SIBIU);
        final List<GoAction> solution = searchForActions(pair);

        assertEquals(Arrays.asList(
            new GoAction(ORADEA),
            new GoAction(SIBIU)),
            solution);

    }

    @Test
    public void aradToBucharest() throws Exception {
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> pair =
            ProblemFactory.getSimpleBidirectionalSearchProblem(ARAD, BUCHAREST);
        final List<GoAction> solution = searchForActions(pair);

        assertEquals(Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST)),
            solution);

    }

    @Test
    public void aradToSibiu() throws Exception {
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> pair =
            ProblemFactory.getSimpleBidirectionalSearchProblem(ARAD, SIBIU);
        final List<GoAction> solution = searchForActions(pair);

        assertEquals(Arrays.asList(
            new GoAction(SIBIU)),
            solution);

    }

    @Test
    public void aradToFagras() throws Exception {
        Pair<Problem<GoAction, InState>, Problem<GoAction, InState>> pair =
            ProblemFactory.getSimpleBidirectionalSearchProblem(ARAD, FAGARAS);
        final List<GoAction> solution = searchForActions(pair);

        assertEquals(Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS)),
            solution);

    }
}