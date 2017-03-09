package aima.core.nlp.parsing.grammars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A derivation rule that is contained within a grammar. This rule is probabilistic, in that it 
 * has an associated probability representing the likelihood that the RHS follows from the LHS, given 
 * the presence of the LHS.
 * @author Jonathon (thundergolfer)
 *
 */
public class Rule {
	
	public final float PROB;
	public final List<String> lhs; // Left hand side of derivation rule
	public final List<String> rhs; // Right hand side of derivation rule
	
	// Basic constructor
	public Rule( List<String> lhs, List<String> rhs, float probability ) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.PROB = validateProb( probability );
	}
	
	// null RHS rule constructor
	public Rule( List<String> lhs, float probability ) {
		this.lhs = lhs;
		this.rhs = null;
		this.PROB = validateProb( probability );
	}
	
	// string split constructor
	public Rule( String lhs, String rhs, float probability) {
		if( lhs.equals("")) {
			this.lhs = new ArrayList<String>();
		} else {
			this.lhs = new ArrayList<String>(Arrays.asList(lhs.split("\\s*,\\s*")));
		}
		if( rhs.equals("")) {
			this.rhs = new ArrayList<String>();
		} else {
			this.rhs = new ArrayList<String>(Arrays.asList(rhs.split("\\s*,\\s*")));
		}
		this.PROB = validateProb( probability );
		
	}
	
	/**
	 * Currently a hack to ensure rule has a valid probablity value.
	 * Don't really want to throw an exception.
	 */
	private float validateProb(float prob) {
		if( prob >= 0.0 && prob <= 1.0 ) {
			return prob;
		}
		else {
			return (float) 0.5; // probably should throw exception
		}
	}
	
	public boolean derives( List<String> sentForm ) {
		if( this.rhs.size() != sentForm.size() ) {
			return false;
		}
		for( int i=0; i < sentForm.size(); i++ ) {
			if( this.rhs.get(i) != sentForm.get(i)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean derives( String terminal ) {
		if( this.rhs.size() == 1 && this.rhs.get(0).equals(terminal)) {
			return true;
		}
		return false;
	}
	
	@Override 
	public String toString() {
		StringBuilder output = new StringBuilder();

		for (String lh : lhs) {
			output.append(lh);
		}

		output.append(" -> ");

		for (String rh : rhs) {
			output.append(rh);
		}

		output.append(" ").append(String.valueOf(PROB));

		return output.toString();
	}
}
