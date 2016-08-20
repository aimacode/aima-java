package aima.core.nlp.data.grammars;

import java.util.ArrayList;

import aima.core.nlp.data.lexicons.LexiconExamples;
import aima.core.nlp.parsing.Lexicon;
import aima.core.nlp.parsing.grammars.ProbCNFGrammar;
import aima.core.nlp.parsing.grammars.Rule;

/**
 * A store of example Probabilistic Chomsky-Normal-Form grammars for testing and 
 * demonstrating CYK.
 * @author Jonathon
 *
 */
public class ProbCNFGrammarExamples {
	
	/** 
	 * An elementary Chomsky-Normal-Form grammar for simple testing and 
	 * demonstrating. This type of grammar is seen more in Computing Theory classes,
	 * and does not mock a subset of English phrase-structure.
	 * @return
	 */
	public static ProbCNFGrammar buildExampleGrammarOne() {
		ProbCNFGrammar g = new ProbCNFGrammar();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		// Start Rules
		rules.add( new Rule( "S", "Y,Z", (float)0.10));
		rules.add( new Rule( "B", "B,D", (float)0.10));
		rules.add( new Rule( "B", "G,D", (float)0.10));
		rules.add( new Rule( "C", "E,C", (float)0.10));
		rules.add( new Rule( "C", "E,H", (float)0.10));
		rules.add( new Rule( "E", "M,N", (float)0.10));
		rules.add( new Rule( "D", "M,N", (float)0.10));
		rules.add( new Rule( "Y", "E,C", (float)0.10));
		rules.add( new Rule( "Z", "E,C", (float)0.10));
		
		// Terminal Rules
		rules.add( new Rule( "M", "m", (float)1.0));
		rules.add( new Rule( "N", "n", (float)1.0));
		rules.add( new Rule( "B", "a", (float)0.25));
		rules.add( new Rule( "B", "b", (float)0.25));
		rules.add( new Rule( "B", "c", (float)0.25));
		rules.add( new Rule( "B", "d", (float)0.25));
		rules.add( new Rule( "G", "a", (float)0.50));
		rules.add( new Rule( "G", "d", (float)0.50));
		rules.add( new Rule( "C", "x", (float)0.20));
		rules.add( new Rule( "C", "y", (float)0.20));
		rules.add( new Rule( "C", "z", (float)0.60));
		rules.add( new Rule( "H", "u", (float)0.50));
		rules.add( new Rule( "H", "z", (float)0.50));
		
		// Add all these rules into the grammar
		if(!g.addRules(rules)) {
			return null;
		}
		return g; 
	}
	
	/**
	 * A more restrictive phrase-structure grammar, used in testing and demonstrating 
	 * the CYK Algorithm. 
	 * Note: It is complemented by the "trivial lexicon" in LexiconExamples.java
	 * @return
	 */
	public static ProbCNFGrammar buildTrivialGrammar() {
		ProbCNFGrammar g = new ProbCNFGrammar();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		rules.add( new Rule( "S", "NP,VP", (float)1.0));
		rules.add( new Rule( "NP", "ARTICLE,NOUN", (float)0.50));
		rules.add( new Rule( "NP", "PRONOUN,ADVERB", (float)0.5));
		rules.add( new Rule( "VP", "VERB,NP", (float)1.0));
		// add terminal rules
		Lexicon trivLex = LexiconExamples.buildTrivialLexicon();
		ArrayList<Rule> terminalRules = new ArrayList<Rule>(trivLex.getAllTerminalRules());
		rules.addAll(terminalRules);
		// Add all these rules into the grammar
		if(!g.addRules(rules)) {
			return null;
		}
		return g;
	}
}
