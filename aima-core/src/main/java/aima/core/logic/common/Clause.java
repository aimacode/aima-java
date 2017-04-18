package aima.core.logic.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lobster on 23.03.16.
 */

public class Clause {

    private List<Predicate> expression;

    public Clause(){
        expression = new ArrayList<>();
    }

    public Clause(Predicate predicate){
        expression = new ArrayList<>();
        expression.add(predicate);
    }

    public void addPredicate(Predicate predicate){
        expression.add(predicate);
    }

    public Predicate getLastPredicate(){
        return expression.get(expression.size()-1);
    }

    public List<Predicate> getExpression() {
        return expression;
    }

    public void setExpression(List<Predicate> expression) {
        this.expression = expression;
    }
}
