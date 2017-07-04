package aima.core.nlp.parsing.grammars;

import java.util.List;

public interface ProbabilisticGrammar {
	
	public boolean addRules(List<Rule> ruleList);
	
	public boolean addRule(Rule r);
	
	public boolean validRule(Rule r);
	
	public boolean validateRuleProbabilities(List<Rule> ruleList);
	
	public void updateVarsAndTerminals(Rule r);
	
	public void updateVarsAndTerminals();
}
