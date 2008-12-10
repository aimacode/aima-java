/*
 * Created on Sep 22, 2004
 *
 */
package aima.logic.fol.demos;

import java.util.ArrayList;
import java.util.List;

import aima.logic.fol.StandardizeApartIndexicalFactory;
import aima.logic.fol.inference.FOLBCAsk;
import aima.logic.fol.inference.FOLFCAsk;
import aima.logic.fol.inference.FOLOTTERLikeTheoremProver;
import aima.logic.fol.inference.FOLTFMResolution;
import aima.logic.fol.inference.InferenceProcedure;
import aima.logic.fol.inference.InferenceResult;
import aima.logic.fol.inference.proof.Proof;
import aima.logic.fol.inference.proof.ProofPrinter;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.logic.fol.parsing.ast.Constant;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.TermEquality;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class FolDemo {
	public static void main(String[] args) {
		substDemo();
		unifierDemo();
		fOL_fcAskDemo();
		fOL_bcAskDemo();
		fOL_TFMResolutionDemo();
		fOL_OTTERDemo();
	}

	private static void substDemo() {
		// TODO write this ?
	}

	private static void unifierDemo() {
		// TODO write this
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