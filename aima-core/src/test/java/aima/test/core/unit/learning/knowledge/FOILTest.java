package aima.test.core.unit.learning.knowledge;

import aima.core.learning.knowledge.FOIL;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.util.math.permute.PermutationGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FOILTest {
    @Test
    public void foilTest(){
        FOLKnowledgeBase kb = FOLKnowledgeBaseFactory.familyKnowledgeBase();
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Literal l = new Literal(new Predicate("Mother", Arrays.asList(x,y)));
        System.out.println(kb.fetch(l).toString());
        System.out.println("Test");
        HashMap<Variable,Term> testing = new HashMap<>();
        testing.put(x,new Constant("Mum"));
        testing.put(y,new Constant("Spencer"));
        System.out.println(kb.ask(kb.subst(testing,l).toString()).isTrue());

        FOIL algo = new FOIL();
        Literal lit = new Literal(new Predicate("Grandfather",Arrays.asList(x,y)));
        List<HashMap<Variable,Constant>> positiveExamples = new ArrayList<>();
        List<HashMap<Variable,Constant>> negativeExamples = new ArrayList<>();
        System.out.println(algo.kb.domain.getConstants().toString());
        for (List<String> list :
                PermutationGenerator.product(new ArrayList<>(algo.kb.domain.getConstants()),new ArrayList<>(algo.kb.domain.getConstants()))) {
            HashMap<Variable,Constant> temp = new HashMap<>();
            temp.put(x,new Constant(list.get(0)));
            temp.put(y,new Constant(list.get(1)));
            negativeExamples.add(new HashMap<>(temp));
        }
        //positive examples
        HashMap<Variable,Constant> temp = new HashMap<>();
        temp.put(x,new Constant("George"));
        temp.put(y,new Constant("Charles"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("George"));
        temp.put(y,new Constant("Anne"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("George"));
        temp.put(y,new Constant("Anne"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("George"));
        temp.put(y,new Constant("Andrew"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("George"));
        temp.put(y,new Constant("Edward"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Spencer"));
        temp.put(y,new Constant("William"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Spencer"));
        temp.put(y,new Constant("Harry"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Philip"));
        temp.put(y,new Constant("William"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Philip"));
        temp.put(y,new Constant("Harry"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Philip"));
        temp.put(y,new Constant("Peter"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Philip"));
        temp.put(y,new Constant("Zara"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Philip"));
        temp.put(y,new Constant("Beatrice"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Philip"));
        temp.put(y,new Constant("Eugenie"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Philip"));
        temp.put(y,new Constant("Louise"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        temp.clear();
        temp.put(x,new Constant("Philip"));
        temp.put(y,new Constant("James"));
        negativeExamples.remove(new HashMap<>(temp));
        positiveExamples.add(new HashMap<>(temp));
        List<List<HashMap<Variable,Constant>>> examples = new ArrayList<>();
        examples.add(new ArrayList<>(positiveExamples));
        examples.add(new ArrayList<>(negativeExamples));
        System.out.println("Fingers crossed !!!!");
        System.out.println(algo.foil(examples,lit).toString());
    }
}
