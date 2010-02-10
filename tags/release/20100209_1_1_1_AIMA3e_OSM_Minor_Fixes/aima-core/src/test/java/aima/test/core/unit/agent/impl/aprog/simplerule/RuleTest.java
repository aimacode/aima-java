package aima.test.core.unit.agent.impl.aprog.simplerule;

import org.junit.Assert;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.agent.impl.DynamicAction;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.aprog.simplerule.ANDCondition;
import aima.core.agent.impl.aprog.simplerule.EQUALCondition;
import aima.core.agent.impl.aprog.simplerule.NOTCondition;
import aima.core.agent.impl.aprog.simplerule.ORCondition;
import aima.core.agent.impl.aprog.simplerule.Rule;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class RuleTest {

	private static final Action ACTION_INITIATE_BRAKING = new DynamicAction(
			"initiate-braking");
	private static final Action ACTION_EMERGENCY_BRAKING = new DynamicAction(
			"emergency-braking");
	//
	private static final String ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING = "car-in-front-is-braking";
	private static final String ATTRIBUTE_CAR_IN_FRONT_IS_INDICATING = "car-in-front-is-indicating";
	private static final String ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING = "car-in-front-tires-smoking";

	@Test
	public void testEQUALRule() {
		Rule r = new Rule(new EQUALCondition(ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING,
				true), ACTION_INITIATE_BRAKING);

		Assert.assertEquals(ACTION_INITIATE_BRAKING, r.getAction());

		Assert
				.assertEquals(
						"if car-in-front-is-braking==true then Action[name==initiate-braking].",
						r.toString());

		Assert.assertEquals(true, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)));

		Assert.assertEquals(false, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false)));

		Assert.assertEquals(false, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_INDICATING, true)));
	}

	@Test
	public void testNOTRule() {
		Rule r = new Rule(new NOTCondition(new EQUALCondition(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)),
				ACTION_INITIATE_BRAKING);

		Assert.assertEquals(ACTION_INITIATE_BRAKING, r.getAction());

		Assert
				.assertEquals(
						"if ![car-in-front-is-braking==true] then Action[name==initiate-braking].",
						r.toString());

		Assert.assertEquals(false, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)));

		Assert.assertEquals(true, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false)));

		Assert.assertEquals(true, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_INDICATING, true)));
	}

	@Test
	public void testANDRule() {
		Rule r = new Rule(new ANDCondition(new EQUALCondition(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true), new EQUALCondition(
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)),
				ACTION_EMERGENCY_BRAKING);

		Assert.assertEquals(ACTION_EMERGENCY_BRAKING, r.getAction());

		Assert
				.assertEquals(
						"if [car-in-front-is-braking==true && car-in-front-tires-smoking==true] then Action[name==emergency-braking].",
						r.toString());

		Assert.assertEquals(false, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)));

		Assert.assertEquals(false, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(true, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(false, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(false, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, false)));
	}

	@Test
	public void testORRule() {
		Rule r = new Rule(new ORCondition(new EQUALCondition(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true), new EQUALCondition(
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)),
				ACTION_EMERGENCY_BRAKING);

		Assert.assertEquals(ACTION_EMERGENCY_BRAKING, r.getAction());

		Assert
				.assertEquals(
						"if [car-in-front-is-braking==true || car-in-front-tires-smoking==true] then Action[name==emergency-braking].",
						r.toString());

		Assert.assertEquals(true, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true)));

		Assert.assertEquals(true, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(true, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(true, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, true)));

		Assert.assertEquals(true, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, true,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, false)));

		Assert.assertEquals(false, r.evaluate(new DynamicPercept(
				ATTRIBUTE_CAR_IN_FRONT_IS_BRAKING, false,
				ATTRIBUTE_CAR_IN_FRONT_TIRES_SMOKING, false)));
	}
}