package aima.test.unit.logic.propositional.parsing;

import org.junit.Test;

import aima.core.logic.basic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.basic.propositional.parsing.ast.Connective;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;

public class ComplexSentenceTest {
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_1() {
		new ComplexSentence(null, new Sentence[] {new PropositionSymbol("A"), new PropositionSymbol("B")});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_2() {
		new ComplexSentence(Connective.NOT, (Sentence[]) null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_3() {
		new ComplexSentence(Connective.NOT, new Sentence[]{});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_4() {
		new ComplexSentence(Connective.NOT, new Sentence[] {new PropositionSymbol("A"), new PropositionSymbol("B")});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_5() {
		new ComplexSentence(Connective.AND, new Sentence[]{new PropositionSymbol("A")});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_6() {
		new ComplexSentence(Connective.AND, new Sentence[]{new PropositionSymbol("A"), new PropositionSymbol("B"), new PropositionSymbol("C")});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_7() {
		new ComplexSentence(Connective.OR, new Sentence[]{new PropositionSymbol("A")});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_8() {
		new ComplexSentence(Connective.OR, new Sentence[]{new PropositionSymbol("A"), new PropositionSymbol("B"), new PropositionSymbol("C")});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_9() {
		new ComplexSentence(Connective.IMPLICATION, new Sentence[]{new PropositionSymbol("A")});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_10() {
		new ComplexSentence(Connective.IMPLICATION, new Sentence[]{new PropositionSymbol("A"), new PropositionSymbol("B"), new PropositionSymbol("C")});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_11() {
		new ComplexSentence(Connective.BICONDITIONAL, new Sentence[]{new PropositionSymbol("A")});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_IllegalArgumentOnConstruction_12() {
		new ComplexSentence(Connective.BICONDITIONAL, new Sentence[]{new PropositionSymbol("A"), new PropositionSymbol("B"), new PropositionSymbol("C")});
	}
}