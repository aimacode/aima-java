package aima.core.logic.planning;

import java.util.*;

/**
 * The data structure for calculating and holding the levels of a planning graph.
 *
 * @author samagra
 * @author Ruediger Lunde
 */
public class Level<CURR, PREV> {
    private final List<CURR> levelObjects = new ArrayList<>();
    HashMap<CURR, List<PREV>> prevLinks = new HashMap<>();
    HashMap<CURR, List<PREV>> nextLinks = new HashMap<>();
    private final HashMap<CURR, List<CURR>> mutexLinks = new HashMap<>(); // can be planned alternatively

    private Level<PREV, CURR> prevLevel;

    public Level(List<CURR> levelObjects, PlanningProblem problem) {
        // store level objects and prevLinks
        this.levelObjects.addAll(levelObjects);
        for (CURR obj : levelObjects)
            prevLinks.put(obj, new ArrayList<>());
    }

    public Level(Level<PREV, CURR> prevLevel, PlanningProblem problem) {
        // store level objects and prevLinks
        this.prevLevel = prevLevel;
        HashMap<PREV, List<CURR>> linksFromPreviousLevel = prevLevel.nextLinks;
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

    public List<CURR> getLevelObjects() {
        return levelObjects;
    }

    public List<PREV> getLinkedPrevObjects(CURR obj) {
        return prevLinks.get(obj);
    }

    // for testing
    public List<PREV> getLinkedNextObjects(CURR obj) {
        return nextLinks.get(obj);
    }

    public Level<PREV, CURR> getPrevLevel() {
        return prevLevel;
    }

    public HashMap<CURR, List<CURR>> getMutexLinks() {
        return mutexLinks;
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
