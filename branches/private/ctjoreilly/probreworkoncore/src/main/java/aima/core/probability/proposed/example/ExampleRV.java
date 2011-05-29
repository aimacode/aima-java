package aima.core.probability.proposed.example;

import aima.core.probability.proposed.domain.ArbitraryTokenDomain;
import aima.core.probability.proposed.domain.BooleanDomain;
import aima.core.probability.proposed.domain.FiniteIntegerDomain;
import aima.core.probability.proposed.util.RandVar;

public class ExampleRV {
	//
	public static final RandVar DICE_1_RV = new RandVar("Dice1",
			new FiniteIntegerDomain(1, 2, 3, 4, 5, 6));
	public static final RandVar DICE_2_RV = new RandVar("Dice2",
			new FiniteIntegerDomain(1, 2, 3, 4, 5, 6));
	//
	public static final RandVar TOOTHACHE_RV = new RandVar("Toothache",
			new BooleanDomain());
	public static final RandVar CAVITY_RV = new RandVar("Cavity",
			new BooleanDomain());
	public static final RandVar CATCH_RV = new RandVar("Catch",
			new BooleanDomain());
	//
	public static final RandVar WEATHER_RV = new RandVar("Weather",
			new ArbitraryTokenDomain("sunny", "rain", "cloudy", "snow"));
	//
	public static final RandVar MENINGITIS_RV = new RandVar("Meningitis",
			new BooleanDomain());
	public static final RandVar STIFF_NECK_RV = new RandVar("StiffNeck",
			new BooleanDomain());
	//
	public static final RandVar BURGLARY_RV = new RandVar("Burglary",
			new BooleanDomain());
	public static final RandVar EARTHQUAKE_RV = new RandVar("Earthquake",
			new BooleanDomain());
	public static final RandVar ALARM_RV = new RandVar("Alarm",
			new BooleanDomain());
	public static final RandVar JOHN_CALLS_RV = new RandVar("JohnCalls",
			new BooleanDomain());
	public static final RandVar MARY_CALLS_RV = new RandVar("MaryCalls",
			new BooleanDomain());
}
