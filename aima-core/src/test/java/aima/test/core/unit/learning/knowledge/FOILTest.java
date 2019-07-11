package aima.test.core.unit.learning.knowledge;

import aima.core.learning.knowledge.FOIL;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.util.math.permute.PermutationGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class FOILTest {
	FOLKnowledgeBase kb;
	Variable x, y;
	
	@Before
	public void setup() {
		kb = FOLKnowledgeBaseFactory.familyKnowledgeBase();
		x = new Variable("x");
		y = new Variable("y");
	}
	
	@Test
	public void testFamilyTree() {
		Literal l = new Literal(new Predicate("Mother", Arrays.asList(x, y)));
		HashMap<Variable, Term> testing = new HashMap<>();
		
		testing.put(x, new Constant("Mum"));
		testing.put(y, new Constant("Spencer"));
		Assert.assertFalse(kb.ask(kb.subst(testing, l).toString()).isTrue());
		
		testing.put(x, new Constant("Mum"));
		testing.put(y, new Constant("Elizabeth"));
		Assert.assertTrue(kb.ask(kb.subst(testing, l).toString()).isTrue());
	}
	
	@Test
	public void testFoil() {
		FOIL algo = new FOIL(kb);
		Literal target = new Literal(new Predicate("Grandfather", Arrays.asList(x, y)));
		List<List<HashMap<Variable, Constant>>> examples = generateExamples(algo);
		HashSet<Clause> hashSet = algo.apply(examples, target);
		
		HashMap<Variable, Constant> testing = new HashMap<>();
		testing.put(x, new Constant("George"));
		testing.put(y, new Constant("Charles"));
		boolean f = false;
		for(Clause clause : hashSet){
			if(check(clause, testing, target)) f = true;
		}
		Assert.assertTrue(f);

		testing.put(x, new Constant("Spencer"));
		testing.put(y, new Constant("Harry"));
		f = false;
		for(Clause clause : hashSet){
			if(check(clause, testing, target)) f = true;
		}
		Assert.assertTrue(f);

		testing.put(x, new Constant("Spencer"));
		testing.put(y, new Constant("Zara"));
		f = false;
		for(Clause clause : hashSet){
			if(check(clause, testing, target)) f = true;
		}
		Assert.assertFalse(f);

		testing.put(x, new Constant("Philip"));
		testing.put(y, new Constant("James"));
		f = false;
		for(Clause clause : hashSet){
			if(check(clause, testing, target)) f = true;
		}
		Assert.assertTrue(f);
		
	}
	
	private boolean check(Clause c, HashMap<Variable, Constant> testing, Literal target) {
		List<HashMap<Variable, Constant>> extended_examples = new ArrayList<>();
		extended_examples.add(testing);
		for (Literal l : c.getLiterals()) {
			if (l.getAtomicSentence().getSymbolicName().equals(target.getAtomicSentence().getSymbolicName())) continue;
			List<HashMap<Variable, Constant>> extended_examples_temp = new ArrayList<>();
			for (HashMap<Variable, Constant> map : extended_examples) {
				Map<Variable, Term> tempEg = new HashMap<>(map);
				InferenceResult result = kb.ask(kb.subst(tempEg, l).toString());
				if (result.isTrue()) {
					for (Proof proof : result.getProofs()) {
						Map<Variable, Term> tempCondition = proof.getAnswerBindings();
						HashMap<Variable, Constant> h = new HashMap<>(map);
						for (Variable v : tempCondition.keySet()) {
							h.put(v, new Constant(tempCondition.get(v).toString()));
						}
						extended_examples_temp.add(h);
					}
				}
			}
			extended_examples.clear();
			extended_examples.addAll(extended_examples_temp);
		}
		return extended_examples.size() > 0;
	}
	
	
	private List<List<HashMap<Variable, Constant>>> generateExamples(FOIL algo) {
		List<HashMap<Variable, Constant>> positiveExamples = new ArrayList<>();
		List<HashMap<Variable, Constant>> negativeExamples = new ArrayList<>();
		for (List<String> list : PermutationGenerator.product(new ArrayList<>(algo.getKnowledgeBase().domain.getConstants()), new ArrayList<>(algo.getKnowledgeBase().domain.getConstants()))) {
			HashMap<Variable, Constant> temp = new HashMap<>();
			temp.put(x, new Constant(list.get(0)));
			temp.put(y, new Constant(list.get(1)));
			negativeExamples.add(new HashMap<>(temp));
		}
		//positive examples
		HashMap<Variable, Constant> temp = new HashMap<>();
		temp.put(x, new Constant("George"));
		temp.put(y, new Constant("Charles"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("George"));
		temp.put(y, new Constant("Anne"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("George"));
		temp.put(y, new Constant("Andrew"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("George"));
		temp.put(y, new Constant("Edward"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Spencer"));
		temp.put(y, new Constant("William"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Spencer"));
		temp.put(y, new Constant("Harry"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Philip"));
		temp.put(y, new Constant("William"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Philip"));
		temp.put(y, new Constant("Harry"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Philip"));
		temp.put(y, new Constant("Peter"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Philip"));
		temp.put(y, new Constant("Zara"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Philip"));
		temp.put(y, new Constant("Beatrice"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Philip"));
		temp.put(y, new Constant("Eugenie"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Philip"));
		temp.put(y, new Constant("Louise"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		temp.clear();
		temp.put(x, new Constant("Philip"));
		temp.put(y, new Constant("James"));
		negativeExamples.remove(new HashMap<>(temp));
		positiveExamples.add(new HashMap<>(temp));
		
		List<List<HashMap<Variable, Constant>>> examples = new ArrayList<>();
		examples.add(new ArrayList<>(positiveExamples));
		examples.add(new ArrayList<>(negativeExamples));
		
		return examples;
	}
}
