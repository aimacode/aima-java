package aima.core.logic.api.firstorderlogic;

import aima.core.logic.basic.firstorder.kb.data.Literal;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An interface representing the First Order Logic (FOL) Knowledge Base
 *
 * @author samagra
 */
public interface FOLKnowledgeBase {
    /**
     * Adds the specified sentence to the knowledge base.
     *
     * @param sentence a fact to be added to the knowledge base.
     */
    void tell(String sentence);

    /**
     * Adds the specified sentences to the knowledge base.
     *
     * @param sentences A set of facts to be added to the knowledge base.
     */
    void tell(Set<? extends Literal> sentences);

    /**
     * @return A list of all the definite implication Clauses useful for inference
     *      algorithms.
     */
    List<Clause> getAllDefiniteClauseImplications();


    /**
     * Used to check if a sentence is a <b>renaming</b> of a known fact. One sentence is a
     * renaming of another if they are identical except for the names of the variables.
     *
     * @param sentence The sentence which is to be checked.
     *
     * @return a boolean which is true when the sentence is a <b>renaming</b> and else false.
     */
    boolean isRenaming(Literal sentence);

    boolean isRenaming(Literal sentence, Set<Literal> base);

    /**
     * <I>fetch(q)</I> returns all substitutions such that query q unifies with some sentence in
     * the knowledge base.
     *
     * @param q The clause for which the substitutions are required
     *
     * @return A set of valid substitutions such that query q unifies with some
     *      sentence in the knowledge base.
     */
    Set<Map<Variable, Term>> fetch(Clause q);


}
