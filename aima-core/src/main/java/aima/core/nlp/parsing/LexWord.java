package aima.core.nlp.parsing;

public class LexWord {
	String word;
	Float  prob;
	
	public LexWord( String word, Float prob ) {
		this.word = word;
		this.prob = prob;
	}
	
	public String getWord() { return word; }
	public Float getProb() { return prob; }
}
