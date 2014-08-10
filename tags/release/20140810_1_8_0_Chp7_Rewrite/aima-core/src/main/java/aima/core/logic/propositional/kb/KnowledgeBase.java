package aima.core.logic.propositional.kb;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.propositional.inference.TTEntails;
import aima.core.logic.propositional.parsing.PLParser;
import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class KnowledgeBase {
	private List<Sentence> sentences;

	private PLParser parser;

	public KnowledgeBase() {
		sentences = new ArrayList<Sentence>();
		parser = new PLParser();
	}

	/**
	 * Adds the specified sentence to the knowledge base.
	 * 
	 * @param aSentence
	 *            a fact to be added to the knowledge base.
	 */
	public void tell(String aSentence) {
		tell((Sentence) parser.parse(aSentence));
		
	}
	
	/**
	 * Adds the specified sentence to the knowledge base.
	 * 
	 * @param aSentence
	 *            a fact to be added to the knowledge base.
	 */
	public void tell(Sentence aSentence) {
		if (!(sentences.contains(aSentence))) {
			sentences.add(aSentence);
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
		return chainWith(Connective.AND, sentences);
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
		PLParser parser = new PLParser();

		Sentence alpha = parser.parse(queryString);

		return new TTEntails().ttEntails(this, alpha);
	}

	@Override
	public String toString() {
		if (sentences.size() == 0) {
			return "";
		} else {
			return asSentence().toString();
		}
	}

	/**
	 * Returns the list of sentences in the knowledge base.
	 * 
	 * @return the list of sentences in the knowledge base.
	 */
	public List<Sentence> getSentences() {
		return sentences;
	}

	public static Sentence chainWith(Connective connective,
			List<Sentence> sentences) {
		if (sentences.size() == 0) {
			return null;
		} else if (sentences.size() == 1) {
			return sentences.get(0);
		} else {
			// Chaining is done righ associative,
			// in the same way parsing works.
			Sentence soFar = sentences.get(sentences.size() - 1);
			for (int i = sentences.size() - 2; i >= 0; i--) {
				Sentence next = sentences.get(i);
				soFar = new ComplexSentence(connective, next, soFar);
			}
			return soFar;
		}
	}
}