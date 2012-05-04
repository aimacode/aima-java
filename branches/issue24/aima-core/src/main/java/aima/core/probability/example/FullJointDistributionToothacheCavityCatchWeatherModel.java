package aima.core.probability.example;

import aima.core.probability.full.FullJointDistributionModel;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class FullJointDistributionToothacheCavityCatchWeatherModel extends
		FullJointDistributionModel {

	public FullJointDistributionToothacheCavityCatchWeatherModel() {
		super(new double[] {
				// Toothache = true, Cavity = true, Catch = true, Weather =
				// sunny
				0.0648,
				// Toothache = true, Cavity = true, Catch = true, Weather = rain
				0.0108,
				// Toothache = true, Cavity = true, Catch = true, Weather =
				// cloudy
				0.03132,
				// Toothache = true, Cavity = true, Catch = true, Weather = snow
				0.00108,
				// Toothache = true, Cavity = true, Catch = false, Weather =
				// sunny
				0.0072,
				// Toothache = true, Cavity = true, Catch = false, Weather =
				// rain
				0.0012,
				// Toothache = true, Cavity = true, Catch = false, Weather =
				// cloudy
				0.00348,
				// Toothache = true, Cavity = true, Catch = false, Weather =
				// snow
				0.00012,
				// Toothache = true, Cavity = false, Catch = true, Weather =
				// sunny
				0.0096,
				// Toothache = true, Cavity = false, Catch = true, Weather =
				// rain
				0.0016,
				// Toothache = true, Cavity = false, Catch = true, Weather =
				// cloudy
				0.00464,
				// Toothache = true, Cavity = false, Catch = true, Weather =
				// snow
				0.00016,
				// Toothache = true, Cavity = false, Catch = false, Weather =
				// sunny
				0.0384,
				// Toothache = true, Cavity = false, Catch = false, Weather =
				// rain
				0.0064,
				// Toothache = true, Cavity = false, Catch = false, Weather =
				// cloudy
				0.01856,
				// Toothache = true, Cavity = false, Catch = false, Weather =
				// snow
				0.00064,
				// Toothache = false, Cavity = true, Catch = true, Weather =
				// sunny
				0.0432,
				// Toothache = false, Cavity = true, Catch = true, Weather =
				// rain
				0.0072,
				// Toothache = false, Cavity = true, Catch = true, Weather =
				// cloudy
				0.02088,
				// Toothache = false, Cavity = true, Catch = true, Weather =
				// snow
				0.00072,
				// Toothache = false, Cavity = true, Catch = false, Weather =
				// sunny
				0.0048,
				// Toothache = false, Cavity = true, Catch = false, Weather =
				// rain
				0.0008,
				// Toothache = false, Cavity = true, Catch = false, Weather =
				// cloudy
				0.00232,
				// Toothache = false, Cavity = true, Catch = false, Weather =
				// snow
				0.00008,
				// Toothache = false, Cavity = false, Catch = true, Weather =
				// sunny
				0.0864,
				// Toothache = false, Cavity = false, Catch = true, Weather =
				// rain
				0.0144,
				// Toothache = false, Cavity = false, Catch = true, Weather =
				// cloudy
				0.04176,
				// Toothache = false, Cavity = false, Catch = true, Weather =
				// snow
				0.00144,
				// Toothache = false, Cavity = false, Catch = false, Weather =
				// sunny
				0.3456,
				// Toothache = false, Cavity = false, Catch = false, Weather =
				// rain
				0.0576,
				// Toothache = false, Cavity = false, Catch = false, Weather =
				// cloudy
				0.16704,
				// Toothache = false, Cavity = false, Catch = false, Weather =
				// snow
				0.00576 }, ExampleRV.TOOTHACHE_RV, ExampleRV.CAVITY_RV,
				ExampleRV.CATCH_RV, ExampleRV.WEATHER_RV);
	}
}
