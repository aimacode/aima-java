package aima.core.logic.basic.propositional.kb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.logic.api.propositional.KnowledgeBase;
import aima.core.logic.basic.propositional.kb.data.Clause;
import aima.core.logic.basic.propositional.kb.data.ConjunctionOfClauses;
import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.core.logic.basic.propositional.visitors.ConvertToConjunctionOfClauses;
import aima.core.logic.basic.propositional.visitors.SymbolCollector;
import aima.core.logic.basic.propositional.inference.TTEntails;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Anurag Rai
 * 
 */
public class BasicKnowledgeBase implements KnowledgeBase {
	private List<Sentence>         sentences = new ArrayList<Sentence>();
	private ConjunctionOfClauses   asCNF     = new ConjunctionOfClauses(Collections.<Clause>emptySet());
	private Set<PropositionSymbol> symbols   = new LinkedHashSet<PropositionSymbol>();
	private PLParser               parser    = new PLParser();

	public BasicKnowledgeBase() {
	}

	/**
	 * Adds the specified sentence to the knowledge base.
	 * 
	 * @param aSentence
	 *            a fact to be added to the knowledge base.
	 */
	@Override
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
			asCNF = asCNF.extend(ConvertToConjunctionOfClauses.convert(aSentence).getClauses());
			symbols.addAll(SymbolCollector.getSymbolsFrom(aSentence));
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
		Arrays.stream(percepts).forEach( percept -> tell(percept) );
	}

	/**
	 * Returns the list of sentences in the knowledge base chained together as a
	 * single sentence.
	 * 
	 * @return the list of sentences in the knowledge base chained together as a
	 *         single sentence.
	 */
	public Sentence asSentence() {
		return Sentence.newConjunction(sentences);
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


	@Override
	public List<Sentence> getSentences() {
		return sentences;
	}
}