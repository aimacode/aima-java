package aima.core.learning.knowledge;

import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

import java.util.*;

public class FOIL {
    public HashSet<Clause> foil(List<List<HashMap<Variable,Constant>>> examples, Literal target){
        HashSet<Clause> clauses = new HashSet<>();
        while(!examples.get(0).isEmpty()){
            Clause clause = newClause(examples, target);
            // remove positive examples covered by clause from examples
            // add clause to clauses
            clauses.add(clause);
        }
        return clauses;
    }

    private Clause newClause(List<List<HashMap<Variable, Constant>>> examples, Literal target) {
        Clause clause = new Clause(new ArrayList<>(Collections.singletonList(target)));
        Literal l;
        List<List<HashMap<Variable,Constant>>> extendedExamples = examples;
        while(!extendedExamples.get(1).isEmpty()){
            //l = chooseLiteral(newLiterals(clause),extendedExamples);
            //clause.addLiteral(l);
        }
        return null;
    }

    private List<List<HashMap<Variable,Constant>>>
    extendExample(HashMap<Variable,Constant> example,Literal literal){
        //if ()
        return null;
    }
}
