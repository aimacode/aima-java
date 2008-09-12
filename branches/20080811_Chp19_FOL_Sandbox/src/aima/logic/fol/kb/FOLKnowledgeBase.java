package aima.logic.fol.kb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.FOLDomain;
import aima.logic.fol.SubstVisitor;
import aima.logic.fol.Unifier;
import aima.logic.fol.VariableCollector;
import aima.logic.fol.inference.InferenceProcedure;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.FOLNode;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Sentence;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * A First Order Logic (FOL) Knowledge Base.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class FOLKnowledgeBase {

	private FOLParser parser;
	private InferenceProcedure inferenceProcedure;
	private Unifier unifier;
	private SubstVisitor substVisitor;
	private VariableCollector variableCollector;
	//
	// Persistent data structures
	//
	// Keeps track of the Sentences in their original form as added to the
	// Knowledge base.
	private List<Sentence> originalSentences = new ArrayList<Sentence>();
	// All the facts in the KB indexed by Predicate name (Note: pg. 279)
	private Map<String, List<Predicate>> indexFacts = new HashMap<String, List<Predicate>>();
	// Keep track of indexical keys for uniquely standardizing apart sentences
	private Integer standardizedApartIndexical = 0;

	//
	// PUBLIC METHODS
	//

	public FOLKnowledgeBase(FOLDomain domain,
			InferenceProcedure inferenceProcedure) {
		this(domain, inferenceProcedure, new Unifier());
	}

	public FOLKnowledgeBase(FOLDomain domain,
			InferenceProcedure inferenceProcedure, Unifier unifier) {
		this.parser = new FOLParser(domain);
		this.inferenceProcedure = inferenceProcedure;
		this.unifier = unifier;
		//
		this.substVisitor = new SubstVisitor(parser);
		this.variableCollector = new VariableCollector(parser);
	}
	
	public Map<Variable, Term> unify(FOLNode x, FOLNode y) {
		return unifier.unify(x, y);
	}
	
	public Map<Variable, Term> unify(FOLNode x, FOLNode y,
			Map<Variable, Term> theta) {
		return unifier.unify(x, y, theta);
	}

	public Sentence subst(Map<Variable, Term> theta, Sentence aSentence) {
		return substVisitor.subst(theta, aSentence);
	}

	public void tell(String aSentence) {
		tell(parser.parse(aSentence));
	}
	
	public void tell(List<? extends Sentence> sentences) {
		for (Sentence s : sentences) {
			tell(s);
		}
	}

	public void tell(Sentence aSentence) {
		store(aSentence);
	}

	/**
	 * 
	 * @param aQuerySentence
	 * @return two possible return values exist. 1. an empty list the query
	 *         returned false. 2. a list of substitutions, indicates true and
	 *         the bindings for different possible answers to the query (Note:
	 *         refer to page 256).
	 */
	public List<Map<Variable, Term>> ask(String aQuerySentence) {
		return ask(parser.parse(aQuerySentence));
	}

	public List<Map<Variable, Term>> ask(Sentence aQuery) {
		return inferenceProcedure.ask(this, aQuery);
	}
	
	// Note: pg 278, FETCH(q) concept.
	public synchronized List<Map<Variable, Term>> fetch(Predicate p) {
		// Get all of the substitutions in the KB that p unifies with
		List<Map<Variable, Term>> allUnifiers = new ArrayList<Map<Variable, Term>>();

		List<Predicate> matchingPredicates = indexFacts.get(p
				.getPredicateName());
		if (null != matchingPredicates) {
			for (Predicate fact : matchingPredicates) {
				Map<Variable, Term> substitution = unifier.unify(p, fact);
				if (null != substitution) {
					allUnifiers.add(substitution);
				}
			}
		}

		return allUnifiers;
	}
	
	// Note: see page 277.
	public Sentence standardizeApart(Sentence aSentence) {
		Set<Variable> toRename = variableCollector
				.collectAllVariables(aSentence);
		Map<Variable, Term> renameSubstitution = new HashMap<Variable, Term>();
		
		synchronized (standardizedApartIndexical) {
			for (Variable var : toRename) {
				standardizedApartIndexical = standardizedApartIndexical + 1;
				renameSubstitution.put(var, new Variable("v"
						+ standardizedApartIndexical));
			}
		}
	
		return substVisitor.subst(renameSubstitution, aSentence); 
	}
	
	// Note: see pg. 281
	public boolean isRenaming(Predicate p) {
		List<Predicate> possibleMatches = indexFacts.get(p.getPredicateName());
		if (null != possibleMatches) {
			return isRenaming(p, possibleMatches);
		}

		return false;
	}

	// Note: see pg. 281
	public boolean isRenaming(Predicate p, List<Predicate> possibleMatches) {

		for (Predicate q : possibleMatches) {
			Map<Variable, Term> subst = unifier.unify(p, q);
			if (null != subst) {
				int cntVarTerms = 0;
				for (Term t : subst.values()) {
					if (t instanceof Variable) {
						cntVarTerms++;
					}
				}
				// If all the substitutions, even if none, map to Variables
				// then this is a renaming
				if (subst.size() == cntVarTerms) {
					return true;
				}
			}
		}

		return false;
	}

	//
	// PROTECTED METHODS
	//

	protected FOLParser getParser() {
		return parser;
	}

	//
	// PRIVATE METHODS
	//
	
	// Note: pg 278, STORE(s) concept.
	private synchronized void store(Sentence aSentence) {
		// Keep a copy of the original sentences, so do
		// not have to worry about them being manipulated
		// externally.
		originalSentences.add(parser.parse(aSentence.toString()));
		
		// TODO: Consider.
		// 1. Internal representation of sentences to ease inference overheads
		// (e.g. stripping overly parenthesized sentences, standardized apart,
		// CNF, etc...).
		// 2. Introduction of Skolem constants and tying back to original
		// sentence, this implies internal to original mapping will not be one
		// to one.
		// 3. Consider previous Universal Instantiations, if applicable, when
		// adding new ground terms.
		// 4. Check for duplicate facts/sentences, i.e. renamings (see pg. 281).

		indexFact(aSentence);
	}
	
	// TODO: consider negated predicates?
	private void indexFact(Sentence aSentence) {
		// Only if it is a Predicate does it get indexed as a fact
		// see pg. 279.
		if (!Predicate.class.isInstance(aSentence)) {
			return;
		}
		
		Predicate fact = (Predicate) aSentence;
		
		if (!indexFacts.containsKey(fact.getPredicateName())) {
			indexFacts.put(fact.getPredicateName(), new ArrayList<Predicate>());
		}

		indexFacts.get(fact.getPredicateName()).add(fact);
	}
}