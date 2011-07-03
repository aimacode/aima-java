package aima.core.logic.propositional.parsing;

import java.util.ArrayList;
import java.util.List;

import aima.core.logic.propositional.parsing.ast.BinarySentence;
import aima.core.logic.propositional.parsing.ast.FalseSentence;
import aima.core.logic.propositional.parsing.ast.MultiSentence;
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
		return new UnarySentence((Sentence) fs.getNegated().accept(this, arg));
	}

	public Object visitBinarySentence(BinarySentence fs, Object arg) {
		return new BinarySentence(fs.getOperator(), (Sentence) fs.getFirst()
				.accept(this, arg), (Sentence) fs.getSecond().accept(this, arg));
	}

	public Object visitMultiSentence(MultiSentence fs, Object arg) {
		List terms = fs.getSentences();
		List<Sentence> newTerms = new ArrayList<Sentence>();
		for (int i = 0; i < terms.size(); i++) {
			Sentence s = (Sentence) terms.get(i);
			Sentence subsTerm = (Sentence) s.accept(this, arg);
			newTerms.add(subsTerm);
		}
		return new MultiSentence(fs.getOperator(), newTerms);
	}

	protected Sentence recreate(Object ast) {
		return (Sentence) parser.parse(((Sentence) ast).toString());
	}
}