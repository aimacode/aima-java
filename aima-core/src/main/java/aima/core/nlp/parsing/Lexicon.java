package aima.core.nlp.parsing;

import java.util.*;

import aima.core.nlp.parsing.grammars.Rule;

/**
 * The Lexicon Object appears on pg. 891 of the text and defines a simple
 * set of words for a certain language category and their associated probabilities.
 * 
 * Defining and using a lexicon saves us from listing out a large number of rules to
 * derive terminal strings in a grammar.
 * 
 * @author Jonathon
 *
 */
public class Lexicon extends HashMap<String,ArrayList<LexWord>> {

	private static final long serialVersionUID = 1L;

	public ArrayList<Rule> getTerminalRules(String partOfSpeech) {
		final String partOfSpeechUpperCase = partOfSpeech.toUpperCase();
		final ArrayList<Rule> rules = new ArrayList<>();

		Optional.ofNullable(this.get(partOfSpeechUpperCase)).ifPresent(lexWords -> {
			for (LexWord word : lexWords)
				rules.add(new Rule(partOfSpeechUpperCase, word.word, word.prob));
		});

		return rules;
	}
	
	public ArrayList<Rule> getAllTerminalRules() {
		final ArrayList<Rule> allRules = new ArrayList<>();
		final Set<String> keys = this.keySet();

		for (String key : keys)
			allRules.addAll( this.getTerminalRules(key));

		return allRules;
	}
	
	public boolean addEntry( String category, String word, float prob ) {
		if( this.containsKey(category))
			this.get(category).add( new LexWord( word, prob ));
		else
			this.put(category, new ArrayList<>(Collections.singletonList(new LexWord(word,prob))));
		
		return true;
	}
	
	public boolean addLexWords(String... vargs) {
		ArrayList<LexWord> lexWords = new ArrayList<>();
		boolean containsKey = false;
		// number of arguments must be key (1) + lexWord pairs ( x * 2 )
		if (vargs.length % 2 != 1)
			return false;

		String key = vargs[0].toUpperCase();
		if (this.containsKey(key)) { containsKey = true; }
			
		for (int i=1; i < vargs.length; i++) {
			try {
				if( containsKey )
					this.get(key).add( new LexWord( vargs[i], Float.valueOf(vargs[i+1])));
				else
					lexWords.add( new LexWord( vargs[i], Float.valueOf(vargs[i+1])));
				i++;
			} catch( NumberFormatException e ) {
				System.err.println("Supplied args have incorrect format.");
				return false;
			}
		}
		if (!containsKey) { this.put(key, lexWords); }
		return true;
		
	}
	
	/**
	 * Add words to an lexicon from an existing lexicon. Using this 
	 * you can combine lexicons.
	 * @param lexicon
	 */
	public void addLexWords(Lexicon lexicon) {
		for (Map.Entry<String, ArrayList<LexWord>> pair : lexicon.entrySet()) {
			final String key = pair.getKey();
			final ArrayList<LexWord> lexWords = pair.getValue();

			if (this.containsKey(key)) {
				for (LexWord word : lexWords)
					this.get(key).add(word);
			} else {
				this.put(key, lexWords);
			}
		}
	}
}


