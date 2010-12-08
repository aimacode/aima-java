package aima.core.logic.fol;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.FOLVisitor;
import aima.core.logic.fol.parsing.ast.ConnectedSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Function;
import aima.core.logic.fol.parsing.ast.NotSentence;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.QuantifiedSentence;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 345.
 * 
 * Every sentence of first-order logic can be converted into an inferentially
 * equivalent CNF sentence.
 * 
 * Note: Transformation rules extracted from 346 and 347, which
 * are essentially the INSEADO method outlined in:
 * http://logic.stanford.edu/classes/cs157/2008/lectures/lecture09.pdf
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CNFConverter {

	private FOLParser parser = null;
	private SubstVisitor substVisitor;

	public CNFConverter(FOLParser parser) {
		this.parser = parser;

		this.substVisitor = new SubstVisitor();
	}

	public CNF convertToCNF(Sentence aSentence) {
		// I)mplications Out:
		Sentence implicationsOut = (Sentence) aSentence.accept(
				new ImplicationsOut(), null);

		// N)egations In:
		Sentence negationsIn = (Sentence) implicationsOut.accept(
				new NegationsIn(), null);

		// S)tandardize variables:
		// For sentences like:
		// (FORALL x P(x)) V (EXISTS x Q(x)),
		// which use the same variable name twice, change the name of one of the
		// variables.
		Sentence saQuantifiers = (Sentence) negationsIn.accept(
				new StandardizeQuantiferVariables(substVisitor),
				new LinkedHashSet<Variable>());

		// Remove explicit quantifiers, by skolemizing existentials
		// and dropping universals:
		// E)xistentials Out
		// A)lls Out:
		Sentence andsAndOrs = (Sentence) saQuantifiers.accept(
				new RemoveQuantifiers(parser), new LinkedHashSet<Variable>());

		// D)istribution
		// V over ^:
		Sentence orDistributedOverAnd = (Sentence) andsAndOrs.accept(
				new DistributeOrOverAnd(), null);

		// O)perators Out
		return (new CNFConstructor()).construct(orDistributedOverAnd);
	}
}

class ImplicationsOut implements FOLVisitor {
	public ImplicationsOut() {

	}

	public Object visitPredicate(Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(Variable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(Constant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(Function function, Object arg) {
		return function;
	}

	public Object visitNotSentence(NotSentence notSentence, Object arg) {
		Sentence negated = notSentence.getNegated();

		return new NotSentence((Sentence) negated.accept(this, arg));
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		Sentence alpha = (Sentence) sentence.getFirst().accept(this, arg);
		Sentence beta = (Sentence) sentence.getSecond().accept(this, arg);

		// Eliminate <=>, bi-conditional elimination,
		// replace (alpha <=> beta) with (~alpha V beta) ^ (alpha V ~beta).
		if (Connectors.isBICOND(sentence.getConnector())) {
			Sentence first = new ConnectedSentence(Connectors.OR,
					new NotSentence(alpha), beta);
			Sentence second = new ConnectedSentence(Connectors.OR, alpha,
					new NotSentence(beta));

			return new ConnectedSentence(Connectors.AND, first, second);
		}

		// Eliminate =>, implication elimination,
		// replacing (alpha => beta) with (~alpha V beta)
		if (Connectors.isIMPLIES(sentence.getConnector())) {
			return new ConnectedSentence(Connectors.OR, new NotSentence(alpha),
					beta);
		}

		return new ConnectedSentence(sentence.getConnector(), alpha, beta);
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {

		return new QuantifiedSentence(sentence.getQuantifier(), sentence
				.getVariables(), (Sentence) sentence.getQuantified().accept(
				this, arg));
	}
}

class NegationsIn implements FOLVisitor {
	public NegationsIn() {

	}

	public Object visitPredicate(Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(Variable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(Constant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(Function function, Object arg) {
		return function;
	}

	public Object visitNotSentence(NotSentence notSentence, Object arg) {
		// CNF requires NOT (~) to appear only in literals, so we 'move ~
		// inwards' by repeated application of the following equivalences:
		Sentence negated = notSentence.getNegated();

		// ~(~alpha) equivalent to alpha (double negation elimination)
		if (negated instanceof NotSentence) {
			return ((NotSentence) negated).getNegated().accept(this, arg);
		}

		if (negated instanceof ConnectedSentence) {
			ConnectedSentence negConnected = (ConnectedSentence) negated;
			Sentence alpha = negConnected.getFirst();
			Sentence beta = negConnected.getSecond();
			// ~(alpha ^ beta) equivalent to (~alpha V ~beta) (De Morgan)
			if (Connectors.isAND(negConnected.getConnector())) {
				// I need to ensure the ~s are moved in deeper
				Sentence notAlpha = (Sentence) (new NotSentence(alpha)).accept(
						this, arg);
				Sentence notBeta = (Sentence) (new NotSentence(beta)).accept(
						this, arg);
				return new ConnectedSentence(Connectors.OR, notAlpha, notBeta);
			}

			// ~(alpha V beta) equivalent to (~alpha ^ ~beta) (De Morgan)
			if (Connectors.isOR(negConnected.getConnector())) {
				// I need to ensure the ~s are moved in deeper
				Sentence notAlpha = (Sentence) (new NotSentence(alpha)).accept(
						this, arg);
				Sentence notBeta = (Sentence) (new NotSentence(beta)).accept(
						this, arg);
				return new ConnectedSentence(Connectors.AND, notAlpha, notBeta);
			}
		}

		// in addition, rules for negated quantifiers:
		if (negated instanceof QuantifiedSentence) {
			QuantifiedSentence negQuantified = (QuantifiedSentence) negated;
			// I need to ensure the ~ is moved in deeper
			Sentence notP = (Sentence) (new NotSentence(negQuantified
					.getQuantified())).accept(this, arg);

			// ~FORALL x p becomes EXISTS x ~p
			if (Quantifiers.isFORALL(negQuantified.getQuantifier())) {
				return new QuantifiedSentence(Quantifiers.EXISTS, negQuantified
						.getVariables(), notP);
			}

			// ~EXISTS x p becomes FORALL x ~p
			if (Quantifiers.isEXISTS(negQuantified.getQuantifier())) {
				return new QuantifiedSentence(Quantifiers.FORALL, negQuantified
						.getVariables(), notP);
			}
		}

		return new NotSentence((Sentence) negated.accept(this, arg));
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		return new ConnectedSentence(sentence.getConnector(),
				(Sentence) sentence.getFirst().accept(this, arg),
				(Sentence) sentence.getSecond().accept(this, arg));
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {

		return new QuantifiedSentence(sentence.getQuantifier(), sentence
				.getVariables(), (Sentence) sentence.getQuantified().accept(
				this, arg));
	}
}

class StandardizeQuantiferVariables implements FOLVisitor {
	// Just use a localized indexical here.
	private StandardizeApartIndexical quantifiedIndexical = new StandardizeApartIndexical() {
		private int index = 0;

		public String getPrefix() {
			return "q";
		}

		public int getNextIndex() {
			return index++;
		}
	};

	private SubstVisitor substVisitor = null;

	public StandardizeQuantiferVariables(SubstVisitor substVisitor) {
		this.substVisitor = substVisitor;
	}

	public Object visitPredicate(Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(Variable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(Constant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(Function function, Object arg) {
		return function;
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		return new NotSentence((Sentence) sentence.getNegated().accept(this,
				arg));
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		return new ConnectedSentence(sentence.getConnector(),
				(Sentence) sentence.getFirst().accept(this, arg),
				(Sentence) sentence.getSecond().accept(this, arg));
	}

	@SuppressWarnings("unchecked")
	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		Set<Variable> seenSoFar = (Set<Variable>) arg;

		// Keep track of what I have to subst locally and
		// what my renamed variables will be.
		Map<Variable, Term> localSubst = new LinkedHashMap<Variable, Term>();
		List<Variable> replVariables = new ArrayList<Variable>();
		for (Variable v : sentence.getVariables()) {
			// If local variable has be renamed already
			// then I need to come up with own name
			if (seenSoFar.contains(v)) {
				Variable sV = new Variable(quantifiedIndexical.getPrefix()
						+ quantifiedIndexical.getNextIndex());
				localSubst.put(v, sV);
				// Replacement variables should contain new name for variable
				replVariables.add(sV);
			} else {
				// Not already replaced, this name is good
				replVariables.add(v);
			}
		}

		// Apply the local subst
		Sentence subst = substVisitor.subst(localSubst, sentence
				.getQuantified());

		// Ensure all my existing and replaced variable
		// names are tracked
		seenSoFar.addAll(replVariables);

		Sentence sQuantified = (Sentence) subst.accept(this, arg);

		return new QuantifiedSentence(sentence.getQuantifier(), replVariables,
				sQuantified);
	}
}

class RemoveQuantifiers implements FOLVisitor {

	private FOLParser parser = null;
	private SubstVisitor substVisitor = null;

	public RemoveQuantifiers(FOLParser parser) {
		this.parser = parser;

		substVisitor = new SubstVisitor();
	}

	public Object visitPredicate(Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(Variable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(Constant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(Function function, Object arg) {
		return function;
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		return new NotSentence((Sentence) sentence.getNegated().accept(this,
				arg));
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		return new ConnectedSentence(sentence.getConnector(),
				(Sentence) sentence.getFirst().accept(this, arg),
				(Sentence) sentence.getSecond().accept(this, arg));
	}

	@SuppressWarnings("unchecked")
	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		Sentence quantified = sentence.getQuantified();
		Set<Variable> universalScope = (Set<Variable>) arg;

		// Skolemize: Skolemization is the process of removing existential
		// quantifiers by elimination. This is done by introducing Skolem
		// functions. The general rule is that the arguments of the Skolem
		// function are all the universally quantified variables in whose
		// scope the existential quantifier appears.
		if (Quantifiers.isEXISTS(sentence.getQuantifier())) {
			Map<Variable, Term> skolemSubst = new LinkedHashMap<Variable, Term>();
			for (Variable eVar : sentence.getVariables()) {
				if (universalScope.size() > 0) {
					// Replace with a Skolem Function
					String skolemFunctionName = parser.getFOLDomain()
							.addSkolemFunction();
					skolemSubst.put(eVar, new Function(skolemFunctionName,
							new ArrayList<Term>(universalScope)));
				} else {
					// Replace with a Skolem Constant
					String skolemConstantName = parser.getFOLDomain()
							.addSkolemConstant();
					skolemSubst.put(eVar, new Constant(skolemConstantName));
				}
			}

			Sentence skolemized = substVisitor.subst(skolemSubst, quantified);
			return skolemized.accept(this, arg);
		}

		// Drop universal quantifiers.
		if (Quantifiers.isFORALL(sentence.getQuantifier())) {
			// Add to the universal scope so that
			// existential skolemization may be done correctly
			universalScope.addAll(sentence.getVariables());

			Sentence droppedUniversal = (Sentence) quantified.accept(this, arg);

			// Enusre my scope is removed before moving back up
			// the call stack when returning
			universalScope.removeAll(sentence.getVariables());

			return droppedUniversal;
		}

		// Should not reach here as have already
		// handled the two quantifiers.
		throw new IllegalStateException("Unhandled Quantifier:"
				+ sentence.getQuantifier());
	}
}

class DistributeOrOverAnd implements FOLVisitor {

	public DistributeOrOverAnd() {

	}

	public Object visitPredicate(Predicate p, Object arg) {
		return p;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		return equality;
	}

	public Object visitVariable(Variable variable, Object arg) {
		return variable;
	}

	public Object visitConstant(Constant constant, Object arg) {
		return constant;
	}

	public Object visitFunction(Function function, Object arg) {
		return function;
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		return new NotSentence((Sentence) sentence.getNegated().accept(this,
				arg));
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		// Distribute V over ^:

		// This will cause flattening out of nested ^s and Vs
		Sentence alpha = (Sentence) sentence.getFirst().accept(this, arg);
		Sentence beta = (Sentence) sentence.getSecond().accept(this, arg);

		// (alpha V (beta ^ gamma)) equivalent to
		// ((alpha V beta) ^ (alpha V gamma))
		if (Connectors.isOR(sentence.getConnector())
				&& ConnectedSentence.class.isInstance(beta)) {
			ConnectedSentence betaAndGamma = (ConnectedSentence) beta;
			if (Connectors.isAND(betaAndGamma.getConnector())) {
				beta = betaAndGamma.getFirst();
				Sentence gamma = betaAndGamma.getSecond();
				return new ConnectedSentence(Connectors.AND,
						(Sentence) (new ConnectedSentence(Connectors.OR, alpha,
								beta)).accept(this, arg),
						(Sentence) (new ConnectedSentence(Connectors.OR, alpha,
								gamma)).accept(this, arg));
			}
		}

		// ((alpha ^ gamma) V beta) equivalent to
		// ((alpha V beta) ^ (gamma V beta))
		if (Connectors.isOR(sentence.getConnector())
				&& ConnectedSentence.class.isInstance(alpha)) {
			ConnectedSentence alphaAndGamma = (ConnectedSentence) alpha;
			if (Connectors.isAND(alphaAndGamma.getConnector())) {
				alpha = alphaAndGamma.getFirst();
				Sentence gamma = alphaAndGamma.getSecond();
				return new ConnectedSentence(Connectors.AND,
						(Sentence) (new ConnectedSentence(Connectors.OR, alpha,
								beta)).accept(this, arg),
						(Sentence) (new ConnectedSentence(Connectors.OR, gamma,
								beta)).accept(this, arg));
			}
		}

		return new ConnectedSentence(sentence.getConnector(), alpha, beta);
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		// This should not be called as should have already
		// removed all of the quantifiers.
		throw new IllegalStateException(
				"All quantified sentences should have already been removed.");
	}
}

class CNFConstructor implements FOLVisitor {
	public CNFConstructor() {

	}

	public CNF construct(Sentence orDistributedOverAnd) {
		ArgData ad = new ArgData();

		orDistributedOverAnd.accept(this, ad);

		return new CNF(ad.clauses);
	}

	public Object visitPredicate(Predicate p, Object arg) {
		ArgData ad = (ArgData) arg;
		if (ad.negated) {
			ad.clauses.get(ad.clauses.size() - 1).addNegativeLiteral(p);
		} else {
			ad.clauses.get(ad.clauses.size() - 1).addPositiveLiteral(p);
		}
		return p;
	}

	public Object visitTermEquality(TermEquality equality, Object arg) {
		ArgData ad = (ArgData) arg;
		if (ad.negated) {
			ad.clauses.get(ad.clauses.size() - 1).addNegativeLiteral(equality);
		} else {
			ad.clauses.get(ad.clauses.size() - 1).addPositiveLiteral(equality);
		}
		return equality;
	}

	public Object visitVariable(Variable variable, Object arg) {
		// This should not be called
		throw new IllegalStateException("visitVariable() should not be called.");
	}

	public Object visitConstant(Constant constant, Object arg) {
		// This should not be called
		throw new IllegalStateException("visitConstant() should not be called.");
	}

	public Object visitFunction(Function function, Object arg) {
		// This should not be called
		throw new IllegalStateException("visitFunction() should not be called.");
	}

	public Object visitNotSentence(NotSentence sentence, Object arg) {
		ArgData ad = (ArgData) arg;
		// Indicate that the enclosed predicate is negated
		ad.negated = true;
		sentence.getNegated().accept(this, arg);
		ad.negated = false;

		return sentence;
	}

	public Object visitConnectedSentence(ConnectedSentence sentence, Object arg) {
		ArgData ad = (ArgData) arg;
		Sentence first = sentence.getFirst();
		Sentence second = sentence.getSecond();

		first.accept(this, arg);
		if (Connectors.isAND(sentence.getConnector())) {
			ad.clauses.add(new Clause());
		}
		second.accept(this, arg);

		return sentence;
	}

	public Object visitQuantifiedSentence(QuantifiedSentence sentence,
			Object arg) {
		// This should not be called as should have already
		// removed all of the quantifiers.
		throw new IllegalStateException(
				"All quantified sentences should have already been removed.");
	}

	class ArgData {
		public List<Clause> clauses = new ArrayList<Clause>();
		public boolean negated = false;

		public ArgData() {
			clauses.add(new Clause());
		}
	}
}