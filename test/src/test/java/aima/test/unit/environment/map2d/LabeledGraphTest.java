package aima.test.unit.environment.map2d;

import aima.core.environment.map2d.LabeledGraph;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author samagra
 */
public class LabeledGraphTest {
    // In this graph all non-co-prime pairs of integers
    // are connected
    private LabeledGraph<Integer,Pair<Integer,Integer>> graph;
    @Before
    public void setup(){
        graph = new LabeledGraph<>();
    }

    @Test
    public void testAddVertex(){
        Assert.assertEquals(0,graph.getVertexLabels().size());
        graph.addVertex(2);
        Assert.assertEquals(1,graph.getVertexLabels().size());
        Assert.assertTrue(graph.isVertexLabel(2));
        Assert.assertFalse(graph.isVertexLabel(1));
        graph.addVertex(3);
        Assert.assertEquals(2,graph.getVertexLabels().size());
        Assert.assertTrue(graph.isVertexLabel(3));
        Assert.assertTrue(graph.isVertexLabel(2));
        Assert.assertFalse(graph.isVertexLabel(1));
        List<Integer> testList = new ArrayList<>();
        testList.add(2);
        testList.add(3);
        Assert.assertEquals(testList,graph.getVertexLabels());
    }

    @Test
    public void testEdgeCreation(){
        graph.addVertex(2);
        Assert.assertEquals(1,graph.getVertexLabels().size());
        Assert.assertTrue(graph.isVertexLabel(2));
        graph.addVertex(6);
        Assert.assertEquals(2,graph.getVertexLabels().size());
        Assert.assertTrue(graph.isVertexLabel(6));
        graph.set(2,6,new Pair<>(2,6));
        Assert.assertEquals(2,graph.getVertexLabels().size());
        graph.set(3,12,new Pair<>(3,12));
        Assert.assertEquals(4,graph.getVertexLabels().size());
        Assert.assertTrue(graph.isVertexLabel(3));
        Assert.assertTrue(graph.isVertexLabel(12));
        graph.set(2,12,new Pair<>(2,12));
        Assert.assertEquals(4,graph.getVertexLabels().size());
        graph.set(3,6,new Pair<>(3,6));
        Assert.assertEquals(4,graph.getVertexLabels().size());
        graph.set(6,12, new Pair<>(6,12));
        Assert.assertEquals(4,graph.getVertexLabels().size());
    }

    @Test
    public void testGetSuccessors(){
        Assert.assertEquals(new ArrayList<>(),graph.getSuccessors(2));
        graph.set(2,12,new Pair<>(2,12));
        graph.set(3,12,new Pair<>(3,12));
        graph.set(2,6,new Pair<>(2,6));
        graph.set(3,6,new Pair<>(3,6));
        graph.set(6,12,new Pair<>(2,12));
        List<Integer> testList = graph.getSuccessors(2);
        testList.sort(null);
        Assert.assertEquals(new ArrayList<>(Arrays.asList(6, 12)),testList);
        testList = graph.getSuccessors(3);
        testList.sort(null);
        Assert.assertEquals(new ArrayList<>(Arrays.asList(6, 12)),testList);
        Assert.assertEquals(new ArrayList<>(Collections.singletonList(12)),graph.getSuccessors(6));
        Assert.assertEquals(new ArrayList<>(),graph.getSuccessors(12));
    }

}