package aima.core.logic.propositional.algorithms;

import java.util.List;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.Connective;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */
public class LogicUtils {

	public static Sentence chainWith(Connective connective, List<Sentence> sentences) {
		if (sentences.size() == 0) {
			return null;
		} else if (sentences.size() == 1) {
			return sentences.get(0);
		} else {
			// Chaining is done righ associative, 
			// in the same way parsing works.
			Sentence soFar = sentences.get(sentences.size()-1);
			for (int i = sentences.size()-2; i >= 0; i--) {
				Sentence next = sentences.get(i);
				soFar = new ComplexSentence(connective, next, soFar);
			}
			return soFar;
		}
	}
}