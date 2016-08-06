package aima.gui.demo.logic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.logic.fol.CNFConverter;
import aima.core.logic.fol.StandardizeApartIndexicalFactory;
import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.domain.DomainFactory;
import aima.core.logic.fol.domain.FOLDomain;
import aima.core.logic.fol.inference.Demodulation;
import aima.core.logic.fol.inference.FOLBCAsk;
import aima.core.logic.fol.inference.FOLFCAsk;
import aima.core.logic.fol.inference.FOLModelElimination;
import aima.core.logic.fol.inference.FOLOTTERLikeTheoremProver;
import aima.core.logic.fol.inference.FOLTFMResolution;
import aima.core.logic.fol.inference.InferenceProcedure;
import aima.core.logic.fol.inference.InferenceResult;
import aima.core.logic.fol.inference.Paramodulation;
import aima.core.logic.fol.inference.proof.Proof;
import aima.core.logic.fol.inference.proof.ProofPrinter;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.core.logic.fol.kb.data.CNF;
import aima.core.logic.fol.kb.data.Clause;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.FOLParser;
import aima.core.logic.fol.parsing.ast.AtomicSentence;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Sentence;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.TermEquality;
import aima.core.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class FolDemo {
	public static void main(String[] args) {
		unifierDemo();
		fOL_fcAskDemo();
		fOL_bcAskDemo();
		fOL_CNFConversion();
		fOL_TFMResolutionDemo();
		fOL_Demodulation();
		fOL_Paramodulation();
		fOL_OTTERDemo();
		fOL_ModelEliminationDemo();
	}

	private static void unifierDemo() {
		FOLParser parser = new FOLParser(DomainFactory.knowsDomain());
		Unifier unifier = new Unifier();
		Map<Variable, Term> theta = new Hashtable<Variable, Term>();

		Sentence query = parser.parse("Knows(John,x)");
		Sentence johnKnowsJane = parser.parse("Knows(y,Mother(y))");

		System.out.println("------------");
		System.out.println("Unifier Demo");
		System.out.println("------------");
		Map<Variable, Term> subst = unifier.unify(query, johnKnowsJane, theta);
		System.out.println("Unify '" + query + "' with '" + johnKnowsJane
				+ "' to get the substitution " + subst + ".");
		System.out.println("");
	}

	private static void fOL_fcAskDemo() {
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Kings Demo 1");
		System.out.println("---------------------------");
		kingsDemo1(new FOLFCAsk());
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Kings Demo 2");
		System.out.println("---------------------------");
		kingsDemo2(new FOLFCAsk());
		System.out.println("---------------------------");
		System.out.println("Forward Chain, Weapons Demo");
		System.out.println("---------------------------");
		weaponsDemo(new FOLFCAsk());
	}

	private static void fOL_bcAskDemo() {
		System.out.println("----------------------------");
		System.out.println("Backward Chain, Kings Demo 1");
		System.out.println("----------------------------");
		kingsDemo1(new FOLBCAsk());
		System.out.println("----------------------------");
		System.out.println("Backward Chain, Kings Demo 2");
		System.out.println("----------------------------");
		kingsDemo2(new FOLBCAsk());
		System.out.println("----------------------------");
		System.out.println("Backward Chain, Weapons Demo");
		System.out.println("----------------------------");
		weaponsDemo(new FOLBCAsk());
	}

	private static void fOL_CNFConversion() {
		System.out.println("-------------------------------------------------");
		System.out.println("Conjuctive Normal Form for First Order Logic Demo");
		System.out.println("-------------------------------------------------");
		FOLDomain domain = DomainFactory.lovesAnimalDomain();
		FOLParser parser = new FOLParser(domain);

		Sentence origSentence = parser
				.parse("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");

		CNFConverter cnfConv = new CNFConverter(parser);

		CNF cnf = cnfConv.convertToCNF(origSentence);

		System.out.println("Convert '" + origSentence + "' to CNF.");
		System.out.println("CNF=" + cnf.toString());
		System.out.println("");
	}

	private static void fOL_TFMResolutionDemo() {
		System.out.println("----------------------------");
		System.out.println("TFM Resolution, Kings Demo 1");
		System.out.println("----------------------------");
		kingsDemo1(new FOLTFMResolution());
		System.out.println("----------------------------");
		System.out.println("TFM Resolution, Kings Demo 2");
		System.out.println("----------------------------");
		kingsDemo2(new FOLTFMResolution());
		System.out.println("----------------------------");
		System.out.println("TFM Resolution, Weapons Demo");
		System.out.println("----------------------------");
		weaponsDemo(new FOLTFMResolution());
		System.out.println("---------------------------------");
		System.out.println("TFM Resolution, Loves Animal Demo");
		System.out.println("---------------------------------");
		lovesAnimalDemo(new FOLTFMResolution());
		System.out.println("---------------------------------------");
		System.out.println("TFM Resolution, ABC Equality Axiom Demo");
		System.out.println("---------------------------------------");
		abcEqualityAxiomDemo(new FOLTFMResolution());
	}

	private static void fOL_Demodulation() {
		System.out.println("-----------------");
		System.out.println("Demodulation Demo");
		System.out.println("-----------------");
		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addConstant("C");
		domain.addConstant("D");
		domain.addConstant("E");
		domain.addPredicate("P");
		domain.addFunction("F");
		domain.addFunction("G");
		domain.addFunction("H");
		domain.addFunction("J");

		FOLParser parser = new FOLParser(domain);

		Predicate expression = (Predicate) parser
				.parse("P(A,F(B,G(A,H(B)),C),D)");
		TermEquality assertion = (TermEquality) parser.parse("B = E");

		Demodulation demodulation = new Demodulation();
		Predicate altExpression = (Predicate) demodulation.apply(assertion,
				expression);

		System.out.println("Demodulate '" + expression + "' with '" + assertion
				+ "' to give");
		System.out.println(altExpression.toString());
		System.out.println("and again to give");
		System.out.println(demodulation.apply(assertion, altExpression)
				.toString());
		System.out.println("");
	}

	private static void fOL_Paramodulation() {
		System.out.println("-------------------");
		System.out.println("Paramodulation Demo");
		System.out.println("-------------------");

		FOLDomain domain = new FOLDomain();
		domain.addConstant("A");
		domain.addConstant("B");
		domain.addPredicate("P");
		domain.addPredicate("Q");
		domain.addPredicate("R");
		domain.addFunction("F");

		FOLParser parser = new FOLParser(domain);

		List<Literal> lits = new ArrayList<Literal>();
		AtomicSentence a1 = (AtomicSentence) parser.parse("P(F(x,B),x)");
		AtomicSentence a2 = (AtomicSentence) parser.parse("Q(x)");
		lits.add(new Literal(a1));
		lits.add(new Literal(a2));

		Clause c1 = new Clause(lits);

		lits.clear();
		a1 = (AtomicSentence) parser.parse("F(A,y) = y");
		a2 = (AtomicSentence) parser.parse("R(y)");
		lits.add(new Literal(a1));
		lits.add(new Literal(a2));

		Clause c2 = new Clause(lits);

		Paramodulation paramodulation = new Paramodulation();
		Set<Clause> paras = paramodulation.apply(c1, c2);

		System.out.println("Paramodulate '" + c1 + "' with '" + c2
				+ "' to give");
		System.out.println(paras.toString());
		System.out.println("");
	}

	private static void fOL_OTTERDemo() {
		System.out.println("---------------------------------------");
		System.out.println("OTTER Like Theorem Prover, Kings Demo 1");
		System.out.println("---------------------------------------");
		kingsDemo1(new FOLOTTERLikeTheoremProver());
		System.out.println("---------------------------------------");
		System.out.println("OTTER Like Theorem Prover, Kings Demo 2");
		System.out.println("---------------------------------------");
		kingsDemo2(new FOLOTTERLikeTheoremProver());
		System.out.println("---------------------------------------");
		System.out.println("OTTER Like Theorem Prover, Weapons Demo");
		System.out.println("---------------------------------------");
		weaponsDemo(new FOLOTTERLikeTheoremProver());
		System.out.println("--------------------------------------------");
		System.out.println("OTTER Like Theorem Prover, Loves Animal Demo");
		System.out.println("--------------------------------------------");
		lovesAnimalDemo(new FOLOTTERLikeTheoremProver());
		System.out
				.println("--------------------------------------------------");
		System.out
				.println("OTTER Like Theorem Prover, ABC Equality Axiom Demo");
		System.out
				.println("--------------------------------------------------");
		abcEqualityAxiomDemo(new FOLOTTERLikeTheoremProver(false));
		System.out
				.println("-----------------------------------------------------");
		System.out
				.println("OTTER Like Theorem Prover, ABC Equality No Axiom Demo");
		System.out
				.println("-----------------------------------------------------");
		abcEqualityNoAxiomDemo(new FOLOTTERLikeTheoremProver(true));
	}

	private static void fOL_ModelEliminationDemo() {
		System.out.println("-------------------------------");
		System.out.println("Model Elimination, Kings Demo 1");
		System.out.println("-------------------------------");
		kingsDemo1(new FOLModelElimination());
		System.out.println("-------------------------------");
		System.out.println("Model Elimination, Kings Demo 2");
		System.out.println("-------------------------------");
		kingsDemo2(new FOLModelElimination());
		System.out.println("-------------------------------");
		System.out.println("Model Elimination, Weapons Demo");
		System.out.println("-------------------------------");
		weaponsDemo(new FOLModelElimination());
		System.out.println("------------------------------------");
		System.out.println("Model Elimination, Loves Animal Demo");
		System.out.println("------------------------------------");
		lovesAnimalDemo(new FOLModelElimination());
		System.out.println("------------------------------------------");
		System.out.println("Model Elimination, ABC Equality Axiom Demo");
		System.out.println("-------------------------------------------");
		abcEqualityAxiomDemo(new FOLModelElimination());
	}

	private static void kingsDemo1(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = FOLKnowledgeBaseFactory
				.createKingsKnowledgeBase(ip);

		String kbStr = kb.toString();

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("John"));
		Predicate query = new Predicate("Evil", terms);

		InferenceResult answer = kb.ask(query);

		System.out.println("Kings Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
	}

	private static void kingsDemo2(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = FOLKnowledgeBaseFactory
				.createKingsKnowledgeBase(ip);

		String kbStr = kb.toString();

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("King", terms);

		InferenceResult answer = kb.ask(query);

		System.out.println("Kings Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
		}
	}

	private static void weaponsDemo(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = FOLKnowledgeBaseFactory
				.createWeaponsKnowledgeBase(ip);

		String kbStr = kb.toString();

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("x"));
		Predicate query = new Predicate("Criminal", terms);

		InferenceResult answer = kb.ask(query);

		System.out.println("Weapons Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
	}

	private static void lovesAnimalDemo(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = FOLKnowledgeBaseFactory
				.createLovesAnimalKnowledgeBase(ip);

		String kbStr = kb.toString();

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Constant("Curiosity"));
		terms.add(new Constant("Tuna"));
		Predicate query = new Predicate("Kills", terms);

		InferenceResult answer = kb.ask(query);

		System.out.println("Loves Animal Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
	}

	private static void abcEqualityAxiomDemo(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = FOLKnowledgeBaseFactory
				.createABCEqualityKnowledgeBase(ip, true);

		String kbStr = kb.toString();

		TermEquality query = new TermEquality(new Constant("A"), new Constant(
				"C"));

		InferenceResult answer = kb.ask(query);

		System.out.println("ABC Equality Axiom Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
	}

	private static void abcEqualityNoAxiomDemo(InferenceProcedure ip) {
		StandardizeApartIndexicalFactory.flush();

		FOLKnowledgeBase kb = FOLKnowledgeBaseFactory
				.createABCEqualityKnowledgeBase(ip, false);

		String kbStr = kb.toString();

		TermEquality query = new TermEquality(new Constant("A"), new Constant(
				"C"));

		InferenceResult answer = kb.ask(query);

		System.out.println("ABC Equality No Axiom Knowledge Base:");
		System.out.println(kbStr);
		System.out.println("Query: " + query);
		for (Proof p : answer.getProofs()) {
			System.out.print(ProofPrinter.printProof(p));
			System.out.println("");
		}
	}
}