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
}
