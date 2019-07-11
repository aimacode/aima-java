package aima.core.learning.knowledge;

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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 19.12, page
 * 807.<br>
 * <br>
 * <pre>
 * function FOIL(examples, target) returns a set of Horn clauses
 * 	 inputs: examples, set of examples
 * 	 		 target, a literal for the goal predicate
 * 	 local variables: clauses, set of clauses, initially empty
 *
 *	 while examples contains positive examples do
 *	 	clause <- NEW-CLAUSE(examples, target)
 *	 	remove positive examples covered by clause from examples
 *      add clause to clauses
 *   return clauses
 *
 * function NEW-CLAUSE(examples, target) returns a Horn clause
 * 	 local variables: clause, a clause with target as head and an empty body
 * 	 				  l, a literal to be added to the clause
 * 	 				  extended_examples, a set of examples with the values for new variables
 *
 * 	 extended_examples <- examples
 * 	 while extended_examples contains negative examples do
 * 	 	l <- CHOOSE-LITERAL(NEW-LITERALS(clause), extended_examples)
 * 	 	append l to the body of clause
 * 	 	extended_examples <- set of examples created by applying EXTEND-EXAMPLE
 * 	 		to each example in extended_examples
 *	 return clause
 *
 * function EXTEND-EXAMPLE(example, literal) returns a set of examples
 * 	 if example satisfies literal
 * 	 	then return the set of examples created by extending set of examples with
 * 	 		each possible constant value for each new variable in literal
 * 	 else return the empty set
 * </pre>
 * <p>
 * Figure 19.12 Sketch of the FOIL algorithm for learning sets of first-order Horn clauses
 * from examples.
 *
 * @author Ciaran O'Reilly
 * @author samagra
 * @author Suyash Jain
 */


public class FOIL {
	private FOLKnowledgeBase kb;
	
	private HashSet<Clause> foil(List<List<HashMap<Variable, Constant>>> examples, Literal target) {
		HashSet<Clause> clauses = new HashSet<>();
		while (!examples.get(0).isEmpty()) {
			Clause clause = newClause(examples, target);
			// remove positive examples covered by clause from examples
			for (HashMap<Variable, Constant> example : new ArrayList<>(examples.get(0))) {
				boolean covered = check(clause, example, target);
				if (covered) {
					examples.get(0).remove(example);
				}
			}
			// add clause to clauses
			clauses.add(clause);
		}
		return clauses;
	}
	
	private Clause newClause(List<List<HashMap<Variable, Constant>>> examples, Literal target) {
		Clause clause = new Clause(new ArrayList<>(Collections.singletonList(target)));
		Literal l;
		List<List<HashMap<Variable, Constant>>> extendedExamples = new ArrayList<>();
		extendedExamples.add(0, new ArrayList<>());
		extendedExamples.add(1, new ArrayList<>());
		for (HashMap<Variable, Constant> h : examples.get(0)) {
			HashMap<Variable, Constant> hashMap = new HashMap<>(h);
			extendedExamples.get(0).add(hashMap);
		}
		for (HashMap<Variable, Constant> h : examples.get(1)) {
			HashMap<Variable, Constant> hashMap = new HashMap<>(h);
			extendedExamples.get(1).add(hashMap);
		}
		while (!extendedExamples.get(1).isEmpty()) {
			l = chooseLiteral(newLiterals(clause), extendedExamples);
			clause.addLiteral(l);
			List<HashMap<Variable, Constant>> allPosExamples = new ArrayList<>(extendedExamples.get(0));
			extendedExamples.get(0).clear();
			for (HashMap<Variable, Constant> example : allPosExamples) {
				extendedExamples.get(0).addAll(extendExample(example, l));
			}
			List<HashMap<Variable, Constant>> allNegExamples = new ArrayList<>(extendedExamples.get(1));
			extendedExamples.get(1).clear();
			for (HashMap<Variable, Constant> example : allNegExamples) {
				extendedExamples.get(1).addAll(extendExample(example, l));
			}
		}
		return clause;
	}
	
	private List<HashMap<Variable, Constant>> extendExample(HashMap<Variable, Constant> example, Literal literal) {
		List<HashMap<Variable, Constant>> extended_examples = new ArrayList<>();
		Map<Variable, Term> tempEg = new HashMap<>(example);
		InferenceResult result = kb.ask(kb.subst(tempEg, literal).toString());
		if (result.isTrue()) {
			for (Proof proof : result.getProofs()) {
				Map<Variable, Term> tempCondition = proof.getAnswerBindings();
				HashMap<Variable, Constant> h = new HashMap<>(example);
				for (Variable v : tempCondition.keySet()) {
					h.put(v, new Constant(tempCondition.get(v).toString()));
				}
				extended_examples.add(h);
			}
		}
		return extended_examples;
	}
	
	/**
	 * function CHOOSE-LITERAL(NEW-LITERALS(clause), extended_example) returns a literal
	 *
	 * @param literals : list of literals returned by NEW-LITERALS method
	 * @param examples : set of examples
	 *
	 * <p>
	 *  CHOOSE-LITERAL uses the information gain heuristic to decide which literal to add.
	 *
	 *   I(C) = -log2(|T+|/|T|)
	 *   	where, |T| : number of tuples in the binding set of current clause C
	 *             |T+| : number of positive tuples in the binding set of current clause C.
	 *
	 *   Gain = d * (I(C) - I(C'))
	 *      where, d : number of current positive tuples covered by literal l
	 *             I(C) : Information gain for current clause C
	 *             I(C') : Information gain for clause C' generated by adding the new candidate literal l
	 * </p>
	 * @return literal l for which "Gain" is maximum
	*/
	
	private Literal chooseLiteral(List<Literal> literals, List<List<HashMap<Variable, Constant>>> examples) {
		Literal literal = null;
		double gain = Double.MIN_VALUE;
		
		for(Literal l : literals){
			
			List<List<HashMap<Variable, Constant>>> allExamples = new ArrayList<>();
			allExamples.add(0, new ArrayList<>());
			allExamples.add(1, new ArrayList<>());
			for (HashMap<Variable, Constant> h : examples.get(0)) {
				HashMap<Variable, Constant> hashMap = new HashMap<>(h);
				allExamples.get(0).add(hashMap);
			}
			for (HashMap<Variable, Constant> h : examples.get(1)) {
				HashMap<Variable, Constant> hashMap = new HashMap<>(h);
				allExamples.get(1).add(hashMap);
			}
			
			double t = allExamples.get(0).size() + allExamples.get(1).size();
			double tplus = allExamples.get(0).size();
			
			double  icurr = -((Math.log(tplus/t))/(Math.log(2)));
			
			List<List<HashMap<Variable, Constant>>> extendedExamples = new ArrayList<>();
			extendedExamples.add(0, new ArrayList<>());
			extendedExamples.add(1, new ArrayList<>());
			int d = 0;
			
			for (HashMap<Variable, Constant> example : allExamples.get(0)) {
				Map<Variable, Term> tempEg = new HashMap<>(example);
				InferenceResult result = kb.ask(kb.subst(tempEg, l).toString());
				if(result.isTrue()) d++;
				extendedExamples.get(0).addAll(extendExample(example, l));
			}
			for (HashMap<Variable, Constant> example : allExamples.get(1)) {
				extendedExamples.get(1).addAll(extendExample(example, l));
			}
			
			double tdash = extendedExamples.get(0).size() + extendedExamples.get(1).size();
			double tdashplus = extendedExamples.get(0).size();
			
			double inow = -((Math.log(tdashplus/tdash))/(Math.log(2)));
			
			double g = d * (icurr - inow);
			if (g >= gain) {
				gain = g;
				literal = l;
			}
		}
		return literal;
	}
	
	/**
	 * function NEW-LITERALS(clause) returns a set of new literals
	 *
	 * @param clause
	 * @return
	 *
	 * <p>
	 *  Constraint for adding new literal:
	 *  	each literal must include at least one variable from an earlier literal or from the head of the clause
	 * </p>
	 */
	
	private List<Literal> newLiterals(Clause clause) {
		HashSet<Literal> setOfLiterals = new HashSet<>();
		HashSet<Term> vars = new HashSet<>();
		for (Literal l : clause.getLiterals()) {
			vars.addAll(l.getAtomicSentence().getArgs());
		}
		for (String s : kb.getIndexFacts().keySet()) {
			Literal l = kb.getIndexFacts().get(s).get(0);
			int r = l.getAtomicSentence().getArgs().size();
			HashSet<Term> arg = new HashSet<>();
			arg.addAll(vars);
			int initialSize = arg.size();
			while (arg.size() < (initialSize + r - 1)) {
				arg.add(new Variable(Character.toString((char) (ThreadLocalRandom.current().nextInt(97, 123)))));
			}
			for (List<Term> variableList : PermutationGenerator.generatePermutations(new ArrayList<>(arg), r)) {
				Literal toAdd = new Literal(new Predicate(l.getAtomicSentence().getSymbolicName(), variableList), l.isNegativeLiteral());
				setOfLiterals.add(toAdd);
			}
		}
		return new ArrayList<>(setOfLiterals);
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
	
	public HashSet<Clause> apply(List<List<HashMap<Variable, Constant>>> examples, Literal target){
		return foil(examples, target);
	}
	
	public FOLKnowledgeBase getKnowledgeBase(){
		return kb;
	}
	
	public FOIL(FOLKnowledgeBase kb) {
		this.kb = kb;
	}
}
