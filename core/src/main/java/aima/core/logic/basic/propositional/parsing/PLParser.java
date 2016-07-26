package aima.core.logic.basic.propositional.parsing;

import aima.core.logic.basic.propositional.parsing.ast.Sentence;

public interface PLParser {
	public Sentence parse(String stringToBeParsed);
}