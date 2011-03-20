package aima.core.logic.propositional.algorithms;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.CNFTransformer;

/**
 * @author Ravi Mohan
 * 
 */
public class KnowledgeBase {
	private List<Sentence> sentences;

	private PEParser parser;

	public KnowledgeBase() {
		sentences = new ArrayList<Sentence>();
		parser = new PEParser();
	}

	public void tell(String aSentence) {
		Sentence sentence = (Sentence) parser.parse(aSentence);
		if (!(sentences.contains(sentence))) {
			sentences.add(sentence);
		}
	}

	public void tellAll(String[] percepts) {
		for (int i = 0; i < percepts.length; i++) {
			tell(percepts[i]);
		}

	}

	public int size() {
		return sentences.size();
	}

	public Sentence asSentence() {
		return LogicUtils.chainWith("AND", sentences);
	}

	public boolean askWithDpll(String queryString) {
		Sentence query = null, cnfForm = null;
		try {
			// just a check to see that the query is well formed
			query = (Sentence) parser.parse(queryString);
		} catch (Exception e) {
			System.out.println("error parsing query" + e.getMessage());
		}

		Sentence kbSentence = asSentence();
		Sentence kbPlusQuery = null;
		if (kbSentence != null) {
			kbPlusQuery = (Sentence) parser.parse(" ( " + kbSentence.toString()
					+ " AND (NOT " + queryString + " ))");
		} else {
			kbPlusQuery = query;
		}
		try {
			cnfForm = new CNFTransformer().transform(kbPlusQuery);
			// System.out.println(cnfForm.toString());
		} catch (Exception e) {
			System.out.println("error converting kb +  query to CNF"
					+ e.getMessage());

		}
		 return !new DPLL().dpllSatisfiable(cnfForm); 
	}

	public boolean askWithTTEntails(String queryString) {

		return new TTEntails().ttEntails(this, queryString);
	}

	@Override
	public String toString() {
		if (sentences.size() == 0) {
			return "";
		} else
			return asSentence().toString();
	}

	public List getSentences() {
		return sentences;
	}
}