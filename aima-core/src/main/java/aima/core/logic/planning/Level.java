package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.*;


public class Level {
    List<Object> levelObjects;
    HashMap<Object,List<Object>> mutexLinks;//can be planned alternatively
    HashMap<Object,List<Object>> nextLinks;
    HashMap<Object,List<Object>> prevLinks;
    Problem problem;
    public Level(HashMap<Object,List<Object>> linksFromPreviousLevel, Problem problem){
        this.problem = problem;
        levelObjects = new ArrayList<>();
        mutexLinks = new HashMap<>();
        nextLinks = new HashMap<>();
        prevLinks = new HashMap<>();
        for (Object node :
                linksFromPreviousLevel.keySet()) {
            List<Object> thisLevelObjects = linksFromPreviousLevel.get(node);
            for (Object nextNode :
                    thisLevelObjects) {
                if(levelObjects.contains(nextNode)){
                    List<Object> tempPrevLink = prevLinks.get(nextNode);
                    tempPrevLink.add(node);
                    prevLinks.put(nextNode,tempPrevLink);
                }
                else{
                    levelObjects.add(nextNode);
                    prevLinks.put(nextNode, new ArrayList<>(Collections.singletonList(node)));
                }

            }

        }
        calculateMutexLinks();
        calculateNextLinks();
    }

    private void calculateMutexLinks() {

    }

    private void calculateNextLinks() {
        nextLinks = new HashMap<>();
        if(levelObjects.get(0) instanceof Literal){
            for (ActionSchema action :
                    problem.actionSchemas) {
                if (levelObjects.containsAll(action.getPrecondition())){
                    List<Object> nextLevelNodes ;
                    for (Literal literal :
                            action.getPrecondition()) {
                        if (nextLinks.containsKey(literal)){
                            nextLevelNodes = nextLinks.get(literal);
                            nextLevelNodes.add(action);
                        }
                        else{
                            nextLevelNodes = new ArrayList<>(Collections.singletonList(action));
                        }
                        nextLinks.put(literal,nextLevelNodes);
                    }
                }
                    
            }
        }
        else if (levelObjects.get(0) instanceof ActionSchema){
            for (Object action :
                    levelObjects) {
                Literal[] effects = (Literal[]) ((ActionSchema) action).getEffects().toArray();
                nextLinks.put(action,Arrays.asList((Object[])effects));
            }
        }
        
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Level))
            return false;
        return this.levelObjects.containsAll(((Level) obj).levelObjects)
                && ((Level) obj).levelObjects.containsAll(this.levelObjects)
                && this.mutexLinks.equals(((Level) obj).mutexLinks)
                && this.nextLinks.equals(((Level) obj).nextLinks)
                && this.prevLinks.equals(((Level) obj).prevLinks);
    }
}
