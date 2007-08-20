package aima.test.coretest;

import junit.framework.TestCase;
import aima.basic.Percept;
import aima.basic.simplerule.ANDCondition;
import aima.basic.simplerule.EQUALCondition;
import aima.basic.simplerule.NOTCondition;
import aima.basic.simplerule.ORCondition;
import aima.basic.simplerule.Rule;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class RuleTest extends TestCase {

	public void testEQUALRule() {
		Rule r = new Rule(
				new EQUALCondition("car-in-front-is-braking", "true"),
				"initiate-braking");

		assertEquals("initiate-braking", r.getAction());

		assertEquals("if car-in-front-is-braking==true then initiate-braking.",
				r.toString());

		assertEquals(true, r.evaluate(new Percept("car-in-front-is-braking",
				"true")));

		assertEquals(false, r.evaluate(new Percept("car-in-front-is-braking",
				"false")));

		assertEquals(false, r.evaluate(new Percept(
				"car-in-front-is-indicating", "true")));
	}

	public void testNOTRule() {
		Rule r = new Rule(new NOTCondition(new EQUALCondition(
				"car-in-front-is-braking", "true")), "initiate-braking");

		assertEquals("initiate-braking", r.getAction());

		assertEquals(
				"if ![car-in-front-is-braking==true] then initiate-braking.", r
						.toString());

		assertEquals(false, r.evaluate(new Percept("car-in-front-is-braking",
				"true")));

		assertEquals(true, r.evaluate(new Percept("car-in-front-is-braking",
				"false")));

		assertEquals(true, r.evaluate(new Percept("car-in-front-is-indicating",
				"true")));
	}

	public void testANDRule() {
		Rule r = new Rule(new ANDCondition(new EQUALCondition(
				"car-in-front-is-braking", "true"), new EQUALCondition(
				"car-in-front-tires-smoking", "true")), "emergency-braking");

		assertEquals("emergency-braking", r.getAction());

		assertEquals(
				"if [car-in-front-is-braking==true && car-in-front-tires-smoking==true] then emergency-braking.",
				r.toString());

		assertEquals(false, r.evaluate(new Percept("car-in-front-is-braking",
				"true")));

		assertEquals(false, r.evaluate(new Percept(
				"car-in-front-tires-smoking", "true")));

		assertEquals(true, r.evaluate(new Percept("car-in-front-is-braking",
				"true", "car-in-front-tires-smoking", "true")));

		assertEquals(false, r.evaluate(new Percept("car-in-front-is-braking",
				"false", "car-in-front-tires-smoking", "true")));

		assertEquals(false, r.evaluate(new Percept("car-in-front-is-braking",
				"true", "car-in-front-tires-smoking", "false")));
	}

	public void testORRule() {
		Rule r = new Rule(new ORCondition(new EQUALCondition(
				"car-in-front-is-braking", "true"), new EQUALCondition(
				"car-in-front-tires-smoking", "true")), "emergency-braking");

		assertEquals("emergency-braking", r.getAction());

		assertEquals(
				"if [car-in-front-is-braking==true || car-in-front-tires-smoking==true] then emergency-braking.",
				r.toString());

		assertEquals(true, r.evaluate(new Percept("car-in-front-is-braking",
				"true")));

		assertEquals(true, r.evaluate(new Percept("car-in-front-tires-smoking",
				"true")));

		assertEquals(true, r.evaluate(new Percept("car-in-front-is-braking",
				"true", "car-in-front-tires-smoking", "true")));

		assertEquals(true, r.evaluate(new Percept("car-in-front-is-braking",
				"false", "car-in-front-tires-smoking", "true")));

		assertEquals(true, r.evaluate(new Percept("car-in-front-is-braking",
				"true", "car-in-front-tires-smoking", "false")));

		assertEquals(false, r.evaluate(new Percept("car-in-front-is-braking",
				"false", "car-in-front-tires-smoking", "false")));
	}
}