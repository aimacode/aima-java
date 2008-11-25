package aima.logic.fol.parsing.ast;

import java.util.List;

/**
 * @author Ciaran O'Reilly
 * 
 */
public interface AtomicSentence extends Sentence {
	String getSymbolicName();
	List<Term> getTerms();
}
