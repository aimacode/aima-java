package aima.extra.logic.firstorder.parser;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.tree.ParseTree;

import aima.core.logic.basic.firstorder.Connectors;
import aima.core.logic.basic.firstorder.parsing.ast.ConnectedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Constant;
import aima.core.logic.basic.firstorder.parsing.ast.NotSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Predicate;
import aima.core.logic.basic.firstorder.parsing.ast.QuantifiedSentence;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.TermEquality;
import aima.core.logic.basic.firstorder.parsing.ast.FOLNode;
import aima.core.logic.basic.firstorder.parsing.ast.Function;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;
import aima.extra.logic.firstorder.parser.FirstOrderLogicParser;
import aima.extra.logic.firstorder.parser.FirstOrderLogicParser.SentenceContext;
import aima.extra.logic.firstorder.parser.FirstOrderLogicParser.TermContext;
import aima.extra.logic.firstorder.parser.FirstOrderLogicParser.VariableContext;

public class FirstOrderVisitor extends FirstOrderLogicBaseVisitor<FOLNode> {
	
	FirstOrderLogicParser parser;

	public FOLNode visit(ParseTree tree, FirstOrderLogicParser parser) {
		this.parser = parser;
		return visit(tree);
	}

	@Override
	public FOLNode visitParse(FirstOrderLogicParser.ParseContext ctx) { 
		Sentence sentence = null;
		boolean first = true;
		for ( SentenceContext s : ctx.sentence() ) {
			if ( first ) {
				sentence = (Sentence) visit(s);
				first = false;
			}
			else {
				sentence = new ConnectedSentence(Connectors.AND, sentence, (Sentence) visit(s));
			}
		}
		return sentence;
	}
	
	@Override
	public FOLNode visitSentence(FirstOrderLogicParser.SentenceContext ctx) {
		Sentence sentence = null;
		if ( ctx.bracketedsentence() != null ) {
			sentence = (Sentence) visitBracketedsentence(ctx.bracketedsentence());
		}
		else if ( ctx.atomicsentence() != null) {
			sentence = (Sentence) visitAtomicsentence(ctx.atomicsentence());
		}
		else if ( ctx.QUANTIFIER() == null ) {
			Sentence leftSentence;
			Sentence rightSentence;
			switch ( ctx.op.getText() ) {
				case "~":	sentence = new NotSentence((Sentence) visit(ctx.right));
							break;
				case "&": 	leftSentence = (Sentence) visit(ctx.left);
						    rightSentence = (Sentence) visit(ctx.right);
						    sentence = new ConnectedSentence(Connectors.AND, leftSentence, rightSentence);
						    break;
				case "|":   leftSentence = (Sentence) visit(ctx.left);
				  		    rightSentence = (Sentence) visit(ctx.right);
				  		    sentence = new ConnectedSentence(Connectors.OR, leftSentence, rightSentence);
				  		    break;
				case "=>":  leftSentence = (Sentence) visit(ctx.left);
				  		    rightSentence = (Sentence) visit(ctx.right);
				  		    sentence = new ConnectedSentence(Connectors.IMPLIES, leftSentence, rightSentence);
				  		    break;
				case "<=>": leftSentence = (Sentence) visit(ctx.left);
				  			rightSentence = (Sentence) visit(ctx.right);
				  			sentence = new ConnectedSentence(Connectors.BICOND, leftSentence, rightSentence);
				  			break;
			}
		}
		else {
			List<Variable> listOfVariables = new ArrayList<>();
			for ( VariableContext v: ctx.variable() ) {
				listOfVariables.add( (Variable) visit(v));
			}
			sentence = new QuantifiedSentence(ctx.QUANTIFIER().getText(), listOfVariables, (Sentence) visitSentence(ctx.right));
			
		}			
		return sentence; 
	}


	@Override
	public FOLNode visitBracketedsentence(FirstOrderLogicParser.BracketedsentenceContext ctx) {
		return this.visit(ctx.sentence());
	}

	@Override
	public FOLNode visitAtomicsentence(FirstOrderLogicParser.AtomicsentenceContext ctx) {
		Sentence sentence = null;
		if ( ctx.predicate() != null ) {
			Predicate predicate;
			List<Term> terms = new ArrayList<>();
			for (TermContext t : ctx.term() ) {
				terms.add((Term) visit(t));
			}
			parser.addPredicate(ctx.predicate().getText());
			predicate = new Predicate(ctx.predicate().getText(), terms);
			sentence = predicate;
		}
		else {
			TermEquality termEquality = new TermEquality( (Term) visitTerm(ctx.left), (Term) visitTerm(ctx.right) );
			sentence = termEquality;
		}
		return sentence;
	}

/*	@Override
	public FOLNode visitPredicate(FirstOrderLogicParser.PredicateContext ctx) {
		return new Predicate(ctx.getText(), null);
	}*/

	@Override
	public FOLNode visitTerm(FirstOrderLogicParser.TermContext ctx) {
		if ( ctx.function() != null ) {
			Function function;
			List<Term> terms = new ArrayList<>();
			for (TermContext t : ctx.term() ) {
				terms.add((Term) visit(t));
			}
			parser.addFunction(ctx.function().getText());
			function = new Function(ctx.function().getText(), terms);
			return function;
		}
		else if ( ctx.constant() != null ) {
			return visit(ctx.constant());
		}
		else if ( ctx.variable() != null ) {
			return visit(ctx.variable());
		}
		return null;
	}
	
/*	@Override
	public FOLNode visitFunction(FirstOrderLogicParser.FunctionContext ctx) {
		return new Function(ctx.getText(), null);
	}*/

	@Override
	public FOLNode visitConstant(FirstOrderLogicParser.ConstantContext ctx) {
		parser.addConstant(ctx.getText());
		return new Constant(ctx.getText());
	}

	@Override
	public FOLNode visitVariable(FirstOrderLogicParser.VariableContext ctx) {
		return new Variable(ctx.getText());
	}
}

