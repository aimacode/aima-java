package aima.logic.fol;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.ConnectedSentence;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */

public class DLKnowledgeBase {
	private FOLParser parser;

	private List<Rule> rules;

	List<Fact> facts;

	private Unifier unifier;

	public DLKnowledgeBase(FOLDomain domain) {
		parser = new FOLParser(domain);
		rules = new ArrayList<Rule>();
		facts = new ArrayList<Fact>();
		unifier = new Unifier(parser);
	}

	public List getRules() {
		return rules;
	}

	public void add(String sentenceString) {
		Sentence sentence = parser.parse(sentenceString);
		if (isRule(sentence)) {
			rules.add(new Rule(sentence, parser));
		} else if (isFact(sentence)) {
			facts.add(new Fact(sentence));
		}
	}

	private boolean isRule(Sentence s) {
		try {
			ConnectedSentence sentence = (ConnectedSentence) s;
			return sentence.getConnector().equals(Connectors.IMPLIES);
		} catch (ClassCastException e) {
			return false;
		}
	}

	private boolean isFact(Sentence s) {
		try {
			Predicate sentence = (Predicate) s;
			return true;
		} catch (ClassCastException e) {
			return false;
		}
	}

	public int numRules() {
		return rules.size();
	}

	public int numFacts() {
		return facts.size();
	}

	public Rule rule(int i) {
		return rules.get(i);
	}

	public Fact fact(int i) {
		return facts.get(i);
	}

	public Properties forwardChain(String query) {
		parser.setUpToParse(query);
		Predicate p = (Predicate) parser.parsePredicate();
		return forwardChain(p);
	}

	public Properties forwardChain(Predicate query) {

		Properties p = new Properties();
		int numberOfNewFactsDiscoveredThisIteration = 0;
		do {
			Hashtable h = matchesFacts(query);
			// System.out.println("MatchedFacts " + h);
			if (h != null) {
				p = new Properties();
				Iterator iter = h.keySet().iterator();
				while (iter.hasNext()) {
					Object key = iter.next();
					String value = h.get(key).toString();
					p.setProperty(key.toString(), value);
				}
				return p;
			} else {
				numberOfNewFactsDiscoveredThisIteration = 0;
				for (int i = 0; i < rules.size(); i++) {
					Rule rule = rules.get(i);

					rule.initializeAllClauseDomainsFrom(facts());
					if (rule.triggerable()) {

						Fact substFact = new Fact(trigger(rule));
						if (!facts.contains(substFact)) {
							add(substFact.toString());
							numberOfNewFactsDiscoveredThisIteration++;
						}
					}

				}
			}
		} while (numberOfNewFactsDiscoveredThisIteration > 0);

		return p;
	}

	public Predicate trigger(Rule triggered) {
		Predicate fact = triggered.conclusion();
		Predicate substFact = null;
		List variables = new VariableCollector(parser)
				.getAllVariableNames(fact);
		if (variables.size() == 0) {// conclusion with no variables
			return fact;
		}
		for (int i = 0; i < variables.size(); i++) {
			String variable = (String) variables.get(0);
			List commonValues = triggered
					.commonValuesInContainingClauses(variable);
			// for now assume only one common value
			Properties substValues = new Properties();
			substValues.setProperty(variable, commonValues.get(0).toString());
			substFact = (Predicate) new SubstVisitor(parser)
					.getSubstitutedSentence(fact, substValues);

		}
		return substFact;

	}

	private Hashtable matchesFacts(Predicate query) {
		Hashtable unificationResult = null;
		for (int i = 0; i < facts.size(); i++) {
			Fact fact = facts.get(i);
			unificationResult = unifier.unify(query, fact.predicate(),
					new Hashtable());
			if (unificationResult != null) {
				return unificationResult;
			}
		}
		return unificationResult;
	}

	public List facts() {
		return facts;
	}

}