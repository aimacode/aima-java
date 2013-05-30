package aima.core.logic.propositional.parsing;

import aima.core.logic.propositional.Connective;
import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.logic.propositional.parsing.ast.Symbol;
import aima.core.logic.propositional.parsing.ast.TrueSentence;
import aima.core.logic.propositional.parsing.ast.UnarySentence;

/**
 * @author Ravi Mohan
 * 
 */
public class AbstractPLVisitor implements PLVisitor {
	private PEParser parser = new PEParser();

	public Object visitSymbol(Symbol s, Object arg) {
		return new Symbol(s.getValue());
	}

	public Object visitTrueSentence(TrueSentence ts, Object arg) {
		return new TrueSentence();
	}

	public Object visitFalseSentence(FalseSentence fs, Object arg) {
		return new FalseSentence();
	}

	public Object visitNotSentence(UnarySentence fs, Object arg) {
		return new UnarySentence(Connective.NOT, (Sentence) fs.getFirst().accept(this, arg));
	}

	public Object visitBinarySentence(BinarySentence fs, Object arg) {
		return new BinarySentence(fs.getConnective(), (Sentence) fs.getFirst()
				.accept(this, arg), (Sentence) fs.getSecond().accept(this, arg));
	}

	protected Sentence recreate(Object ast) {
		return (Sentence) parser.parse(((Sentence) ast).toString());
	}
}