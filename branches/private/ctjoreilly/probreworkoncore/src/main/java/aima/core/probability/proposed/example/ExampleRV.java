package aima.core.probability.proposed.example;

import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.ArbitraryTokenDomain;
import aima.core.probability.proposed.model.domain.BooleanDomain;
import aima.core.probability.proposed.model.domain.FiniteIntegerDomain;

public class ExampleRV {
	//
	public static final RandomVariable DICE_1_RV = new RandomVariable("Dice1",
			new FiniteIntegerDomain(1, 2, 3, 4, 5, 6));
	public static final RandomVariable DICE_2_RV = new RandomVariable("Dice2",
			new FiniteIntegerDomain(1, 2, 3, 4, 5, 6));
	//
	public static final RandomVariable TOOTHACHE_RV = new RandomVariable(
			"Toothache", new BooleanDomain());
	public static final RandomVariable CAVITY_RV = new RandomVariable("Cavity",
			new BooleanDomain());
	public static final RandomVariable CATCH_RV = new RandomVariable("Catch",
			new BooleanDomain());
	//
	public static final RandomVariable WEATHER_RV = new RandomVariable(
			"Weather", new ArbitraryTokenDomain("sunny", "rain", "cloudy",
					"snow"));
	//
	public static final RandomVariable MENINGITIS_RV = new RandomVariable(
			"Meningitis", new BooleanDomain());
	public static final RandomVariable STIFF_NECK_RV = new RandomVariable(
			"StiffNeck", new BooleanDomain());
	//
	public static final RandomVariable BURGLARY_RV = new RandomVariable(
			"Burglary", new BooleanDomain());
	public static final RandomVariable EARTHQUAKE_RV = new RandomVariable(
			"Earthquake", new BooleanDomain());
	public static final RandomVariable ALARM_RV = new RandomVariable("Alarm",
			new BooleanDomain());
	public static final RandomVariable JOHN_CALLS_RV = new RandomVariable(
			"JohnCalls", new BooleanDomain());
	public static final RandomVariable MARY_CALLS_RV = new RandomVariable(
			"MaryCalls", new BooleanDomain());
}
