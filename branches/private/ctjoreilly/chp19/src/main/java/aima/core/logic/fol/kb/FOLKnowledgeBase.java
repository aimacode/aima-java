package aima.core.logic.fol.kb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.StandardizeApart;
import aima.core.logic.fol.StandardizeApartIndexical;
import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.StandardizeApartResult;
import aima.core.logic.fol.SubstVisitor;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.VariableCollector;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.FOLOTTERLikeTheoremProver;
import aima.core.logic.fol.inference.InferenceProcedure;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofStepClauseClausifySentence;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Chain;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.FOLNode;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

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
	private CNFConverter cnfConverter;
	//
	// Persistent data structures
	//
	// Keeps track of the Sentences in their original form as added to the
	// Knowledge base.
	private List<Sentence> originalSentences = new ArrayList<Sentence>();
	// The KB in clause form
	private Set<Clause> clauses = new LinkedHashSet<Clause>();
	// Keep track of all of the definite clauses in the database
	// along with those that represent implications.
	private List<Clause> allDefiniteClauses = new ArrayList<Clause>();
	private List<Clause> implicationDefiniteClauses = new ArrayList<Clause>();
	// All the facts in the KB indexed by Atomic Sentence name (Note: pg. 279)
	private Map<String, List<Literal>> indexFacts = new HashMap<String, List<Literal>>();
	// Keep track of indexical keys for uniquely standardizing apart sentences
	private StandardizeApartIndexical variableIndexical = StandardizeApartIndexicalFactory
			.newStandardizeApartIndexical('v');
	private StandardizeApartIndexical queryIndexical = StandardizeApartIndexicalFactory
			.newStandardizeApartIndexical('q');

	//
	// PUBLIC METHODS
	//
	public FOLKnowledgeBase(FOLDomain domain) {
		// Default to Full Resolution if not set.
		this(domain, new FOLOTTERLikeTheoremProver());
	}

	public FOLKnowledgeBase(FOLDomain domain,
			InferenceProcedure inferenceProcedure) {
		this(domain, inferenceProcedure, new Unifier());
	}

	public FOLKnowledgeBase(FOLDomain domain,
			InferenceProcedure inferenceProcedure, Unifier unifier) {
		this.parser = new FOLParser(new FOLDomain(domain));
		this.inferenceProcedure = inferenceProcedure;
		this.unifier = unifier;
		//
		this.substVisitor = new SubstVisitor();
		this.variableCollector = new VariableCollector();
		this.standardizeApart = new StandardizeApart(variableCollector,
				substVisitor);
		this.cnfConverter = new CNFConverter(parser);
	}

	public void clear() {
		this.originalSentences.clear();
		this.clauses.clear();
		this.allDefiniteClauses.clear();
		this.implicationDefiniteClauses.clear();
		this.indexFacts.clear();
	}

	public InferenceProcedure getInferenceProcedure() {
		return inferenceProcedure;
	}

	public void setInferenceProcedure(InferenceProcedure inferenceProcedure) {
		if (null != inferenceProcedure) {
			this.inferenceProcedure = inferenceProcedure;
		}
	}

	public Sentence tell(String aSentence) {
		Sentence s = parser.parse(aSentence);
		tell(s);
		return s;
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
	 * @return an InferenceResult.
	 */
	public InferenceResult ask(String aQuerySentence) {
		return ask(parser.parse(aQuerySentence));
	}

	public InferenceResult ask(Sentence aQuery) {
		// Want to standardize apart the query to ensure
		// it does not clash with any of the sentences
		// in the database
		StandardizeApartResult saResult = standardizeApart.standardizeApart(
				aQuery, queryIndexical);

		// Need to map the result variables (as they are standardized apart)
		// to the original queries variables so that the caller can easily
		// understand and use the returned set of substitutions
		InferenceResult infResult = getInferenceProcedure().ask(this,
				saResult.getStandardized());
		for (Proof p : infResult.getProofs()) {
			Map<Variable, Term> im = p.getAnswerBindings();
			Map<Variable, Term> em = new LinkedHashMap<Variable, Term>();
			for (Variable rev : saResult.getReverseSubstitution().keySet()) {
				em.put((Variable) saResult.getReverseSubstitution().get(rev),
						im.get(rev));
			}
			p.replaceAnswerBindings(em);
		}

		return infResult;
	}

	public int getNumberFacts() {
		return allDefiniteClauses.size() - implicationDefiniteClauses.size();
	}

	public int getNumberRules() {
		return clauses.size() - getNumberFacts();
	}

	public List<Sentence> getOriginalSentences() {
		return Collections.unmodifiableList(originalSentences);
	}

	public List<Clause> getAllDefiniteClauses() {
		return Collections.unmodifiableList(allDefiniteClauses);
	}

	public List<Clause> getAllDefiniteClauseImplications() {
		return Collections.unmodifiableList(implicationDefiniteClauses);
	}

	public Set<Clause> getAllClauses() {
		return Collections.unmodifiableSet(clauses);
	}

	// Note: pg 278, FETCH(q) concept.
	public synchronized Set<Map<Variable, Term>> fetch(Literal l) {
		// Get all of the substitutions in the KB that p unifies with
		Set<Map<Variable, Term>> allUnifiers = new LinkedHashSet<Map<Variable, Term>>();

		List<Literal> matchingFacts = fetchMatchingFacts(l);
		if (null != matchingFacts) {
			for (Literal fact : matchingFacts) {
				Map<Variable, Term> substitution = unifier.unify(l
						.getAtomicSentence(), fact.getAtomicSentence());
				if (null != substitution) {
					allUnifiers.add(substitution);
				}
			}
		}

		return allUnifiers;
	}

	// Note: To support FOL-FC-Ask
	public Set<Map<Variable, Term>> fetch(List<Literal> literals) {
		Set<Map<Variable, Term>> possibleSubstitutions = new LinkedHashSet<Map<Variable, Term>>();

		if (literals.size() > 0) {
			Literal first = literals.get(0);
			List<Literal> rest = literals.subList(1, literals.size());

			recursiveFetch(new LinkedHashMap<Variable, Term>(), first, rest,
					possibleSubstitutions);
		}

		return possibleSubstitutions;
	}

	public Map<Variable, Term> unify(FOLNode x, FOLNode y) {
		return unifier.unify(x, y);
	}

	public Sentence subst(Map<Variable, Term> theta, Sentence aSentence) {
		return substVisitor.subst(theta, aSentence);
	}

	public Literal subst(Map<Variable, Term> theta, Literal l) {
		return substVisitor.subst(theta, l);
	}

	public Term subst(Map<Variable, Term> theta, Term aTerm) {
		return substVisitor.subst(theta, aTerm);
	}

	// Note: see page 277.
	public Sentence standardizeApart(Sentence aSentence) {
		return standardizeApart.standardizeApart(aSentence, variableIndexical)
				.getStandardized();
	}

	public Clause standardizeApart(Clause aClause) {
		return standardizeApart.standardizeApart(aClause, variableIndexical);
	}

	public Chain standardizeApart(Chain aChain) {
		return standardizeApart.standardizeApart(aChain, variableIndexical);
	}

	public Set<Variable> collectAllVariables(Sentence aSentence) {
		return variableCollector.collectAllVariables(aSentence);
	}

	public CNF convertToCNF(Sentence aSentence) {
		return cnfConverter.convertToCNF(aSentence);
	}

	public Set<Clause> convertToClauses(Sentence aSentence) {
		CNF cnf = cnfConverter.convertToCNF(aSentence);

		return new LinkedHashSet<Clause>(cnf.getConjunctionOfClauses());
	}

	public Literal createAnswerLiteral(Sentence forQuery) {
		String alName = parser.getFOLDomain().addAnswerLiteral();
		List<Term> terms = new ArrayList<Term>();

		Set<Variable> vars = variableCollector.collectAllVariables(forQuery);
		for (Variable v : vars) {
			// Ensure copies of the variables are used.
			terms.add(v.copy());
		}

		return new Literal(new Predicate(alName, terms));
	}

	// Note: see pg. 281
	public boolean isRenaming(Literal l) {
		List<Literal> possibleMatches = fetchMatchingFacts(l);
		if (null != possibleMatches) {
			return isRenaming(l, possibleMatches);
		}

		return false;
	}

	// Note: see pg. 281
	public boolean isRenaming(Literal l, List<Literal> possibleMatches) {

		for (Literal q : possibleMatches) {
			if (l.isPositiveLiteral() != q.isPositiveLiteral()) {
				continue;
			}
			Map<Variable, Term> subst = unifier.unify(l.getAtomicSentence(), q
					.getAtomicSentence());
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Sentence s : originalSentences) {
			sb.append(s.toString());
			sb.append("\n");
		}
		return sb.toString();
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
		originalSentences.add(aSentence);

		// Convert the sentence to CNF
		CNF cnfOfOrig = cnfConverter.convertToCNF(aSentence);
		for (Clause c : cnfOfOrig.getConjunctionOfClauses()) {
			c.setProofStep(new ProofStepClauseClausifySentence(c, aSentence));
			if (c.isEmpty()) {
				// This should not happen, if so the user
				// is trying to add an unsatisfiable sentence
				// to the KB.
				throw new IllegalArgumentException(
						"Attempted to add unsatisfiable sentence to KB, orig=["
								+ aSentence + "] CNF=" + cnfOfOrig);
			}

			// Ensure all clauses added to the KB are Standardized Apart.
			c = standardizeApart.standardizeApart(c, variableIndexical);

			// Will make all clauses immutable
			// so that they cannot be modified externally.
			c.setImmutable();
			if (clauses.add(c)) {
				// If added keep track of special types of
				// clauses, as useful for query purposes
				if (c.isDefiniteClause()) {
					allDefiniteClauses.add(c);
				}
				if (c.isImplicationDefiniteClause()) {
					implicationDefiniteClauses.add(c);
				}
				if (c.isUnitClause()) {
					indexFact(c.getLiterals().iterator().next());
				}
			}
		}
	}

	// Only if it is a unit clause does it get indexed as a fact
	// see pg. 279 for general idea.
	private void indexFact(Literal fact) {
		String factKey = getFactKey(fact);
		if (!indexFacts.containsKey(factKey)) {
			indexFacts.put(factKey, new ArrayList<Literal>());
		}

		indexFacts.get(factKey).add(fact);
	}

	private void recursiveFetch(Map<Variable, Term> theta, Literal l,
			List<Literal> remainingLiterals,
			Set<Map<Variable, Term>> possibleSubstitutions) {

		// Find all substitutions for current predicate based on the
		// substitutions of prior predicates in the list (i.e. SUBST with
		// theta).
		Set<Map<Variable, Term>> pSubsts = fetch(subst(theta, l));

		// No substitutions, therefore cannot continue
		if (null == pSubsts) {
			return;
		}

		for (Map<Variable, Term> psubst : pSubsts) {
			// Ensure all prior substitution information is maintained
			// along the chain of predicates (i.e. for shared variables
			// across the predicates).
			psubst.putAll(theta);
			if (remainingLiterals.size() == 0) {
				// This means I am at the end of the chain of predicates
				// and have found a valid substitution.
				possibleSubstitutions.add(psubst);
			} else {
				// Need to move to the next link in the chain of substitutions
				Literal first = remainingLiterals.get(0);
				List<Literal> rest = remainingLiterals.subList(1,
						remainingLiterals.size());

				recursiveFetch(psubst, first, rest, possibleSubstitutions);
			}
		}
	}

	private List<Literal> fetchMatchingFacts(Literal l) {
		return indexFacts.get(getFactKey(l));
	}

	private String getFactKey(Literal l) {
		StringBuilder key = new StringBuilder();
		if (l.isPositiveLiteral()) {
			key.append("+");
		} else {
			key.append("-");
		}
		key.append(l.getAtomicSentence().getSymbolicName());

		return key.toString();
	}
}