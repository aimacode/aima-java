package aima.core.logic.propositional.algorithms;

import java.util.List;

import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */
public class LogicUtils {

	public static Sentence chainWith(String connector, List sentences) {
		if (sentences.size() == 0) {
			return null;
		} else if (sentences.size() == 1) {
			return (Sentence) sentences.get(0);
		} else {
			Sentence soFar = (Sentence) sentences.get(0);
			for (int i = 1; i < sentences.size(); i++) {
				Sentence next = (Sentence) sentences.get(i);
				soFar = new BinarySentence(connector, soFar, next);
			}
			return soFar;
		}
	}
}