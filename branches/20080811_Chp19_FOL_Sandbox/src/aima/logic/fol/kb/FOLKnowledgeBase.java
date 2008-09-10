package aima.logic.fol.kb;

import java.util.List;
import java.util.Map;

import aima.logic.fol.FOLDomain;
import aima.logic.fol.Unifier;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * A First Order Logic (FOL) Knowledge Base.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLKnowledgeBase {
	
	private FOLParser parser;

	//
	// PUBLIC METHODS
	//
	
	public FOLKnowledgeBase(FOLDomain domain) {
		parser = new FOLParser(domain);
	}

	public void tell(String aSentence) {
		tell(parser.parse(aSentence));
	}
	
	public void tell(Sentence aSentence) {

	}

	/**
	 * 
	 * @param aQuerySentence
	 * @return three possible return values exist. 1. 'null' indicates the query
	 *         returned false. 2. an empty list the query returned true. 3. a
	 *         list of substitutions, indicates true and the bindings for
	 *         different possible answers to the query (Note: refer to page
	 *         256).
	 */
	public List<Map<Variable, Term>> ask(String aQuerySentence) {
		return ask(parser.parse(aQuerySentence));
	}

	public List<Map<Variable, Term>> ask(Sentence aQuery) {
		return null;
	}
	
	//
	// PROTECTED METHODS
	//

	protected FOLParser getParser() {
		return parser;
	}

	protected Unifier createUnifier() {
		return new Unifier();
	}
	
	
	// TODO: Note pg 278, STORE(s) and FETCH(q).
}
