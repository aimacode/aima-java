package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.CNF;
import aima.logic.fol.kb.data.DefiniteClause;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.ast.Predicate;
import aima.logic.fol.parsing.ast.Term;
import aima.logic.fol.parsing.ast.Variable;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public class FOLKnowledgeBaseTest extends TestCase {

	private FOLKnowledgeBase weaponsKB, kingsKB;

	@Override
	public void setUp() {
		weaponsKB = new FOLKnowledgeBase(DomainFactory.weaponsDomain());

		kingsKB = new FOLKnowledgeBase(DomainFactory.kingsDomain());
	}

	public void testAddRuleAndFact() {
		weaponsKB.tell("(Missile(x) => Weapon(x))");
		assertEquals(1, weaponsKB.getNumberRules());
		weaponsKB.tell("American(West)");
		assertEquals(1, weaponsKB.getNumberRules());
		assertEquals(1, weaponsKB.getNumberFacts());
	}

	public void testAddComplexRule() {
		weaponsKB
				.tell("( (((American(x) AND Weapon(y)) AND Sells(x,y,z)) AND Hostile(z)) => Criminal(x))");
		assertEquals(1, weaponsKB.getNumberRules());
		weaponsKB.tell("American(West)");
		assertEquals(1, weaponsKB.getNumberRules());

		CNF cnfDC = weaponsKB.getCNFsOfOriginalSentences().get(0);
		assertNotNull(cnfDC);
		assertEquals(1, cnfDC.getNumberOfClauses());
		assertEquals(true, cnfDC.isDefiniteClause());
		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("v0"));
		assertEquals(new Predicate("Criminal", terms), cnfDC
				.getConjunctionOfClauses().get(0).getPositiveLiterals().get(0));

		DefiniteClause dcRule = weaponsKB.getAllDefiniteClauseImplications()
				.get(0);
		assertNotNull(dcRule);
		assertEquals(true, dcRule.isImplication());
		assertEquals(new Predicate("Criminal", terms), dcRule.getConclusion());
	}

	public void testFactNotAddedWhenAlreadyPresent() {
		kingsKB.tell("((King(x) AND Greedy(x)) => Evil(x))");
		kingsKB.tell("King(John)");
		kingsKB.tell("King(Richard)");
		kingsKB.tell("Greedy(John)");

		assertEquals(1, kingsKB.getNumberRules());
		assertEquals(3, kingsKB.getNumberFacts());

		kingsKB.tell("King(John)");
		kingsKB.tell("King(Richard)");
		kingsKB.tell("Greedy(John)");

		assertEquals(1, kingsKB.getNumberRules());
		assertEquals(3, kingsKB.getNumberFacts());

		kingsKB.tell("(((King(John))))");
		kingsKB.tell("(((King(Richard))))");
		kingsKB.tell("(((Greedy(John))))");

		assertEquals(1, kingsKB.getNumberRules());
		assertEquals(3, kingsKB.getNumberFacts());		
	}
}