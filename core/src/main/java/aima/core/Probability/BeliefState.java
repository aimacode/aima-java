package aima.core.Probability;

public interface BeliefState<A,P> {
    void update(A action,P percept);



}
