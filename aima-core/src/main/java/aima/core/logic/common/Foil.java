package aima.core.logic.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Lobster on 23.03.16.
 */

abstract public class Foil {

    public List<Clause> foil(List<Predicate> examples, Predicate target){
        List<Clause> clauses = new ArrayList<>();
        Clause clause;

        while (containingPositivePredicate(examples, target)){
            clause = newClause(examples, target);
            deleteExamples(clause, examples);
            clauses.add(clause);
            target = clause.getLastPredicate();
        }

        return clauses;
    }

    private boolean containingPositivePredicate(List<Predicate> predicates, Predicate goalPredicate){
        if (goalPredicate.isGoalPredicate()){
            for (Predicate p: predicates){
                if(p.getArg1().equals(goalPredicate.getArg1())){
                    return true;
                }
            }
        } else {
            for (Predicate p: predicates){
                if(p.getArg1().equals(goalPredicate.getArg2())){
                    return true;
                }
            }
        }

        return false;
    }

    private Clause newClause(List<Predicate> examples, Predicate target){
        Clause clause = new Clause(target);
        Predicate literal;
        List<Predicate> extended_examples = new ArrayList<>(examples);

        while (containingNegativePredicate(extended_examples,target)){
            literal = chooseLiteral(newLiteral(clause), extended_examples);
            clause.addPredicate(literal);
            extended_examples = extendExample(extended_examples, literal);
        }

        return clause;
    }

    public abstract List<Predicate> newLiteral(Clause clause);

    public abstract Predicate chooseLiteral(List<Predicate> literals, List<Predicate> examples);

    private List<Predicate> extendExample(List<Predicate> examples, Predicate predicate){
        List<Predicate> extendedExamples = new ArrayList<>();
        for (Predicate p: examples){
            if (p.getArg1().equals(predicate.getArg1())){
                for (Predicate p2: examples){
                    extendedExamples.add(new Predicate(p.getNamePredicate(),predicate.getArg1(),p.getArg2(),false));
                }
            }
        }

        return extendedExamples;
    }

    private boolean containingNegativePredicate(List<Predicate> predicates, Predicate goalPredicate){
        if (goalPredicate.isGoalPredicate()){
            for (Predicate p: predicates){
                if(p.getArg1().equals(goalPredicate.getArg1())){
                    return true;
                }
            }
        } else {
            for (Predicate p: predicates){
                if(p.getArg1().equals(goalPredicate.getArg2())){
                    return true;
                }
            }
        }

        return false;
    }

    private void deleteExamples(Clause clause, List<Predicate> predicates){
        Predicate currentPredicate;
        Iterator<Predicate> iteratorPredicate = clause.getExpression().iterator();
        while (iteratorPredicate.hasNext()){
            currentPredicate = iteratorPredicate.next();

            if (predicates.contains(currentPredicate)){
                predicates.remove(currentPredicate);
            }
        }
    }
}
