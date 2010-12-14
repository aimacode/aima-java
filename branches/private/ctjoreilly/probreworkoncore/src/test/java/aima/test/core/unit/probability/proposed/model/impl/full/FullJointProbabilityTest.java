package aima.test.core.unit.probability.proposed.model.impl.full;

import org.junit.Test;

import aima.core.probability.proposed.model.impl.full.example.FullJointDistributionPairFairDiceModel;
import aima.core.probability.proposed.model.impl.full.example.FullJointDistributionToothacheCavityCatchModel;
import aima.test.core.unit.probability.proposed.model.CommonProbabilityModelTests;

public class FullJointProbabilityTest extends CommonProbabilityModelTests {

	@Test
	public void test_RollingPairFairDiceModel() {
		FullJointDistributionPairFairDiceModel model = new FullJointDistributionPairFairDiceModel();
		
		test_RollingPairFairDiceModel(model, model.getDice1(), model.getDice2());
	}
	
	@Test
	public void test_ToothacheCavityCatchModel() {
		FullJointDistributionToothacheCavityCatchModel model = new FullJointDistributionToothacheCavityCatchModel();
		
		test_ToothacheCavityCatchModel(model, model.getToothache(), model.getCavity(), model.getCatch());
	}
}
