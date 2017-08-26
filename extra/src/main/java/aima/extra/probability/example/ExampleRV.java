package aima.extra.probability.example;

import java.util.Arrays;
import aima.extra.probability.RandVar;
import aima.extra.probability.domain.Domain;
import aima.extra.probability.domain.FiniteArbitraryTokenDomain;
import aima.extra.probability.domain.FiniteBooleanDomain;
import aima.extra.probability.domain.FiniteOrdinalDomain;

/**
 * Predefined example random variables from AIMA3e.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class ExampleRV {

	public static final Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
	public static final Domain diceDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3, 4, 5, 6));
	public static final Domain weatherDomain = new FiniteArbitraryTokenDomain(
			Arrays.asList("sunny", "rain", "cloudy", "snow"));
	//

	public static final RandVar DICE_1_RV = new RandVar("Dice1", diceDomain);
	public static final RandVar DICE_2_RV = new RandVar("Dice2", diceDomain);
	//

	public static final RandVar TOOTHACHE_RV = new RandVar("Toothache", booleanDomain);
	public static final RandVar CAVITY_RV = new RandVar("Cavity", booleanDomain);
	public static final RandVar CATCH_RV = new RandVar("Catch", booleanDomain);
	//

	public static final RandVar WEATHER_RV = new RandVar("Weather", weatherDomain);
	//

	public static final RandVar MENINGITIS_RV = new RandVar("Meningitis", booleanDomain);
	public static final RandVar STIFF_NECK_RV = new RandVar("StiffNeck", booleanDomain);
	//

	public static final RandVar BURGLARY_RV = new RandVar("Burglary", booleanDomain);
	public static final RandVar EARTHQUAKE_RV = new RandVar("Earthquake", booleanDomain);
	public static final RandVar ALARM_RV = new RandVar("Alarm", booleanDomain);
	public static final RandVar JOHN_CALLS_RV = new RandVar("JohnCalls", booleanDomain);
	public static final RandVar MARY_CALLS_RV = new RandVar("MaryCalls", booleanDomain);
	//

	public static final RandVar CLOUDY_RV = new RandVar("Cloudy", booleanDomain);
	public static final RandVar SPRINKLER_RV = new RandVar("Sprinkler", booleanDomain);
	public static final RandVar RAIN_RV = new RandVar("Rain", booleanDomain);
	public static final RandVar WET_GRASS_RV = new RandVar("WetGrass", booleanDomain);
	//
}
