package aima.logic.fol;

import java.util.ArrayList;
import java.util.List;

import aima.logic.fol.kb.data.DefiniteClause;
import aima.logic.fol.parsing.AbstractFOLVisitor;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.ConnectedSentence;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Function;
import aima.logic.fol.parsing.ast.NotSentence;
import aima.logic.fol.parsing.ast.ParanthizedSentence;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.QuantifiedSentence;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.TermEquality;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ciaran O'Reilly
 * 
 */
// TODO - implement this more cleanly when create a CNF Visitor!!!!
public class DefiniteClauseVisitor extends AbstractFOLVisitor {

	private boolean knownToBeInvalid = false;
	private int noNegated = 0;
	private int noAnds = 0;
	private int noOrs = 0;
	private int noImplications = 0;
	private List<Predicate> premises = new ArrayList<Predicate>();
	private Predicate conclusion = null;
	private boolean nextPredicateIsConclusion = false;
	
	public DefiniteClauseVisitor(FOLParser parser) {
		super(parser);
	}

	/**
	 * 
	 * @param aSentence
	 *            an FOL sentence from which a DefiniteClause is to be
	 *            extracted.
	 * @return a DefiniteClause is the Sentence represents one, otherwise null.
	 */
	public synchronized DefiniteClause definiteClause(Sentence aSentence) {
		DefiniteClause dc = null;
		
		// Initialize variables before constructing
		knownToBeInvalid = false;
		noNegated = 0;
		noAnds = 0;
		noOrs = 0;
		noImplications = 0;
		premises.clear();
		conclusion = null;
		nextPredicateIsConclusion = false;

		aSentence.accept(this, null);
		
		if (!knownToBeInvalid) {
			// If an implication
			if (noImplications > 0) {
				if (noImplications == 1 &&
					noAnds == (premises.size()-1) &&
					noOrs == 0 &&
					noNegated == 0) {
					dc = new DefiniteClause(premises, conclusion);
				}
			// If a fact
			} else if (null == conclusion && 1 == premises.size()
					&& noAnds == 0 && noOrs == 0 && noNegated == 0) {
				dc = new DefiniteClause(new ArrayList<Predicate>(), premises
						.get(0));
				// If a Clause with 1 positive literal
			} else if (null != conclusion && noNegated == premises.size()
					&& noOrs == premises.size() && noAnds == 0) {
				dc = new DefiniteClause(premises, conclusion);
			}
		}
		
		return dc;
	}
	
	public Object visitPredicate(Predicate p, Object arg) {
		if (nextPredicateIsConclusion) {
			conclusion = p;
			nextPredicateIsConclusion = false;
		} else {
			premises.add(p);
		}

		return arg;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		knownToBeInvalid = true;
		return arg;
	}

	public Object visitVariable(Variable variable, Object arg) {
		return arg;
	}

	public Object visitConstant(Constant constant, Object arg) {
		return arg;
	}

	public Object visitFunction(Function function, Object arg) {
		return arg;
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		noNegated++;
		nextPredicateIsConclusion = true;
		sentence.getNegated().accept(this, null);

		return null;
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		String connector = sentence.getConnector();
		if (Connectors.isAND(connector)) {
			noAnds++;
			sentence.getFirst().accept(this, null);
			sentence.getSecond().accept(this, null);
		} else if (Connectors.isBICOND(connector)) {
			// TODO : Correct?
			knownToBeInvalid = true;
		} else if (Connectors.isIMPLIES(connector)) {
			noImplications++;
			sentence.getFirst().accept(this, null);
			nextPredicateIsConclusion = true;
			sentence.getSecond().accept(this, null);
		} else if (Connectors.isOR(connector)) {
			noOrs++;
			sentence.getFirst().accept(this, null);
			sentence.getSecond().accept(this, null);
		}
		
		return arg;
	}

	public Object visitParanthizedSentence(ParanthizedSentence sentence,
			Object arg) {

		sentence.getParanthized().accept(this, null);

		return arg;
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {

		// TODO: Don't support quantified at the moment!
		// When implement FOL CNF this will be simpler

		knownToBeInvalid = true;

		return arg;
	}
}