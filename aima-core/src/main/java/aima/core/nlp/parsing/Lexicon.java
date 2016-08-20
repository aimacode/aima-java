package aima.core.nlp.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

	public ArrayList<Rule> getTerminalRules( String partOfSpeech ) {
		ArrayList<LexWord> lexWords = this.get(partOfSpeech.toUpperCase());
		ArrayList<Rule> rules = new ArrayList<Rule>();
		if( lexWords.size() > 0) {
			for( int i=0; i < lexWords.size(); i++ ) {
				rules.add( new Rule( partOfSpeech.toUpperCase(), 
						   			    lexWords.get(i).word, 
						   			    lexWords.get(i).prob));
			}	
		}
		return rules;
	}
	
	public ArrayList<Rule> getAllTerminalRules() {
		ArrayList<Rule> allRules = new ArrayList<Rule>();
		Set<String> keys = this.keySet();
		Iterator<String> it = keys.iterator();
		while( it.hasNext() ) {
			String key = (String) it.next();
			allRules.addAll( this.getTerminalRules(key));
		}
		
		return allRules;
	}
	
	public boolean addEntry( String category, String word, float prob ) {
		if( this.containsKey(category)) {
			this.get(category).add( new LexWord( word, prob ));
		}
		else {
			this.put(category, new ArrayList<LexWord>( Arrays.asList(new LexWord(word,prob))));
		}
		
		return true;
	}
	
	public boolean addLexWords( String... vargs ) {
		
		String key; ArrayList<LexWord> lexWords = new ArrayList<LexWord>();
		boolean containsKey = false;
		// number of arguments must be key (1) + lexWord pairs ( x * 2 )
		if( vargs.length % 2 != 1 ) {
			return false;
		}
		key = vargs[0].toUpperCase();
		if( this.containsKey(key)) { containsKey = true; }
			
		for( int i=1; i < vargs.length; i++ ) {
			try {
				if( containsKey ) {
					this.get(key).add( new LexWord( vargs[i], Float.valueOf(vargs[i+1])));
				}
				else {
					lexWords.add( new LexWord( vargs[i], Float.valueOf(vargs[i+1])));	
				}
				i++;
			} catch( NumberFormatException e ) {
				System.err.println("Supplied args have incorrect format.");
				return false;
			}
		}
		if( !containsKey ) { this.put(key, lexWords); }
		return true;
		
	}
	
	/**
	 * Add words to an lexicon from an existing lexicon. Using this 
	 * you can combine lexicons.
	 * @param l
	 */
	public void addLexWords( Lexicon l ) {
		Iterator<Map.Entry<String,ArrayList<LexWord>>> it = l.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String,ArrayList<LexWord>> pair = it.next();
			if( this.containsKey( pair.getKey())) {
				for( int i=0; i < pair.getValue().size(); i++ ) {
					this.get(pair.getKey()).add(pair.getValue().get(i));
				}
			}
			else {
				this.put(pair.getKey(), pair.getValue());
			}
		}
	}
}


