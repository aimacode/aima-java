package aima.core.nlp.parsing.grammars;

import java.util.ArrayList;
import java.util.List;

public class ProbContextFreeGrammar extends ProbContextSensitiveGrammar implements ProbabilisticGrammar {
	
	// default constructor
	public ProbContextFreeGrammar() {
		type = 2; 
		rules = null;
	}
	
	/**
	 * Add a ruleList as the grammar's rule list if all rules in it pass
	 * both the restrictions of the parent grammars (unrestricted and context-sens)
	 * and this grammar's restrictions.
	 */
	public boolean addRules( List<Rule> ruleList ) {
		for( int i=0; i < ruleList.size(); i++ ) {
			if( !super.validRule(ruleList.get(i)) || !validRule(ruleList.get(i)) ) {
				return false;
			}
		}
		this.rules = ruleList;
		return true;
	}
	
	/**
	 * Add a rule to the grammar's rule list if it passes
	 * both the restrictions of the parent grammars (unrestricted and context-sens)
	 * and this grammar's restrictions.
	 */
	public boolean addRule( Rule r ) {
		if( !super.validRule(r) || !validRule(r) ) {
			return false;
		}
		rules.add(r);
		return true;
	}
	
	/**
	 * For a grammar rule to be valid in a context-free grammar, 
	 * all the restrictions of the parent grammars must hold, and the restriction
	 * of the context-free grammar must hold. The restriction is that the lhs must 
	 * consist of a single non-terminal (variable). There are no restrictions on the rhs
	 * 
	 */
	public boolean validRule( Rule r ){
		if( !super.validRule(r) ){
			return false;
		}
		// lhs must be a single non-terminal
		if( r.lhs.size() != 1 || !isVariable(r.lhs.get(0)))
		{
			return false;
		}
	
		return true;
	}
	
	/**
	 * Test whether LHS -> RHS is a rule in the grammar. 
	 * Note: it must be a DIRECT derivation
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	public boolean leftDerivesRight( ArrayList<String> lhs, ArrayList<String> rhs ) {
		
		// for each rule in the grammar 
		for( int i=0; i < rules.size(); i++ ) {
			Rule r = rules.get(i);
			if( r.lhs.equals(lhs) && r.rhs.equals(rhs)) {
				// matching rule found. left does derive the right in this grammar
				return true;
			}
		}
		// no match found
		return false;
	}


} // end of ContextFreeGrammar()
