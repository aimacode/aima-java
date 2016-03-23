package aima.core.logic.common;

/**
 * Created by Lobster on 23.03.16.
 */

public class Predicate {

    private String namePredicate;

    private String arg1;
    private String arg2;

    private boolean isGoalPredicate;

    public Predicate(String namePredicate, String arg1, String arg2, boolean isGoalPredicate){
        this.namePredicate = namePredicate;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.isGoalPredicate = isGoalPredicate;
    }

    public boolean isGoalPredicate() {
        return isGoalPredicate;
    }

    public String getNamePredicate() {
        return namePredicate;
    }

    public void setNamePredicate(String namePredicate) {
        this.namePredicate = namePredicate;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }
}
