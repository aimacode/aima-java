package aima.test.unit.search.uninformed;

import aima.core.environment.map2d.GoAction;
import aima.core.environment.map2d.InState;
import aima.core.search.api.BidirectionalProblem;
import aima.core.search.basic.uninformed.BidirectionalBreadthFirstSearch;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.ARAD;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.BUCHAREST;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.CRAIOVA;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.DOBRETA;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.FAGARAS;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.IASI;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.LUGOJ;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.MEHADIA;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.NEAMT;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.ORADEA;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.RIMNICU_VILCEA;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.SIBIU;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.TIMISOARA;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.URZICENI;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.VASLUI;
import static aima.core.environment.map2d.SimplifiedRoadMapOfPartOfRomania.ZERIND;
import static aima.core.environment.support.ProblemFactory.getBidirectionalRomaniaRoadMapProblem;

/**
* @author wormi
*/
public class BiDirectionalBreadthFirstTest {

  private List<GoAction> searchSolution(BidirectionalProblem<GoAction, InState> problem) {
    BidirectionalBreadthFirstSearch<GoAction, InState> search = new BidirectionalBreadthFirstSearch<>();
    try {
      return search.findSolution(problem);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException((e));
    }
  }

  @Test
  public void arad2arad() {
    Assert.assertEquals(Arrays.asList((String) null),
        searchSolution(getBidirectionalRomaniaRoadMapProblem(ARAD, ARAD)));
  }

  @Test
  public void arad2sibiu() {
    final String start = ARAD;
    final String goal = SIBIU;
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU)),
        searchSolution(getBidirectionalRomaniaRoadMapProblem(start, goal)));
  }

  @Test
  public void arad2fagaras() {
    final String start = ARAD;
    final String goal = FAGARAS;
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS)),
        searchSolution(getBidirectionalRomaniaRoadMapProblem(start, goal)));
  }

  @Test
  public void arad2bucharest() {
    final String start = ARAD;
    final String goal = BUCHAREST;
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST)),
        searchSolution(getBidirectionalRomaniaRoadMapProblem(start, goal)));
  }

  @Test
  public void arad2neamt() {
    final String start = ARAD;
    final String goal = NEAMT;
    List<GoAction> actual = searchSolution(getBidirectionalRomaniaRoadMapProblem(start, goal));

    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST),
            new GoAction(URZICENI),
            new GoAction(VASLUI),
            new GoAction(IASI),
            new GoAction(NEAMT)),
        actual);
  }

  @Test
  public void arad2urziceni() {
    final String start = ARAD;
    final String goal = URZICENI;
    Assert.assertEquals(
        Arrays.asList(
            new GoAction(SIBIU),
            new GoAction(FAGARAS),
            new GoAction(BUCHAREST),
            new GoAction(URZICENI)
        ),
        searchSolution(getBidirectionalRomaniaRoadMapProblem(start, goal)));
  }

  @Test
  public void timisoara2Zerind() {
    final String start = TIMISOARA;
    final String goal = ARAD;
    List<GoAction> actual = searchSolution(getBidirectionalRomaniaRoadMapProblem(start, goal));
    List<GoAction> expected = Arrays.asList(new GoAction(ARAD));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void timisoara2bucharestOrCraiova() {

    final String start = TIMISOARA;
    final String goal = CRAIOVA;
    List<GoAction> actual = searchSolution(getBidirectionalRomaniaRoadMapProblem(start, goal));

    List<GoAction> expected;
    // three solutions valid
    if (actual.contains(new GoAction(RIMNICU_VILCEA))) {
      expected = Arrays.asList(
          new GoAction(ARAD),
          new GoAction(SIBIU),
          new GoAction(RIMNICU_VILCEA),
          new GoAction(CRAIOVA)
      );
    } else {
      expected = Arrays.asList(
          new GoAction(LUGOJ),
          new GoAction(MEHADIA),
          new GoAction(DOBRETA),
          new GoAction(CRAIOVA)
      );
    }
    Assert.assertEquals(expected, actual);
  }


  @Test
  public void arad2oreda() {

    List<GoAction> expected;
    final String start = ARAD;
    final String goal = ORADEA;
    List<GoAction> actual = searchSolution(getBidirectionalRomaniaRoadMapProblem(start, goal));

    // has two possible results
    if (actual.contains(new GoAction(ZERIND))) {
      expected = Arrays.asList(
          new GoAction(ZERIND),
          new GoAction(ORADEA));
    } else {
      expected = Arrays.asList(
          new GoAction(SIBIU),
          new GoAction(ORADEA));
    }

    Assert.assertEquals(expected, actual);
  }


}
