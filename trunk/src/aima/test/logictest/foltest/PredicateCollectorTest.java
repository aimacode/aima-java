package aima.test.logictest.foltest;

import java.util.List;

import junit.framework.TestCase;
import aima.logic.fol.PredicateCollector;
import aima.logic.fol.parsing.DomainFactory;
import aima.logic.fol.parsing.FOLParser;
import aima.logic.fol.parsing.ast.Sentence;

/**
 * @author Ravi Mohan
 * 
 */

public class PredicateCollectorTest extends TestCase {
	PredicateCollector collector;

	FOLParser parser;

	@Override
	public void setUp() {
		collector = new PredicateCollector();
		parser = new FOLParser(DomainFactory.weaponsDomain());
	}

	public void testSimpleSentence() {
		Sentence s = parser.parse("(Missile(x) => Weapon(x))");
		List predicates = collector.getPredicates(s);
		assertNotNull(predicates);
	}

}
