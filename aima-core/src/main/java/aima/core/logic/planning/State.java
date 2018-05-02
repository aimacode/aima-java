package aima.core.logic.planning;

import aima.core.logic.fol.parsing.ast.AtomicSentence;

import java.util.List;

public class State {
    List<AtomicSentence> fluents;

    public State(List<AtomicSentence> fluents) {
        this.fluents = fluents;
    }

    State result(State s, ActionSchema a){
        s.fluents.removeAll(a.getEffectsNegativeLiterals());
        s.fluents.addAll(a.getEffectsPositiveLiterals());
        return s;
    }
}
