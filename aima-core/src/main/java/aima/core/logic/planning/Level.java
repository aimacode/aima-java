package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.*;

/**
 * The data structure for calculating and holding the levels of a planning graph.
 *
 * @author samagra
 * @author Ruediger Lunde
 */
public class Level<CURR, PREV> {
    List<CURR> levelObjects = new ArrayList<>();
    HashMap<CURR, List<PREV>> prevLinks = new HashMap<>();
    HashMap<CURR, List<PREV>> nextLinks = new HashMap<>();
    HashMap<CURR, List<CURR>> mutexLinks = new HashMap<>();; //can be planned alternatively

    Level<PREV, CURR> prevLevel;

    public Level(List<CURR> levelObjects, Problem problem) {
        // store level objects and prevLinks
        this.levelObjects.addAll(levelObjects);
        for (CURR obj : levelObjects)
            prevLinks.put(obj, new ArrayList<>());
    }

    public Level(Level<PREV, CURR> prevLevel, Problem problem) {
        // store level objects and prevLinks
        this.prevLevel = prevLevel;
        HashMap<PREV, List<CURR>> linksFromPreviousLevel = prevLevel.getNextLinks();
        for (PREV obj : linksFromPreviousLevel.keySet()) {
            List<CURR> thisLevelObjects = linksFromPreviousLevel.get(obj);
            for (CURR nextObj : thisLevelObjects) {
                if (!levelObjects.contains(nextObj)) {
                    levelObjects.add(nextObj);
                    prevLinks.put(nextObj, new ArrayList<>());
                }
                prevLinks.get(nextObj).add(obj);
            }
        }
    }

    /*
    // for testing only...
    public Level(Level<PREV, CURR> prevLevel, Problem problem, String extraLiterals) {
        this(prevLevel, problem);
        for (Literal literal : Utils.parse(extraLiterals)) {
            if(!levelObjects.contains(literal))
                levelObjects.add(literal);
        }
        calculateNextLinks(problem);
        calculateMutexLinks(getPrevLevel());
        addPersistenceActions();
    }
     */

    public List<CURR> getLevelObjects() {
        return levelObjects;
    }

    public HashMap<CURR, List<CURR>> getMutexLinks() {
        return mutexLinks;
    }

    public HashMap<CURR, List<PREV>> getNextLinks() {
        return nextLinks;
    }

    public HashMap<CURR, List<PREV>> getPrevLinks() {
        return prevLinks;
    }

    public Level<PREV, CURR> getPrevLevel() {
        return prevLevel;
    }

    public void addToPrevLinks(CURR currObject, PREV prevObject) {
        List<PREV> list = prevLinks.computeIfAbsent(currObject, k -> new ArrayList<>());
        list.add(prevObject);
    }

    public void addToNextLinks(CURR currObject, PREV nextObject) {
        List<PREV> list = nextLinks.computeIfAbsent(currObject, k -> new ArrayList<>());
        list.add(nextObject);
    }

    public void putToNextLinks(CURR currObject, ArrayList<PREV> nextObjects) {
        nextLinks.put(currObject, nextObjects);
    }

    public void addToMutexLinks(CURR firstObject, CURR secondObject) {
        List<CURR> list = mutexLinks.computeIfAbsent(firstObject, k -> new ArrayList<>());
        list.add(secondObject);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false;
        Level<?, ?> other = (Level<?, ?>) obj;
        return levelObjects.containsAll(other.levelObjects)
                && other.levelObjects.containsAll(levelObjects)
                && mutexLinks.equals(other.mutexLinks)
                && nextLinks.equals(other.nextLinks)
                && prevLinks.equals(other.prevLinks);
    }
}
