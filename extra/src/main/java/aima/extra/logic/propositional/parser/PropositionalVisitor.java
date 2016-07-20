package aima.extra.logic.propositional.parser;

import aima.core.logic.basic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.basic.propositional.parsing.ast.Connective;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;
import aima.extra.logic.propositional.parser.PropositionalLogicBaseVisitor;
import aima.extra.logic.propositional.parser.PropositionalLogicParser;
import aima.extra.logic.propositional.parser.PropositionalLogicParser.SentenceContext;

public class PropositionalVisitor extends PropositionalLogicBaseVisitor<Sentence> {
	
	@Override
	public Sentence visitParse(PropositionalLogicParser.ParseContext ctx) { 
		Sentence sentence = null;
		boolean first = true;
		for ( SentenceContext s : ctx.sentence() ) {
			if ( first ) {
				sentence = visit(s);
				first = false;
			}
			else {
				sentence = new ComplexSentence(Connective.AND, sentence, visit(s));
			}
		}
		return sentence;
	}
	
	@Override
	public Sentence visitSentence(PropositionalLogicParser.SentenceContext ctx) {
		Sentence sentence = null;
		if ( ctx.bracketedsentence() != null ) {
			sentence = visitBracketedsentence(ctx.bracketedsentence());
		}
		else if ( ctx.atomicsentence() != null) {
			sentence = visitAtomicsentence(ctx.atomicsentence());
		}
		else if ( ctx.op.getText().equals("~") ) {
			Sentence rightSentence = visit(ctx.right);
			sentence = new ComplexSentence(Connective.NOT, rightSentence);
		}
		else {
			Sentence leftSentence;
			Sentence rightSentence;
			switch ( ctx.op.getText() ) {
				case "&": 	leftSentence = visit(ctx.left);
						    rightSentence = visit(ctx.right);
						    sentence = new ComplexSentence(Connective.AND, leftSentence, rightSentence);
						    break;
				case "|":   leftSentence = visit(ctx.left);
				  		    rightSentence = visit(ctx.right);
				  		    sentence = new ComplexSentence(Connective.OR, leftSentence, rightSentence);
				  		    break;
				case "=>":  leftSentence = visit(ctx.left);
				  		    rightSentence = visit(ctx.right);
				  		    sentence = new ComplexSentence(Connective.IMPLICATION, leftSentence, rightSentence);
				  		    break;
				case "<=>": leftSentence = visit(ctx.left);
				  			rightSentence = visit(ctx.right);
				  			sentence = new ComplexSentence(Connective.BICONDITIONAL, leftSentence, rightSentence);
				  			break;
			}
		}
		return sentence; 
	}

	@Override
	public Sentence visitBracketedsentence(PropositionalLogicParser.BracketedsentenceContext ctx) {
		return this.visit(ctx.sentence());
	}

	@Override
	public Sentence visitAtomicsentence(PropositionalLogicParser.AtomicsentenceContext ctx) {
		return new PropositionSymbol(ctx.getText());
	}
}
