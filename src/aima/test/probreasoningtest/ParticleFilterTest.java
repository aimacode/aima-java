package aima.test.probreasoningtest;

import junit.framework.TestCase;
import aima.probability.RandomVariable;
import aima.probability.Randomizer;
import aima.probability.reasoning.HMMFactory;
import aima.probability.reasoning.HiddenMarkovModel;
import aima.probability.reasoning.HmmConstants;
import aima.probability.reasoning.Particle;
import aima.probability.reasoning.ParticleSet;
import aima.probability.reasoning.TransitionModel;
import aima.test.probabilitytest.MockRandomizer;

/**
 * @author Ravi Mohan
 * 
 */
public class ParticleFilterTest extends TestCase {
	private HiddenMarkovModel rainman, robot;

	ParticleSet particleSet;

	Randomizer randomizer;

	@Override
	public void setUp() {
		rainman = HMMFactory.createRainmanHMM();
		robot = HMMFactory.createRobotHMM();

		randomizer = new MockRandomizer(new double[] { 0.1, 0.9 });
		particleSet = new ParticleSet(rainman);
		particleSet.add(new Particle(HmmConstants.RAINING));
		particleSet.add(new Particle(HmmConstants.RAINING));
		particleSet.add(new Particle(HmmConstants.RAINING));
		particleSet.add(new Particle(HmmConstants.NOT_RAINING));

	}

	public void testFilteringWithParticleSetsWorksForRainmanHmm() {
		Randomizer r = new MockRandomizer(new double[] { 0.1, 0.2, 0.3, 0.4,
				0.5, 0.6, 0.7, 0.8, 0.9 });
		ParticleSet starting = rainman.prior().toParticleSet(rainman, r, 100);
		assertEquals(56, starting
				.numberOfParticlesWithState(HmmConstants.RAINING));
		assertEquals(44, starting
				.numberOfParticlesWithState(HmmConstants.NOT_RAINING));

		ParticleSet afterSeeingUmbrella = starting.filter(
				HmmConstants.SEE_UMBRELLA, r);
		assertEquals(84, afterSeeingUmbrella
				.numberOfParticlesWithState(HmmConstants.RAINING));
		assertEquals(16, afterSeeingUmbrella
				.numberOfParticlesWithState(HmmConstants.NOT_RAINING));

		// or alternatively
		ParticleSet afterNotSeeingUmbrella = starting.filter(
				HmmConstants.SEE_NO_UMBRELLA, r);
		assertEquals(12, afterNotSeeingUmbrella
				.numberOfParticlesWithState(HmmConstants.RAINING));
		assertEquals(88, afterNotSeeingUmbrella
				.numberOfParticlesWithState(HmmConstants.NOT_RAINING));
	}

	public void testFilteringWithParticleSetsForRobotHmm() {
		Randomizer r = new MockRandomizer(new double[] { 0.1, 0.2, 0.3, 0.4,
				0.5, 0.6, 0.7, 0.8, 0.9 });
		ParticleSet starting = robot.prior().toParticleSet(robot, r, 100);
		assertEquals(56, starting
				.numberOfParticlesWithState(HmmConstants.DOOR_OPEN));
		assertEquals(44, starting
				.numberOfParticlesWithState(HmmConstants.DOOR_CLOSED));

		// step one = robot takes no action but senses open door
		ParticleSet afterStepOne = starting.filter(HmmConstants.SEE_DOOR_OPEN,
				r);
		assertEquals(66, afterStepOne
				.numberOfParticlesWithState(HmmConstants.DOOR_OPEN));
		assertEquals(34, afterStepOne
				.numberOfParticlesWithState(HmmConstants.DOOR_CLOSED));

		// step two = robot pushes the door and then senses open door
		ParticleSet afterStepTwo = starting.filter(HmmConstants.PUSH_DOOR,
				HmmConstants.SEE_DOOR_OPEN, r);
		assertEquals(100, afterStepTwo
				.numberOfParticlesWithState(HmmConstants.DOOR_OPEN));
		assertEquals(0, afterStepTwo
				.numberOfParticlesWithState(HmmConstants.DOOR_CLOSED));
	}

	public void testRandomVariableConversionToParticleSet() {
		RandomVariable rv = rainman.prior();
		ParticleSet ps = rv.toParticleSet(rainman, randomizer, 10);
		assertEquals(5, ps.numberOfParticlesWithState(HmmConstants.RAINING));
		assertEquals(5, ps.numberOfParticlesWithState(HmmConstants.NOT_RAINING));

	}

	public void testParticleSetConversionToRandomVariable() {

		RandomVariable rv = particleSet.toRandomVariable();
		assertEquals(0.75, rv.getProbabilityOf(HmmConstants.RAINING));
		assertEquals(0.25, rv.getProbabilityOf(HmmConstants.NOT_RAINING));
	}

	public void testRoundTripConversion() {
		RandomVariable rv = particleSet.toRandomVariable();
		Randomizer r = new MockRandomizer(new double[] { 0.1, 0.2, 0.3, 0.4,
				0.9 });
		ParticleSet ps2 = rv.toParticleSet(rainman, r, 10);
		assertEquals(8, ps2.numberOfParticlesWithState(HmmConstants.RAINING));
		assertEquals(2, ps2
				.numberOfParticlesWithState(HmmConstants.NOT_RAINING));
	}

	public void testTransitionModelGeneratesNewStateWhenGivenOldStateAndProbability() {

		TransitionModel tm = rainman.transitionModel();
		String oldState = HmmConstants.RAINING;
		String state1 = tm.getStateForProbability(oldState, randomizer
				.nextDouble());
		String state2 = tm.getStateForProbability(oldState, randomizer
				.nextDouble());
		assertEquals(state1, HmmConstants.RAINING);
		assertEquals(state2, HmmConstants.NOT_RAINING);
	}

	public void testParticleSetForPredictedStateGeneratedFromOldStateParticleSet() {
		Randomizer r = new MockRandomizer(new double[] { 0.1, 0.2, 0.3, 0.4,
				0.5, 0.6, 0.7, 0.8, 0.9 });
		ParticleSet ps = rainman.prior().toParticleSet(rainman, r, 10);
		assertEquals(6, ps.numberOfParticlesWithState(HmmConstants.RAINING));
		assertEquals(4, ps.numberOfParticlesWithState(HmmConstants.NOT_RAINING));

		ParticleSet nps = ps.generateParticleSetForPredictedState(r);
		assertEquals(7, nps.numberOfParticlesWithState(HmmConstants.RAINING));
		assertEquals(3, nps
				.numberOfParticlesWithState(HmmConstants.NOT_RAINING));

	}

	public void testParticleSetForPerceptionUpdatedStateGeneratedFromPredictedStateParticleSetGivenPerception() {
		Randomizer r = new MockRandomizer(new double[] { 0.1, 0.2, 0.3, 0.4,
				0.5, 0.6, 0.7, 0.8, 0.9 });
		ParticleSet starting = rainman.prior().toParticleSet(rainman, r, 10);
		ParticleSet predicted = starting
				.generateParticleSetForPredictedState(r);

		ParticleSet updatedWithPerceptionOfUmbrella = predicted
				.perceptionUpdate(HmmConstants.SEE_UMBRELLA, r);
		assertEquals(9, updatedWithPerceptionOfUmbrella
				.numberOfParticlesWithState(HmmConstants.RAINING));
		assertEquals(1, updatedWithPerceptionOfUmbrella
				.numberOfParticlesWithState(HmmConstants.NOT_RAINING));

		ParticleSet updatedWithPerceptionOfNoUmbrella = predicted
				.perceptionUpdate(HmmConstants.SEE_NO_UMBRELLA, r);
		assertEquals(2, updatedWithPerceptionOfNoUmbrella
				.numberOfParticlesWithState(HmmConstants.RAINING));
		assertEquals(8, updatedWithPerceptionOfNoUmbrella
				.numberOfParticlesWithState(HmmConstants.NOT_RAINING));
	}

}
