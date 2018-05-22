package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.*;

/**
 * The data structure for calculating and holding the levels of a planning graph.
 *
 * @author samagra
 */
public class Level {
    List<Object> levelObjects;
    HashMap<Object, List<Object>> mutexLinks;//can be planned alternatively
    HashMap<Object, List<Object>> nextLinks;
    HashMap<Object, List<Object>> prevLinks;
    Problem problem;
    Level prevLevel;

    public Level(Level prevLevel, Problem problem) {
        this.prevLevel = prevLevel;
        this.problem = problem;
        if (prevLevel != null) {
            HashMap<Object, List<Object>> linksFromPreviousLevel = prevLevel.getNextLinks();
            this.problem = problem;
            levelObjects = new ArrayList<>();
            prevLinks = new HashMap<>();
            for (Object node :
                    linksFromPreviousLevel.keySet()) {
                List<Object> thisLevelObjects = linksFromPreviousLevel.get(node);
                for (Object nextNode :
                        thisLevelObjects) {
                    if (levelObjects.contains(nextNode)) {
                        List<Object> tempPrevLink = prevLinks.get(nextNode);
                        tempPrevLink.add(node);
                        prevLinks.put(nextNode, tempPrevLink);
                    } else {
                        levelObjects.add(nextNode);
                        prevLinks.put(nextNode, new ArrayList<>(Collections.singletonList(node)));
                    }

                }

            }
            addNoPrecondActions();
            calculateNextLinks();
            calculateMutexLinks(prevLevel);
        } else {
            levelObjects = new ArrayList<>();
            prevLinks = new HashMap<>();
            levelObjects.addAll(problem.getInitialState().getFluents());
            for (Object obj :
                    levelObjects) {
                prevLinks.put(obj, new ArrayList<>());
            }
            addNoPrecondActions();
            calculateNextLinks();
            calculateMutexLinks(null);
        }
        addPersistentActions();
    }

    public Level(Level prevLevel, Problem problem, String extraLiterals){
        this(prevLevel, problem);
        this.addExtraLiterals(extraLiterals);
    }

    public void addExtraLiterals(String s){
        for (Literal literal :
                Utils.parse(s)) {
            if(!levelObjects.contains(literal)){
                levelObjects.add(literal);
            }
        }
        calculateNextLinks();
        calculateMutexLinks(getPrevLevel());
        addPersistentActions();
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

    private void addPersistentActions() {
       if(getLevelObjects().get(0) instanceof Literal) {
           for (Object literal :
                   getLevelObjects()) {
               ActionSchema action = new ActionSchema("No-op", null,
                       Collections.singletonList((Literal) literal),
                       Collections.singletonList((Literal) literal));
               addToHashMap(literal, action, nextLinks);
           }
       }
    }

    public void addNoPrecondActions(){
        if(getLevelObjects().get(0) instanceof ActionSchema){
            for (ActionSchema action :
                    problem.getPropositionalisedActions()) {
                if (action.getPrecondition().size()==0)
                    levelObjects.add(action);
            }
        }
    }



    private void calculateMutexLinks(Level prevLevel) {
        mutexLinks = new HashMap<>();
        if(prevLevel == null) return;
        if (levelObjects.get(0) instanceof Literal) {
            Literal firstLiteral, secondLiteral;
            List<Object> possibleActionsFirst, possibleActionsSecond;
            for (int i = 0; i < levelObjects.size(); i++) {
                firstLiteral = (Literal) levelObjects.get(i);
                possibleActionsFirst = prevLinks.get(firstLiteral);
                for (int j = i; j < levelObjects.size(); j++) {
                    secondLiteral = (Literal) levelObjects.get(j);
                    possibleActionsSecond = prevLinks.get(secondLiteral);
                    if (firstLiteral.getAtomicSentence().getSymbolicName().equals(
                            secondLiteral.getAtomicSentence().getSymbolicName()) &&
                            ((firstLiteral.isNegativeLiteral() && secondLiteral.isPositiveLiteral()) ||
                                    firstLiteral.isPositiveLiteral() && secondLiteral.isNegativeLiteral()
                            )) {

                        addToHashMap(firstLiteral, secondLiteral, mutexLinks);
                        addToHashMap(secondLiteral, firstLiteral, mutexLinks);
                    } else {
                        boolean eachPossiblePairExclusive = true;
                        HashMap<Object, List<Object>> prevMutexes = prevLevel.getMutexLinks();
                        for (Object firstAction :
                                possibleActionsFirst) {
                            for (Object secondAction :
                                    possibleActionsSecond) {
                                if ((!prevMutexes.containsKey(firstAction))||(!prevMutexes.get(firstAction).contains(secondAction))) {
                                    eachPossiblePairExclusive = false;
                                }
                            }
                        }
                        if (eachPossiblePairExclusive) {
                            addToHashMap(firstLiteral, secondLiteral, mutexLinks);
                            addToHashMap(secondLiteral, firstLiteral, mutexLinks);
                        }
                    }
                }
            }
        } else if (levelObjects.get(0) instanceof ActionSchema) {
            ActionSchema firstAction, secondAction;
            boolean checkMutex;

            for (int i = 0; i < levelObjects.size(); i++) {
                firstAction = (ActionSchema) levelObjects.get(i);
                List<Literal> firstActionEffects = firstAction.getEffects();
                List<Literal> firstActionPositiveEffects = firstAction.getEffectsPositiveLiterals();
                List<Literal> firstActionPreconditions = firstAction.getPrecondition();
                for (int j = i+1; j < levelObjects.size(); j++) {
                    checkMutex = false;
                    secondAction = (ActionSchema) levelObjects.get(j);
                    List<Literal> secondActionEffects = secondAction.getEffects();
                    List<Literal> secondActionNegatedLiterals = secondAction.getEffectsNegativeLiterals();
                    List<Literal> secondActionPreconditions = secondAction.getPrecondition();
                    for (Literal posLiteral :
                            firstActionPositiveEffects) {
                        for (Literal negatedLit :
                                secondActionNegatedLiterals) {
                            if (posLiteral.equals(new Literal(negatedLit.getAtomicSentence(),false))
                            ) {
                                checkMutex = true;
                            }
                        }
                    }
                    if (!checkMutex) {
                        if (checkInterference(secondActionPreconditions, firstActionEffects)) {
                            checkMutex = true;
                        }
                        if (checkInterference(firstActionPreconditions, secondActionEffects)) {
                            checkMutex = true;
                        }
                    }
                    if (!checkMutex) {
                        HashMap<Object, List<Object>> prevMutex = prevLevel.getMutexLinks();
                        if(prevMutex!=null) {
                            for (Literal firstActionPrecondition :
                                    firstActionPreconditions) {
                                for (Literal secondActionPrecondition :
                                        secondActionPreconditions) {
                                    if (prevMutex.get(firstActionPrecondition) != null && prevMutex.get(firstActionPrecondition).contains(secondActionPrecondition)) {
                                        checkMutex = true;
                                    }
                                }

                            }
                        }
                    }
                    if (checkMutex) {
                        addToHashMap(firstAction, secondAction, mutexLinks);
                        addToHashMap(secondAction, firstAction, mutexLinks);
                    }
                }

            }
        }
    }

    private boolean checkInterference(List<Literal> firstActionPreconditions, List<Literal> secondActionEffects) {
        boolean checkMutex = false;
        for (Literal secondActionEffect :
                secondActionEffects) {
            for (Literal firstActionPrecondition :
                    firstActionPreconditions) {
                if (secondActionEffect.equals(new Literal(firstActionPrecondition.getAtomicSentence(),firstActionPrecondition.isPositiveLiteral())))
                {
                        checkMutex = true;
                }

            }
        }
        return checkMutex;
    }

    private void addToHashMap(Object firstObject, Object secondObject, HashMap<Object, List<Object>> map) {
        List<Object> tempList;
        if (map.containsKey(firstObject)) {
            tempList = map.get(firstObject);
            tempList.add(secondObject);
            map.put(firstObject, tempList);
        } else {
            map.put(firstObject, new ArrayList<>(Collections.singletonList(secondObject)));
        }
    }

    private void calculateNextLinks() {
        nextLinks = new HashMap<>();
        if (levelObjects.get(0) instanceof Literal) {
            for (ActionSchema action :
                    problem.getPropositionalisedActions()) {
                if (levelObjects.containsAll(action.getPrecondition())) {
                    List<Object> nextLevelNodes;
                    for (Literal literal :
                            action.getPrecondition()) {
                        if (nextLinks.containsKey(literal)) {
                            nextLevelNodes = nextLinks.get(literal);
                            nextLevelNodes.add(action);
                        } else {
                            nextLevelNodes = new ArrayList<>(Collections.singletonList(action));
                        }
                        nextLinks.put(literal, nextLevelNodes);
                    }
                }

            }
        } else if (levelObjects.get(0) instanceof ActionSchema) {
            for (Object action :
                    levelObjects) {
                Object[] effects =  ((ActionSchema) action).getEffects().toArray();
                nextLinks.put(action, new ArrayList<>(Arrays.asList(effects)));
            }
        }

    }

    public Level getPrevLevel() {
        return prevLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Level))
            return false;
        return this.levelObjects.containsAll(((Level) obj).levelObjects)
                && ((Level) obj).levelObjects.containsAll(this.levelObjects)
                && this.mutexLinks.equals(((Level) obj).mutexLinks)
                && this.nextLinks.equals(((Level) obj).nextLinks)
                && this.prevLinks.equals(((Level) obj).prevLinks);
    }
}
