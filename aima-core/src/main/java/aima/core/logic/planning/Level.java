package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.*;


public class Level {
    List<Object> levelObjects;
    HashMap<Object,List<Object>> mutexLinks;//can be planned alternatively
    HashMap<Object,List<Object>> nextLinks;
    HashMap<Object,List<Object>> prevLinks;
    Problem problem;
    public Level(Level prevLevel, Problem problem){
        HashMap<Object,List<Object>> linksFromPreviousLevel = prevLevel.getNextLinks();
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
        addPersistentActions(linksFromPreviousLevel);
        calculateNextLinks();
        calculateMutexLinks();
    }

    public List<Object> getLevelObjects() {
        return levelObjects;
    }

    public HashMap<Object, List<Object>> getMutexLinks() {
        return mutexLinks;
    }

    public HashMap<Object, List<Object>> getNextLinks() {
        return nextLinks;
    }

    public HashMap<Object, List<Object>> getPrevLinks() {
        return prevLinks;
    }

    public Problem getProblem() {
        return problem;
    }

    private void addPersistentActions(HashMap<Object, List<Object>> linksFromPreviousLevel) {
        Object[] keys = linksFromPreviousLevel.keySet().toArray();
        if(keys[0] instanceof Literal){
            for (Object literal :
                    keys) {
                ActionSchema action = new ActionSchema("No-op",null,
                        Collections.singletonList((Literal)literal),
                        Collections.singletonList((Literal)literal));
                levelObjects.add(action);
            }
        }
    }

    private void calculateMutexLinks(Level prevLevel) {
        if(levelObjects.get(0) instanceof Literal) {
            Literal firstLiteral,secondLiteral;
            List<Object> possibleActionsFirst, possibleActionsSecond;
            List<Object> tempList;
            for (int i = 0; i < levelObjects.size(); i++) {
                firstLiteral= (Literal)levelObjects.get(i);
                possibleActionsFirst = prevLinks.get(firstLiteral);
                for (int j = i; j < levelObjects.size(); j++) {
                    secondLiteral = (Literal) levelObjects.get(j);
                    possibleActionsSecond = prevLinks.get(secondLiteral);
                    if(firstLiteral.getAtomicSentence().getSymbolicName().equals(
                            secondLiteral.getAtomicSentence().getSymbolicName())&&
                                    ((firstLiteral.isNegativeLiteral()&&secondLiteral.isPositiveLiteral())||
                                    firstLiteral.isPositiveLiteral()&&secondLiteral.isNegativeLiteral()
                                    )){

                        addToHashMap(firstLiteral, secondLiteral, mutexLinks);
                        addToHashMap(secondLiteral, secondLiteral, mutexLinks);
                    }
                    else {
                        boolean eachPossiblePairExclusive = true;
                        HashMap<Object,List<Object>> prevMutexes = prevLevel.getMutexLinks();
                        for (Object firstAction :
                                possibleActionsFirst) {
                            for (Object secondAction :
                                    possibleActionsSecond) {
                                if(!prevMutexes.get(firstAction).contains(secondAction)){
                                    eachPossiblePairExclusive = false;
                                }
                            }
                        }
                        if(eachPossiblePairExclusive){
                            addToHashMap(firstLiteral,secondLiteral,mutexLinks);
                            addToHashMap(secondLiteral,firstLiteral,mutexLinks);
                        }
                    }
                }
            }
        }
        else if (levelObjects.get(0) instanceof ActionSchema){


        }
    }

    private void addToHashMap(Object firstObject, Object secondObject, HashMap<Object,List<Object>> map) {
        List<Object> tempList;
        if(map.containsKey(firstObject)){
            tempList = map.get(firstObject);
            tempList.add(secondObject);
            map.put(firstObject,tempList);
        }
        else{
            map.put(firstObject, Collections.singletonList(secondObject));
        }
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
