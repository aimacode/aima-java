package aima.core.logic.api.firstorderlogic;

import aima.core.logic.basic.firstorder.kb.data.Literal;

public interface Clause {
    Clause standardizeVariables();
    Literal getConsequence();
}
