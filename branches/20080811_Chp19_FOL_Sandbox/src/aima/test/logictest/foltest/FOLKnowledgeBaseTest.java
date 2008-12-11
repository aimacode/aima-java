package aima.test.logictest.foltest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.logic.fol.StandardizeApartIndexicalFactory;
import aima.logic.fol.domain.DomainFactory;
import aima.logic.fol.kb.FOLKnowledgeBase;
import aima.logic.fol.kb.data.Clause;
import aima.logic.fol.kb.data.Literal;
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
		StandardizeApartIndexicalFactory.flush();
		
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

		List<Term> terms = new ArrayList<Term>();
		terms.add(new Variable("v0"));

		Clause dcRule = weaponsKB.getAllDefiniteClauseImplications().get(0);
		assertNotNull(dcRule);
		assertEquals(true, dcRule.isImplicationDefiniteClause());
		assertEquals(new Literal(new Predicate("Criminal", terms)), dcRule
				.getPositiveLiterals().get(0));
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