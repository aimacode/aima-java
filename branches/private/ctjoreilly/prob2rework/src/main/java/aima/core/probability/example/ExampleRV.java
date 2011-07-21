package aima.core.probability.example;

import aima.core.probability.domain.ArbitraryTokenDomain;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.domain.FiniteIntegerDomain;
import aima.core.probability.util.RandVar;

/**
 * Predefined example Random Variables from AIMA3e.
 * 
 * @author Ciaran O'Reilly
 */
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
	//
	public static final RandVar CLOUDY_RV = new RandVar("Cloudy",
			new BooleanDomain());
	public static final RandVar SPRINKLER_RV = new RandVar("Sprinkler",
			new BooleanDomain());
	public static final RandVar RAIN_RV = new RandVar("Rain",
			new BooleanDomain());
	public static final RandVar WET_GRASS_RV = new RandVar("WetGrass",
			new BooleanDomain());
	//
	public static final RandVar RAIN_tm1_RV = new RandVar("Rain_t-1",
			new BooleanDomain());
	public static final RandVar RAIN_t_RV = new RandVar("Rain_t",
			new BooleanDomain());
	public static final RandVar UMBREALLA_t_RV = new RandVar("Umbrella_t",
			new BooleanDomain());
}
