package aima.core.logic.api.propositional;

import java.util.List;

import aima.core.logic.basic.propositional.parsing.ast.Sentence;

/**
 * @author Anurag Rai
 */

public interface SimpleKnowledgeBase {
	
	public void tell(String aSentence);
	public List<Sentence> getSentences();

}
