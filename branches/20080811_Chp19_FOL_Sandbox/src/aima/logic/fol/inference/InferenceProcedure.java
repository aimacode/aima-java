package aima.logic.fol.inference;

import java.util.Map;
import java.util.Set;

import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface InferenceProcedure {
	/**
	 * 
	 * @param kb
	 *            the knowledge base against which the query is to be made.
	 * @param aQuery
	 *            to be answered.
	 * @return two possible return values exist. 1. an empty Set, the query
	 *         returned false. 2. a Set of substitutions, indicates true and the
	 *         bindings for different possible answers to the query (Note: refer
	 *         to page 256).
	 */
	Set<Map<Variable, Term>> ask(FOLKnowledgeBase kb, Sentence aQuery);
}
