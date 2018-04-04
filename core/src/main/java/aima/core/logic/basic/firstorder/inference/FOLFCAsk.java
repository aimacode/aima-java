package aima.core.logic.basic.firstorder.inference;

import aima.core.logic.api.firstorderlogic.Clause;
import aima.core.logic.api.firstorderlogic.FOLKnowledgeBase;
import aima.core.logic.basic.firstorder.SubstVisitor;
import aima.core.logic.basic.firstorder.Unifier;
import aima.core.logic.basic.firstorder.kb.data.Literal;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FOLFCAsk {
    private SubstVisitor substVisitor;
    private Unifier unifier;

    public FOLFCAsk() {
        substVisitor= new SubstVisitor();
        unifier = new Unifier();
    }
    public Map<Variable,Term> folFcAsk(FOLKnowledgeBase KB, Sentence query){
        Set<Literal> newSentences = new HashSet<>();
        do{
            newSentences.clear();

            for (Clause rule :
                    KB.getAllDefiniteClauseImplications()) {
                Clause implication = rule.standardizeVariables();
                // for each theta such that SUBST(theta, p1 ^ ... ^ pn) =
                // SUBST(theta, p'1 ^ ... ^ p'n)
                // --- for some p'1,...,p'n in KB
                for (Map<Variable, Term> theta :
                        KB.fetch(implication)) {
                    Literal qPrime = substVisitor.subst(theta,implication.getConsequence());
                    if(!KB.isRenaming(qPrime) && !KB.isRenaming(qPrime,newSentences)){
                        newSentences.add(qPrime);
                        Map<Variable,Term> phi = unifier.unify(qPrime.getAtomicSentence(),query);
                        if (phi != null){
                            return phi;
                        }

                    }

                }

            }
            KB.tell(newSentences);
        }while(!newSentences.isEmpty());
        return null; // here null represents a false substitution
    }
}
