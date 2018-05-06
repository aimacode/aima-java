package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.AtomicSentence;

import java.util.List;

public class State {
    List<Literal> fluents;

    public State(List<Literal> fluents) {
        this.fluents = fluents;
    }
    public State(String fluents){
        this(Utils.parse(fluents));}

    State result(State s, ActionSchema a){
        s.fluents.removeAll(a.getEffectsNegativeLiterals());
        s.fluents.addAll(a.getEffectsPositiveLiterals());
        return s;
    }

    boolean isApplicable(State s, ActionSchema a){
        return s.getFluents().containsAll(a.getPrecondition());
    }

    public List<Literal> getFluents() {
        return fluents;
    }
}
