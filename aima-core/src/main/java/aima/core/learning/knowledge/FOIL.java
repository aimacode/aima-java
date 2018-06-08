package aima.core.learning.knowledge;

import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

import java.util.*;

public class FOIL {
    public FOLKnowledgeBase kb = FOLKnowledgeBaseFactory.familyKnowledgeBase();

    public HashSet<Clause> foil(List<List<HashMap<Variable, Constant>>> examples, Literal target) {
        HashSet<Clause> clauses = new HashSet<>();
        int i = 0;
        while (!examples.get(0).isEmpty()) {
            //
            System.out.println("Positive Examples iteration i == "+ (i++));
            assert(i<50);
            System.out.println(examples.get(0).toString());
            System.out.println(examples.get(1).size());
            //
            Clause clause = newClause(examples, target);
            System.out.println(clause.toString());
            // remove positive examples covered by clause from examples
            for (HashMap<Variable, Constant> example :
                    new ArrayList<>(examples.get(0))) {
                boolean covered = true;
                for (Literal l :
                        clause.getLiterals()) {
                    Map<Variable,Term> tempExample = new HashMap<>(example);
                    if ((!l.getAtomicSentence().getSymbolicName().equals("Grandfather"))&&!kb.ask(kb.subst(tempExample,l).toString()).isTrue()){
                        covered = false;
                        break;
                    }
                }
                if (covered){
                    examples.get(0).remove(example);
                }
            }
            // add clause to clauses
            System.out.println(clause);
            clauses.add(clause);
        }
        return clauses;
    }

    private Clause newClause(List<List<List<Constant>>> examples, Literal target) {
        Clause clause = new Clause(new ArrayList<>(Collections.singletonList(target)));
        Literal l;
        List<List<HashMap<Variable, Constant>>> extendedExamples = examples;
        while (!extendedExamples.get(1).isEmpty()) {
            l = chooseLiteral(newLiterals(clause), extendedExamples);
            System.out.println("CHOSEN LITERAL **** = "+l.toString());
            clause.addLiteral(l);
            List<HashMap<Variable, Constant>> allExamples = new ArrayList<>(extendedExamples.get(1));
            for (HashMap<Variable, Constant> example : allExamples
                    ) {
                Map<Variable,Term> tempEg = new HashMap<>(example);
                if ((!l.getAtomicSentence().getSymbolicName().equals("Grandfather"))&&!kb.ask(kb.subst(tempEg,l).toString()).isTrue())
                    extendedExamples.get(1).remove(example);
            }
            System.out.println("Reduced size =="+extendedExamples.get(1).size());
        }
        return clause;
    }

    private Literal chooseLiteral(List<Literal> literals, List<List<HashMap<Variable, Constant>>> extendedExamples) {
        return literals.get((int) (literals.size() * Math.random()));
    }

    private List<Literal> newLiterals(Clause clause) {
        HashSet<Literal> setOfLiterals = new HashSet<>();
        HashSet<Term> vars = new HashSet<>();
        for (Literal l :
                clause.getLiterals()) {
            vars.addAll(l.getAtomicSentence().getArgs());
        }
        List<Term> variableList = new ArrayList<>(this.vars);
        Collections.shuffle(variableList);
        for (String s :
                kb.getIndexFacts().keySet()) {
            Literal l = kb.getIndexFacts().get(s).get(0);
            Literal toAdd = new Literal(new Predicate(l.getAtomicSentence().getSymbolicName()
                    , variableList.subList(0, l.getAtomicSentence().getArgs().size())), l.isNegativeLiteral());
            setOfLiterals.add(toAdd);
        }
        return new ArrayList<>(setOfLiterals);
    }

    private List<List<HashMap<Variable, Constant>>> extendExample(HashMap<Variable, Constant> example, Literal literal) {
        if (kb.fetch(literal).contains(example)) {
            return new ArrayList<>(Arrays.asList(
                    new ArrayList<>(Collections.singletonList(example)),
                    new ArrayList<>()
            ));
        } else {
            return new ArrayList<>(Arrays.asList(
                    new ArrayList<>(),
                    new ArrayList<>(Collections.singletonList(example))
            ));
        }
    }
}
