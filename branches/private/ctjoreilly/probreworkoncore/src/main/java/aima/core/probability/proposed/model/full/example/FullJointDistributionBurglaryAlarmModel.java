package aima.core.probability.proposed.model.full.example;

import java.util.ArrayList;
import java.util.List;

import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.BooleanDomain;
import aima.core.probability.proposed.model.full.FullJointDistributionModel;

public class FullJointDistributionBurglaryAlarmModel extends
		FullJointDistributionModel {

	private RandomVariable burglaryRV = null;
	private RandomVariable earthquakeRV = null;
	private RandomVariable alarmRV = null;
	private RandomVariable johnCallsRV = null;
	private RandomVariable maryCallsRV = null;

	public FullJointDistributionBurglaryAlarmModel() {
		super(new double[] {
				// B = t, E = t, A = t, J = t, M = t
				0.000001197,
				// B = t, E = t, A = t, J = t, M = f
				0.000000513,
				// B = t, E = t, A = t, J = f, M = t
				0.000000133,
				// B = t, E = t, A = t, J = f, M = f
				0.000000057,
				// B = t, E = t, A = f, J = t, M = t
				0.00000000005,
				// B = t, E = t, A = f, J = t, M = f
				0.00000000495,
				// B = t, E = t, A = f, J = f, M = t
				0.00000000095,
				// B = t, E = t, A = f, J = f, M = f
				0.00000009405,
				// B = t, E = f, A = t, J = t, M = t
				0.0005910156,
				// B = t, E = f, A = t, J = t, M = f
				0.0002532924,
				// B = t, E = f, A = t, J = f, M = t
				0.0000656684,
				// B = t, E = f, A = t, J = f, M = f
				0.0000281436,
				// B = t, E = f, A = f, J = t, M = t
				0.00000002994,
				// B = t, E = f, A = f, J = t, M = f
				0.00000296406,
				// B = t, E = f, A = f, J = f, M = t
				0.00000056886,
				// B = t, E = f, A = f, J = f, M = f
				0.00005631714,
				// B = f, E = t, A = t, J = t, M = t
				0.0003650346,
				// B = f, E = t, A = t, J = t, M = f
				0.0001564434,
				// B = f, E = t, A = t, J = f, M = t
				0.0000405594,
				// B = f, E = t, A = t, J = f, M = f
				0.0000173826,
				// B = f, E = t, A = f, J = t, M = t
				0.00000070929,
				// B = f, E = t, A = f, J = t, M = f
				0.00007021971,
				// B = f, E = t, A = f, J = f, M = t
				0.00001347651,
				// B = f, E = t, A = f, J = f, M = f
				0.00133417449,
				// B = f, E = f, A = t, J = t, M = t
				0.00062811126,
				// B = f, E = f, A = t, J = t, M = f
				0.00026919054,
				// B = f, E = f, A = t, J = f, M = t
				0.00006979014,
				// B = f, E = f, A = t, J = f, M = f
				0.00002991006,
				// B = f, E = f, A = f, J = t, M = t
				0.000498002499,
				// B = f, E = f, A = f, J = t, M = f
				0.049302247401,
				// B = f, E = f, A = f, J = f, M = t
				0.009462047481,
				// B = f, E = f, A = f, J = f, M = f
				0.936742700619 }, new RandomVariable("Burglary", new BooleanDomain()),
				new RandomVariable("Earthquake", new BooleanDomain()),
				new RandomVariable("Alarm", new BooleanDomain()),
				new RandomVariable("JohnCalls", new BooleanDomain()),
				new RandomVariable("MaryCalls", new BooleanDomain()));

		List<RandomVariable> vars = new ArrayList<RandomVariable>(
				getRepresentation());
		burglaryRV = vars.get(0);
		earthquakeRV = vars.get(1);
		alarmRV = vars.get(2);
		johnCallsRV = vars.get(3);
		maryCallsRV = vars.get(4);
	}

	public RandomVariable getBurglary() {
		return burglaryRV;
	}

	public RandomVariable getEarthquake() {
		return earthquakeRV;
	}

	public RandomVariable getAlarm() {
		return alarmRV;
	}

	public RandomVariable getJohnCalls() {
		return johnCallsRV;
	}

	public RandomVariable getMaryCalls() {
		return maryCallsRV;
	}
}
