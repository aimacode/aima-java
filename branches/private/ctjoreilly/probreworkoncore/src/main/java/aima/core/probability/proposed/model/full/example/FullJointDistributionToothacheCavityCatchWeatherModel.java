package aima.core.probability.proposed.model.full.example;

import java.util.ArrayList;
import java.util.List;

import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.ArbitraryTokenDomain;
import aima.core.probability.proposed.model.domain.BooleanDomain;
import aima.core.probability.proposed.model.full.FullJointDistributionModel;

public class FullJointDistributionToothacheCavityCatchWeatherModel extends
		FullJointDistributionModel {

	private RandomVariable toothacheRV = null;
	private RandomVariable cavityRV = null;
	private RandomVariable catchRV = null;
	private RandomVariable weatherRV = null;

	public FullJointDistributionToothacheCavityCatchWeatherModel() {
		super(new double[] {
				// Toothache = true,  Cavity = true,  Catch = true, Weather = sunny
				0.0648,
				// Toothache = true,  Cavity = true,  Catch = true, Weather = rain
				0.0108,
				// Toothache = true,  Cavity = true,  Catch = true, Weather = cloudy
				0.03132,
				// Toothache = true,  Cavity = true,  Catch = true, Weather = snow
				0.00108,
				// Toothache = true,  Cavity = true,  Catch = false, Weather = sunny
				0.0072,
				// Toothache = true,  Cavity = true,  Catch = false, Weather = rain
				0.0012,
				// Toothache = true,  Cavity = true,  Catch = false, Weather = cloudy
				0.00348,
				// Toothache = true,  Cavity = true,  Catch = false, Weather = snow
				0.00012,
				// Toothache = true,  Cavity = false, Catch = true, Weather = sunny
				0.0096,
				// Toothache = true,  Cavity = false, Catch = true, Weather = rain
				0.0016,
				// Toothache = true,  Cavity = false, Catch = true, Weather = cloudy
				0.00464,
				// Toothache = true,  Cavity = false, Catch = true, Weather = snow
				0.00016,
				// Toothache = true,  Cavity = false, Catch = false, Weather = sunny
				0.0384,
				// Toothache = true,  Cavity = false, Catch = false, Weather = rain
				0.0064,
				// Toothache = true,  Cavity = false, Catch = false, Weather = cloudy
				0.01856,
				// Toothache = true,  Cavity = false, Catch = false, Weather = snow
				0.00064,
				// Toothache = false, Cavity = true,  Catch = true, Weather = sunny
				0.0432,
				// Toothache = false, Cavity = true,  Catch = true, Weather = rain
				0.0072,
				// Toothache = false, Cavity = true,  Catch = true, Weather = cloudy
				0.02088,
				// Toothache = false, Cavity = true,  Catch = true, Weather = snow
				0.00072,
				// Toothache = false, Cavity = true,  Catch = false, Weather = sunny
				0.0048,
				// Toothache = false, Cavity = true,  Catch = false, Weather = rain
				0.0008,
				// Toothache = false, Cavity = true,  Catch = false, Weather = cloudy
				0.00232,
				// Toothache = false, Cavity = true,  Catch = false, Weather = snow
				0.00008,
				// Toothache = false, Cavity = false, Catch = true, Weather = sunny
				0.0864,
				// Toothache = false, Cavity = false, Catch = true, Weather = rain
				0.0144,
				// Toothache = false, Cavity = false, Catch = true, Weather = cloudy
				0.04176,
				// Toothache = false, Cavity = false, Catch = true, Weather = snow
				0.00144,
				// Toothache = false, Cavity = false, Catch = false, Weather = sunny
				0.3456,
				// Toothache = false, Cavity = false, Catch = false, Weather = rain
				0.0576,
				// Toothache = false, Cavity = false, Catch = false, Weather = cloudy
				0.16704,
				// Toothache = false, Cavity = false, Catch = false, Weather = snow
				0.00576
		}, new RandomVariable("Toothache", new BooleanDomain()),
				new RandomVariable("Cavity", new BooleanDomain()),
				new RandomVariable("Catch", new BooleanDomain()),
				new RandomVariable("Weather", new ArbitraryTokenDomain("sunny", "rain", "cloudy", "snow")));

		List<RandomVariable> vars = new ArrayList<RandomVariable>(
				getRepresentation());
		toothacheRV = vars.get(0);
		cavityRV = vars.get(1);
		catchRV = vars.get(2);
		weatherRV = vars.get(3);
	}

	public RandomVariable getToothache() {
		return toothacheRV;
	}

	public RandomVariable getCavity() {
		return cavityRV;
	}

	public RandomVariable getCatch() {
		return catchRV;
	}
	
	public RandomVariable getWeather() {
		return weatherRV;
	}
}
