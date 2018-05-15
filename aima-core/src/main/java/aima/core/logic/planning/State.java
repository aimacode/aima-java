package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;

import java.util.List;

public class State {
    List<Literal> fluents;
    int hashCode = 0;

    public State(List<Literal> fluents) {
        this.fluents = fluents;
    }

    public State(String fluents) {
        this(Utils.parse(fluents));
    }

    public State result(ActionSchema a) {
        if (this.isApplicable(a)) {
            for (Literal fluent :
                    a.getEffectsNegativeLiterals()) {
                Literal tempFluent = new Literal(fluent.getAtomicSentence());
                this.fluents.remove(tempFluent);
            }
            this.fluents.addAll(a.getEffectsPositiveLiterals());
        }
        return this;
    }


    public boolean isApplicable(ActionSchema a) {
        return this.getFluents().containsAll(a.getPrecondition());
    }

    public List<Literal> getFluents() {
        return fluents;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State))
            return false;
        return this.fluents.containsAll(((State) obj).getFluents())
                && ((State) obj).getFluents().containsAll(this.getFluents());
    }

    @Override
    public int hashCode() {
        hashCode = 17;
        for (Literal fluent :
                getFluents()) {
            hashCode = 37 * hashCode + fluent.hashCode();
        }
        return hashCode;
    }
}
