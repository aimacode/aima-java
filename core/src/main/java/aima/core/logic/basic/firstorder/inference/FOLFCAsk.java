package aima.core.logic.basic.firstorder.inference;

import aima.core.logic.api.firstorderlogic.Clause;
import aima.core.logic.api.firstorderlogic.FOLKnowledgeBase;
import aima.core.logic.basic.firstorder.SubstVisitor;
import aima.core.logic.basic.firstorder.Unifier;
import aima.core.logic.basic.firstorder.kb.data.Literal;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ?.?, page
 * ??.<br>
 * <br>
 *
 * <pre>
 * function FOL-FC-ASK(KB, alpha) returns a substitution or false
 *   inputs: KB, the knowledge base, a set of first order definite clauses
 *           alpha, the query, an atomic sentence
 *   local variables: new, the new sentences inferred on each iteration
 *
 *   repeat until new is empty
 *      new &lt;- {}
 *      for each rule in KB do
 *          (p1 &circ; ... &circ; pn =&gt; q) &lt;- STANDARDIZE-VARIABLES(rule)
 *          for each theta such that SUBST(theta, p1 &circ; ... &circ; pn) = SUBST(theta, p'1 &circ; ... &circ; p'n)
 *                         for some p'1,...,p'n in KB
 *              q' &lt;- SUBST(theta, q)
 *              if q' does not unify with some sentence already in KB or new then
 *                   add q' to new
 *                   phi &lt;- UNIFY(q', alpha)
 *                   if theta is not fail then return theta
 *      add new to KB
 *   return false
 * </pre>
 *
 * Figure ?.? A conceptually straightforward, but very inefficient
 * forward-chaining algorithm. On each iteration, it adds to KB all the atomic
 * sentences that can be inferred in one step from the implication sentences and
 * the atomic sentences already in KB.
 *
 * @author samagra
 *
 */


public class FOLFCAsk {
    //Variables to call SUBST(theta,q) and UNIFY(q',alpha)
    private SubstVisitor substVisitor;
    private Unifier unifier;

    //Public constructor
    public FOLFCAsk() {
        substVisitor = new SubstVisitor();
        unifier = new Unifier();
    }

    //function FOL-FC-ASK(KB, alpha) returns a substitution or false
    //  inputs: KB, the knowledge base, a set of first order definite clauses
    //           alpha, the query, an atomic sentence
    public Map<Variable, Term> folFcAsk(FOLKnowledgeBase KB, Sentence query) {
        //local variables: new, the new sentences inferred on each iteration
        Set<Literal> newSentences = new HashSet<>();
        //repeat until new is empty
        do {
            //new <- {}
            newSentences.clear();
            //for each rule in KB do
            for (Clause rule : KB.getAllDefiniteClauseImplications()) {
                //(p1^...^ pn =>q) <- STANDARDIZE-VARIABLES(rule)
                Clause implication = rule.standardizeVariables();
                // for each theta such that SUBST(theta, p1 ^ ... ^ pn) = SUBST(theta, p'1 ^ ... ^ p'n)
                // --- for some p'1,...,p'n in KB
                for (Map<Variable, Term> theta :
                        KB.fetch(implication)) {
                    //q' <- SUBST(theta,q)
                    Literal qPrime = substVisitor.subst(theta, implication.getConsequence());
                    //if q' does not unify with some sentence already in KB or new then
                    if (!KB.isRenaming(qPrime) && !KB.isRenaming(qPrime, newSentences)) {
                        //add q' to new
                        newSentences.add(qPrime);
                        //phi <- UNIFY(q', alpha)
                        Map<Variable, Term> phi = unifier.unify(qPrime.getAtomicSentence(), query);
                        // if phi is not fail then return phi
                        if (phi != null) return phi;
                    }
                }
            }
            //add new to KB
            KB.tell(newSentences);
        } while (!newSentences.isEmpty());
        //return false
        return null; // here null represents a false substitution
    }
}
