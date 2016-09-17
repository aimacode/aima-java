package aima.core.nlp.data.grammars;

import java.util.ArrayList;

import aima.core.nlp.data.lexicons.LexiconExamples;
import aima.core.nlp.parsing.Lexicon;
import aima.core.nlp.parsing.grammars.ProbContextFreeGrammar;
import aima.core.nlp.parsing.grammars.Rule;

public class ProbContextFreeExamples {

	public static ProbContextFreeGrammar buildWumpusGrammar() {
		ProbContextFreeGrammar g = new ProbContextFreeGrammar();
		ArrayList<Rule> rules = new ArrayList<Rule>();
		// Start Rules
		rules.add( new Rule( "S", "NP,VP", (float)0.90));
		rules.add( new Rule( "S", "CONJ,S", (float)0.10));
		// Noun Phrase Rules
		rules.add( new Rule( "NP", "PRONOUN", (float)0.30));
		rules.add( new Rule( "NP", "NAME" , (float)0.10));
		rules.add( new Rule( "NP", "NOUN" , (float)0.10));
		rules.add( new Rule( "NP", "ARTICLE,NOUN", (float)0.25));
		rules.add( new Rule( "NP", "AP,NOUN", (float)0.05));
		rules.add( new Rule( "NP", "DIGIT,DIGIT", (float)0.05));
		rules.add( new Rule( "NP", "NP,PP", (float)0.10));
		rules.add( new Rule( "NP", "NP,RELCLAUSE", (float)0.05));
		// add verb phrase rules
		rules.add( new Rule( "VP", "VERB", (float)0.40));
		rules.add( new Rule( "VP", "VP,NP", (float)0.35));
		rules.add( new Rule( "VP", "VP,ADJS", (float)0.05));
		rules.add( new Rule( "VP", "VP,PP", (float)0.10));
		rules.add( new Rule( "VP", "VP,ADVERB", (float)0.10));
		// add adjective rules
		rules.add( new Rule( "AJD", "AJDS", (float)0.80));
		rules.add( new Rule( "AJD", "AJD,AJDS", (float)0.20));
		// add Article Phrase
		// This deviates from the text because the text provides the rule:
		// NP -> Article Adjs Noun, which is NOT in Chomsky Normal Form
		//
		// We instead define AP (Article Phrase) AP -> Article Adjs, to get around this
		rules.add( new Rule( "AP", "ARTICLE,ADJS", (float)1.0));
		// add preposition phrase
		rules.add( new Rule( "PP", "PREP,NP", (float)1.00));
		// add relative clause
		rules.add( new Rule( "RELCLAUSE", "RELPRO,VP", (float)1.00));
		
		// Now we can add all rules that derive terminal symbols, which are in 
		// this case words.
		Lexicon wumpusLex = LexiconExamples.buildWumpusLex();
		ArrayList<Rule> terminalRules = new ArrayList<Rule>(wumpusLex.getAllTerminalRules());
		rules.addAll( terminalRules );
		// Add all these rules into the grammar
		if(!g.addRules(rules)) {
			return null;
		}
		return g; 
	}
}
