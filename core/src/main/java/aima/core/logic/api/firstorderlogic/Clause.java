package aima.core.logic.api.firstorderlogic;

import aima.core.logic.basic.firstorder.kb.data.Literal;

/**
 * An interface representing a First Order Logic (FOL) clause
 *
 * @author samagra .
 */
public interface Clause {

    /**
     * The <I>standardizeVariables()</I> replaces all variables in a given clause with
     * new ones that have not been used before.
     *
     * @return a clause with its variables replaced by new ones that have not been
     *      used before.
     */
    Clause standardizeVariables();

    /**
     * <I>getConsequence()</I> returns the conclusion of a clause if the clause can be represented
     * as an Implication.
     *
     * @return the consequence of the implication clause
     */
    Literal getConsequence();

    boolean isDefiniteClause();

    boolean isUnitClause();

    boolean isImplicationDefiniteClause();

    boolean isHornClause();

}
