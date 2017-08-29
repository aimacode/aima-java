package aima.extra.probability.proposition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import aima.extra.probability.RandomVariable;
import aima.extra.util.ListOps;
import aima.extra.util.MapOps;

/**
 * Proposition is a wrapper class that constitutes the proposition statement.
 * The bound variables (i.e scoped variables) in the proposition statement and
 * unbound variables (i.e unscoped variables) constituting the proposition
 * statement are distinguished. Proposition class is immutable.
 * 
 * @author Nagaraj Poti
 */
public class Proposition {

	// Internal fields

	/**
	 * Proposition statement.
	 */
	private Predicate<Map<RandomVariable, Object>> statement;

	/**
	 * Scoped terms and their values.
	 */
	private Map<RandomVariable, Object> scopedWorld;

	/**
	 * List of scoped / bound terms in the proposition statement.
	 */
	private List<RandomVariable> scopedTerms;

	/**
	 * List of unscoped / unbound terms in the proposition statement.
	 */
	private List<RandomVariable> unscopedTerms;

	// Constructor

	/**
	 * Constructor initializes Proposition class.
	 * 
	 * @param statement
	 *            specified as a predicate function.
	 */
	public Proposition(Predicate<Map<RandomVariable, Object>> statement) {
		this(statement, Collections.emptyMap());
	}

	/**
	 * Constructor initializes Proposition class.
	 * 
	 * @param statement
	 *            specified as a predicate function.
	 * @param scopedWorld
	 *            is the mapping of scoped random variables in the proposition
	 *            statement to their corresponding values.
	 */
	public Proposition(Predicate<Map<RandomVariable, Object>> statement, Map<RandomVariable, Object> scopedWorld) {
		this(statement, scopedWorld, Collections.emptyList());
	}

	/**
	 * Constructor initializes Proposition class.
	 * 
	 * @param statement
	 *            specified as a predicate function.
	 * @param scopedWorld
	 *            is the mapping of scoped random variables in the proposition
	 *            statement to their corresponding values.
	 * @param unscopedTerms
	 *            is the list of unscoped random variables in the proposition
	 *            statement.
	 */
	public Proposition(Predicate<Map<RandomVariable, Object>> statement, Map<RandomVariable, Object> scopedWorld,
			List<RandomVariable> unscopedTerms) {
		Objects.requireNonNull(statement, "Proposition statement must be specified.");
		Objects.requireNonNull(scopedTerms,
				"Scoped variables of proposition statement must be specified. If no scoped variables are present, pass an empty list (not null).");
		Objects.requireNonNull(unscopedTerms,
				"Unscoped variables of proposition statement must be specified. If no unscoped variables are present, pass an empty list (not null).");
		this.statement = statement;
		this.scopedWorld = Collections.unmodifiableMap(scopedWorld);
		this.scopedTerms = Collections.unmodifiableList(new ArrayList<RandomVariable>(scopedWorld.keySet()));
		this.unscopedTerms = Collections
				.unmodifiableList(unscopedTerms.stream().distinct().collect(Collectors.toList()));
	}

	// Public methods

	/**
	 * Create a new Proposition that is a conjunction of two Propositions.
	 * 
	 * @param other
	 *            Proposition.
	 * 
	 * @return a new conjunction Proposition.
	 */
	public Proposition and(Proposition other) {
		Predicate<Map<RandomVariable, Object>> newStatement = this.statement.and(other.statement);
		Map<RandomVariable, Object> newScopedWorld = MapOps.merge(this.scopedWorld, other.scopedWorld);
		List<RandomVariable> newUnscopedTerms = ListOps.union(this.unscopedTerms, other.unscopedTerms);
		Proposition conjunction = new Proposition(newStatement, newScopedWorld, newUnscopedTerms);
		return conjunction;
	}

	// Getter methods

	/**
	 * @return statement
	 */
	public Predicate<Map<RandomVariable, Object>> getStatement() {
		return this.statement;
	}

	/**
	 * @return list of scoped variables.
	 */
	public List<RandomVariable> getScopedTerms() {
		return this.scopedTerms;
	}

	/**
	 * @return list of unscoped variables.
	 */
	public List<RandomVariable> getUnscopedTerms() {
		return this.unscopedTerms;
	}

	/**
	 * @return scopedWorld
	 */
	public Map<RandomVariable, Object> getScopedWorld() {
		return this.scopedWorld;
	}
}
