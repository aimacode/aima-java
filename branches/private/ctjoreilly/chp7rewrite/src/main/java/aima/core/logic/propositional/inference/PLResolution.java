package aima.core.logic.propositional.inference;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.kb.data.Clause;
import aima.core.logic.propositional.kb.data.Literal;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.propositional.parsing.ast.Sentence;
import aima.core.util.SetOps;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 255.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function PL-RESOLUTION(KB, &alpha;) returns true or false
 *    inputs: KB, the knowledge base, a sentence in propositional logic
 *            &alpha;, the query, a sentence in propositional logic
 *            
 *    clauses &larr; the set of clauses in the CNF representation of KB &and; &not;&alpha;
 *    new &larr; {}
 *    loop do
 *       for each pair of clauses C<sub>i</sub>, C<sub>j</sub> in clauses do
 *          resolvents &larr; PL-RESOLVE(C<sub>i</sub>, C<sub>j</sub>)
 *          if resolvents contains the empty clause then return true
 *          new &larr; new &cup; resolvents
 *       if new &sube; clauses then return false
 *       clauses &larr; clauses &cup; new
 * </code>
 * </pre>
 * 
 * Figure 7.12 A simple resolution algorithm for propositional logic. The
 * function PL-RESOLVE returns the set of all possible clauses obtained by
 * resolving its two inputs.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class PLResolution {
	
	public boolean plResolution(KnowledgeBase kb, Sentence alpha) {
		throw new UnsupportedOperationException("TODO");
	}
	
	public Set<Clause> plResolve(Clause c1, Clause c2) {
		Set<Clause> resolvents = new LinkedHashSet<Clause>();
	
		resolvePositiveWithNegative(c1, c2, resolvents);
		resolvePositiveWithNegative(c2, c1, resolvents);
		
		return resolvents;
	}
	
	//
	// PRIVATE
	//
	private void resolvePositiveWithNegative(Clause c1, Clause c2, Set<Clause> resolvents) {
		Set<PropositionSymbol> complementary = SetOps.intersection(c1.getPositiveSymbols(), c2.getNegativeSymbols());
		for (PropositionSymbol complement : complementary) {
			List<Literal> resolventLiterals = new ArrayList<Literal>();
			for (Literal c1l : c1.getLiterals()) {
				if (c1l.isNegativeLiteral() || !c1l.getAtomicSentence().equals(complement)) {
					resolventLiterals.add(c1l);
				}
			}
			for (Literal c2l : c2.getLiterals()) {
				if (c2l.isPositiveLiteral() || !c2l.getAtomicSentence().equals(complement)) {
					resolventLiterals.add(c2l);
				}
			}
			resolvents.add(new Clause(resolventLiterals));
		}
	}
}