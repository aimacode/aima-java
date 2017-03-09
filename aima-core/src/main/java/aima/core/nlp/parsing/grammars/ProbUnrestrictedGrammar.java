package aima.core.nlp.parsing.grammars;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the most general grammatical formalism,
 * the Unrestricted (or Recrusively Enumerable) Grammar.
 * All other grammars can derive from this grammar, imposing extra
 * restrictions.
 * @author Jonathon
 *
 */
public class ProbUnrestrictedGrammar implements ProbabilisticGrammar {

	// types of grammars
	public static final int UNRESTRICTED = 0;
	public static final int CONTEXT_SENSITIVE = 1;
	public static final int	CONTEXT_FREE = 2;
	public static final int REGULAR = 3;
	public static final int CNFGRAMMAR = 4;
	public static final int PROB_CONTEXT_FREE = 5;
	
	public List<Rule> rules;
	public List<String> vars;
	public List<String> terminals;
	public int type; 
	
	// default constructor. has no rules
	public ProbUnrestrictedGrammar() {
		type = 0;
		rules = new ArrayList<Rule>();
		vars =  new ArrayList<String>();
		terminals = new ArrayList<String>();
	}
	
	/**
	 * Add a number of rules at once, testing each in turn
	 * for validity, and then testing the batch for probability validity.
	 * @param ruleList
	 * @return true if rules are valid and incorporated into the grammar. false, otherwise
	 */
	public boolean addRules( List<Rule> ruleList ) {
		for( int i=0; i < ruleList.size(); i++ ) {
			if( !validRule(ruleList.get(i)) ) {
				return false;
			}
		}
		if( !validateRuleProbabilities(ruleList)) {
			return false;
		}
		this.rules = ruleList;
		updateVarsAndTerminals();
		return true;
	}
	
	/**
	 * Add a single rule the grammar, testing it for structural 
	 * and probability validity.
	 * @param rule
	 * @return true if rule is incorporated. false, otherwise
	 */
	// TODO: More sophisticated probability distribution management
	public boolean addRule( Rule rule ) {
		if( validRule(rule)) {
			rules.add(rule);
			updateVarsAndTerminals( rule );
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * For a set of rules, test whether each batch of rules with the same 
	 * LHS have their probabilities sum to exactly 1.0
	 * @param ruleList
	 * @return true if the probabilities are valid. false, otherwise
	 */
	public boolean validateRuleProbabilities( List<Rule> ruleList ) {
		float probTotal = 0;
		for( int i=0; i < vars.size(); i++ ) {
			for( int j=0; j < ruleList.size(); j++ ) {
				// reset probTotal at start
				if( j == 0 ) {
					probTotal = (float) 0.0;
				}
				if( ruleList.get(i).lhs.get(0).equals(vars.get(i))) {
					probTotal += ruleList.get(i).PROB;
				}
				// check probTotal hasn't exceed max
				if( probTotal > 1.0 ) {
					return false;
				}
				// check we have correct probability total
				if( j == ruleList.size() -1 && probTotal != (float) 1.0 ) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Test validity of the LHS and RHS of grammar rule.
	 * In unrestricted grammar, the only invalid rule type is
	 * a rule with a null LHS.
	 * @param r ( a rule )
	 * @return true, if rule has valid form. false, otherwise
	 */
	public boolean validRule( Rule r ) {
		if( r.lhs != null && r.lhs.size() > 0 ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/** 
	 * Whenever a new rule is added to the grammar, we want to 
	 * update the list of variables and terminals with any new grammar symbols
	 */
	public void updateVarsAndTerminals() {
		if( rules == null ) {
			vars =  new ArrayList<String>();
			terminals = new ArrayList<String>();
			return;
		}
		for( int i=0; i < rules.size(); i++ ) {
			Rule r = rules.get(i);
			updateVarsAndTerminals(r);	// update the variables and terminals for this rule
		}
	}
	
	/**
	 * Update variable and terminal lists with a single rule's symbols,
	 * if there a new symbols
	 * @param r
	 */
	public void updateVarsAndTerminals( Rule r ) {
		// check lhs for new terminals or variables
		for( int j=0; j < r.lhs.size(); j++ ) {
			if( isVariable(r.lhs.get(j)) && !vars.contains(r.lhs.get(j))) {
				vars.add(r.lhs.get(j));
			}
			else if( isTerminal(r.lhs.get(j)) && !terminals.contains(r.lhs.get(j))) {
				terminals.add(r.lhs.get(j));
			}
		}
		// for rhs we must check that this isn't a null-rule
		if ( r.rhs != null ) {
			// check rhs for new terminals or variables
			for( int j=0; j < r.rhs.size(); j++ ) {
				if( isVariable(r.rhs.get(j)) && !vars.contains(r.rhs.get(j))) {
					vars.add(r.rhs.get(j));
				}
				else if( isTerminal(r.rhs.get(j)) && !terminals.contains(r.rhs.get(j))) {
					terminals.add(r.rhs.get(j));
				}
			}
		}
		// maintain sorted lists
		Collections.sort(vars);
		Collections.sort(terminals);
	}
	
	
	/**
	 * Check if we have a variable, as they are uppercase strings.
	 * @param s
	 * @return
	 */
	public static boolean isVariable(String s) {
		for (int i=0; i < s.length(); i++)
		{
			if (!Character.isUpperCase(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/** 
	 * Check if we have a terminal, as they are lowercase strings
	 * @param s
	 * @return true, if string must be a terminal. false, otherwise
	 */
	public static boolean isTerminal(String s) {
		for (int i=0; i < s.length(); i++ ) {
			
			if( !Character.isLowerCase(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();

		output.append("Variables:  ");

		this.vars.forEach(var -> output.append(var).append(", "));

		output.append('\n');
		output.append("Terminals:  ");

		this.terminals.forEach(terminal -> output.append(terminal).append(", "));

		output.append('\n');

		this.rules.forEach(rule -> output.append(rule.toString()).append('\n'));

		return output.toString();
	}
}
