package aima.test.logictest.foltest;

import aima.logic.fol.CNFConverter;
import aima.logic.fol.domain.FOLDomain;
import aima.logic.fol.kb.data.CNF;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Sentence;
import junit.framework.TestCase;

/**
 * @author Ciaran O'Reilly
 * 
 */
// TODO : more tests!
public class CNFConverterTest extends TestCase {

	public void testExamplePg295() {
		FOLDomain domain = DomainFactory.weaponsDomain();
		FOLParser parser = new FOLParser(domain);

		Sentence origSentence = parser
				.parse("FORALL x ((((American(x) AND Weapon(y)) AND Sells(x, y, z)) AND Hostile(z)) => Criminal(x))");

		CNFConverter cnfConv = new CNFConverter(parser);

		CNF cnf = cnfConv.convertToCNF(origSentence);

		assertEquals(
				"[NOT( American( x ) ) OR NOT( Weapon( y ) ) OR NOT( Sells( x,y,z ) ) OR NOT( Hostile( z ) ) OR  Criminal( x ) ]",
				cnf.toString());
	}

	public void testExamplePg296() {
		FOLDomain domain = DomainFactory.lovesAnimalDomain();
		FOLParser parser = new FOLParser(domain);

		Sentence origSentence = parser
				.parse("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");

		CNFConverter cnfConv = new CNFConverter(parser);

		CNF cnf = cnfConv.convertToCNF(origSentence);

		assertEquals(
				"[ Animal(  SF0( x ) )  OR  Loves(  SF1( x ),x ) ] AND [NOT( Loves( x, SF0( x ) ) ) OR  Loves(  SF1( x ),x ) ]",
				cnf.toString());
	}

	public void testExamplesPg299() {
		FOLDomain domain = DomainFactory.lovesAnimalDomain();
		FOLParser parser = new FOLParser(domain);

		// FOL A.
		Sentence origSentence = parser
				.parse("FORALL x (FORALL y (Animal(y) => Loves(x, y)) => EXISTS y Loves(y, x))");

		CNFConverter cnfConv = new CNFConverter(parser);

		CNF cnf = cnfConv.convertToCNF(origSentence);

		// CNF A1. and A2.
		assertEquals(
				"[ Animal(  SF0( x ) )  OR  Loves(  SF1( x ),x ) ] AND [NOT( Loves( x, SF0( x ) ) ) OR  Loves(  SF1( x ),x ) ]",
				cnf.toString());

		// FOL B.
		origSentence = parser
				.parse("FORALL x (EXISTS y (Animal(y) AND Kills(x, y)) => FORALL z NOT(Loves(z, x)))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF B.
		assertEquals(
				"[NOT( Animal( y ) ) OR NOT( Kills( x,y ) ) OR NOT( Loves( z,x ) )]",
				cnf.toString());

		// FOL C.
		origSentence = parser.parse("FORALL x (Animal(x) => Loves(Jack, x))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF C.
		assertEquals("[NOT( Animal( x ) ) OR  Loves( Jack,x ) ]", cnf
				.toString());
		
		// FOL D.
		origSentence = parser.parse("(Kills(Jack, Tuna) OR Kills(Curiosity, Tuna))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF D.
		assertEquals("[ Kills( Jack,Tuna )  OR  Kills( Curiosity,Tuna ) ]", cnf
				.toString());
		
		// FOL E.
		origSentence = parser.parse("Cat(Tuna)");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF E.
		assertEquals("[ Cat( Tuna ) ]", cnf
				.toString());
		
		// FOL F.
		origSentence = parser.parse("FORALL x (Cat(x) => Animal(x))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF F.
		assertEquals("[NOT( Cat( x ) ) OR  Animal( x ) ]", cnf
				.toString());
		
		// FOL G.
		origSentence = parser.parse("NOT(Kills(Curiosity, Tuna))");

		cnf = cnfConv.convertToCNF(origSentence);

		// CNF G.
		assertEquals("[NOT( Kills( Curiosity,Tuna ) )]", cnf
				.toString());
	}
}
