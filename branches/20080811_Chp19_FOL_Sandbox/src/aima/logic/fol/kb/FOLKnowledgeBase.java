package aima.logic.fol.kb;

import aima.logic.fol.FOLDomain;
import aima.logic.fol.Unifier;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Sentence;

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
	
	public Object ask(String aSentence) {
		return ask(parser.parse(aSentence));
	}

	// TODO - determine a good return
	// true or false, or a substitution or binding list, more than 1 answer a
	// list of substitutions returned.
	// Note: pg. 254
	public Object ask(Sentence aQuery) {
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
