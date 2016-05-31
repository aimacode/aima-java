package aima.core.logic.propositional.api;

import java.util.List;

import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * @author Anurag Rai
 */

public interface SimpleKnowledgeBase {
	
	public void tell(String aSentence);
	public List<Sentence> getSentences();

}
