package aima.core.logic.api.propositional;

import java.util.List;

import aima.core.logic.basic.propositional.parsing.ast.Sentence;

/**
 * @author Anurag Rai
 */

public interface KnowledgeBase {
	
	/**
	 * Adds the specified sentence to the knowledge base.
	 * 
	 * @param aSentence
	 *            a fact to be added to the knowledge base.
	 */
	void tell(String aSentence);
	
	/**
	 * Returns the list of sentences in the knowledge base chained together as a
	 * single sentence.
	 * 
	 * @return the list of sentences in the knowledge base chained together as a
	 *         single sentence.
	 */
	Sentence asSentence();
	
	List<Sentence> getSentences();
}
