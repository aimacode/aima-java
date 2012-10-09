package aima.core.logic.propositional.algorithms;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.propositional.parsing.PEParser;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.visitors.CNFTransformer;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class KnowledgeBase {
	private List<Sentence> sentences;

	private PEParser parser;

	public KnowledgeBase() {
		sentences = new ArrayList<Sentence>();
		parser = new PEParser();
	}

	/**
	 * Adds the specified sentence to the knowledge base.
	 * 
	 * @param aSentence
	 *            a fact to be added to the knowledge base.
	 */
	public void tell(String aSentence) {
		Sentence sentence = (Sentence) parser.parse(aSentence);
		if (!(sentences.contains(sentence))) {
			sentences.add(sentence);
		}
	}

	/**
	 * Each time the agent program is called, it TELLS the knowledge base what
	 * it perceives.
	 * 
	 * @param percepts
	 *            what the agent perceives
	 */
	public void tellAll(String[] percepts) {
		for (int i = 0; i < percepts.length; i++) {
			tell(percepts[i]);
		}

	}

	/**
	 * Returns the number of sentences in the knowledge base.
	 * 
	 * @return the number of sentences in the knowledge base.
	 */
	public int size() {
		return sentences.size();
	}

	/**
	 * Returns the list of sentences in the knowledge base chained together as a
	 * single sentence.
	 * 
	 * @return the list of sentences in the knowledge base chained together as a
	 *         single sentence.
	 */
	public Sentence asSentence() {
		return LogicUtils.chainWith("AND", sentences);
	}

	/**
	 * Returns the answer to the specified question using the DPLL algorithm.
	 * 
	 * @param queryString
	 *            a question to ASK the knowledge base
	 * 
	 * @return the answer to the specified question using the DPLL algorithm.
	 */
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

	/**
	 * Returns the answer to the specified question using the TT-Entails
	 * algorithm.
	 * 
	 * @param queryString
	 *            a question to ASK the knowledge base
	 * 
	 * @return the answer to the specified question using the TT-Entails
	 *         algorithm.
	 */
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

	/**
	 * Returns the list of sentences in the knowledge base.
	 * 
	 * @return the list of sentences in the knowledge base.
	 */
	public List<Sentence> getSentences() {
		return sentences;
	}
}