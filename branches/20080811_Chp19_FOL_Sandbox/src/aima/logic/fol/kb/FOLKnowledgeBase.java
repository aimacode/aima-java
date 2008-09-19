package aima.logic.fol.kb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.logic.fol.FOLDomain;
import aima.logic.fol.StandardizeApart;
import aima.logic.fol.StandardizeApartIndexical;
import aima.logic.fol.StandardizeApartResult;
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
	private StandardizeApart standardizeApart;
	//
	// Persistent data structures
	//
	// Keeps track of the Sentences in their original form as added to the
	// Knowledge base.
	private List<Sentence> originalSentences = new ArrayList<Sentence>();
	// All the facts in the KB indexed by Predicate name (Note: pg. 279)
	private Map<String, List<Predicate>> indexFacts = new HashMap<String, List<Predicate>>();
	// Keep track of indexical keys for uniquely standardizing apart sentences
	private StandardizeApartIndexical variableIndexical = new StandardizeApartIndexical(
			"v");
	private StandardizeApartIndexical queryIndexical = new StandardizeApartIndexical(
			"q");

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
		this.standardizeApart = new StandardizeApart(variableCollector,
				substVisitor);
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
	 * @return two possible return values exist. 1. an empty Set, the query
	 *         returned false. 2. a Set of substitutions, indicates true and
	 *         the bindings for different possible answers to the query (Note:
	 *         refer to page 256).
	 */
	public Set<Map<Variable, Term>> ask(String aQuerySentence) {
		return ask(parser.parse(aQuerySentence));
	}

	public Set<Map<Variable, Term>> ask(Sentence aQuery) {
		// Want to standardize apart the query to ensure
		// it does not clash with any of the sentences
		// in the database
		StandardizeApartResult saResult = standardizeApart.standardizeApart(aQuery,
				queryIndexical);

		// Need to map the result variables (as they are standardized apart)
		// to the original queries variables so that the caller can easily
		// understand and use the returned set of substitutions
		Set<Map<Variable, Term>> internalResult = inferenceProcedure.ask(this, saResult.getStandardized());
		Set<Map<Variable, Term>> externalResult = new LinkedHashSet<Map<Variable, Term>>();
		for (Map<Variable, Term> im : internalResult) {
			Map<Variable, Term> em = new LinkedHashMap<Variable, Term>();
			for (Variable rev : saResult.getReverseSubstitution().keySet()) {
				em.put((Variable) saResult.getReverseSubstitution().get(rev),
						im.get(rev));
			}
			externalResult.add(em);
		}
		
		return externalResult;
	}
	
	// Note: pg 278, FETCH(q) concept.
	public synchronized Set<Map<Variable, Term>> fetch(Predicate p) {
		// Get all of the substitutions in the KB that p unifies with
		Set<Map<Variable, Term>> allUnifiers = new LinkedHashSet<Map<Variable, Term>>();

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
	
	// TODO: Note: To support FOL-FC-Ask
	public Set<Map<Variable, Term>> fetch(List<Predicate> predicates) {
		Set<Map<Variable, Term>> possibleSubstitutions = new LinkedHashSet<Map<Variable, Term>>();

		if (predicates.size() > 0) {
			Predicate first = predicates.get(0);
			List<Predicate> rest = new ArrayList<Predicate>(predicates.subList(1, predicates.size()));

			recursiveFetch(new LinkedHashMap<Variable, Term>(), first, rest,
					possibleSubstitutions);
		}

		return possibleSubstitutions;
	}
	
	// Note: see page 277.
	public Sentence standardizeApart(Sentence aSentence) {
		return standardizeApart.standardizeApart(aSentence, variableIndexical)
				.getStandardized();
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
	
	private void recursiveFetch(Map<Variable, Term> theta, Predicate p,
			List<Predicate> remainingPredicates,
			Set<Map<Variable, Term>> possibleSubstitutions) {

		// Find all substitutions for current predicate based on the
		// substitutions of prior predicates in the list (i.e. SUBST with theta).
		Set<Map<Variable, Term>> pSubsts = fetch((Predicate) subst(theta, p));

		// No substitutions, therefore cannot continue
		if (null == pSubsts) {
			return;
		}

		for (Map<Variable, Term> psubst : pSubsts) {
			// Ensure all prior substitution information is maintained
			// along the chain of predicates (i.e. for shared variables
			// across the predicates).
			psubst.putAll(theta);
			if (remainingPredicates.size() == 0) {
				// This means I am at the end of the chain of predicates
				// and have found a valid substitution.
				possibleSubstitutions.add(psubst);
			} else {
				// Need to move to the next link in the chain of substitutions
				Predicate first = remainingPredicates.get(0);
				List<Predicate> rest = new ArrayList<Predicate>(
						remainingPredicates.subList(1, remainingPredicates.size()));
				
				recursiveFetch(psubst, first, rest, possibleSubstitutions);
			}
		}
	}
}