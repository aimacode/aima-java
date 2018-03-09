package aima.test.unit.probability;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import aima.extra.probability.DoubleProbabilityNumber;
import aima.extra.probability.ProbabilityNumber;
import aima.extra.probability.RandVar;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.bayes.ProbabilityTable;
import aima.extra.probability.domain.Domain;
import aima.extra.probability.domain.FiniteBooleanDomain;
import aima.extra.probability.domain.FiniteOrdinalDomain;
import aima.extra.probability.factory.ProbabilityFactory;

public class TestToBeRemoved {

	@Test
	public void test() {
		Class<? extends ProbabilityNumber> clazz = DoubleProbabilityNumber.class;
		Domain booleanDomain = FiniteBooleanDomain.getFiniteBooleanDomain();
		Domain ordinalDomain = new FiniteOrdinalDomain<>(Arrays.asList(1, 2, 3));
		RandomVariable rv1 = new RandVar("X", booleanDomain);
		RandomVariable rv2 = new RandVar("Y", ordinalDomain);
		ProbabilityFactory<?> probFactory = ProbabilityFactory.make(clazz);
		List<RandomVariable> vars = Arrays.asList(rv1, rv2);
		List<ProbabilityNumber> values = Arrays.asList(
				// X = true, Y = 1
				probFactory.valueOf(0.2),
				// X = true, Y = 2
				probFactory.valueOf(0.1),
				// X = true, Y = 3
				probFactory.valueOf(0.2),
				// X = false, Y = 1
				probFactory.valueOf(0.3),
				// X = false, Y = 2
				probFactory.valueOf(0.1),
				// X = false, Y = 3
				probFactory.valueOf(0.1));
		ProbabilityTable newTable = new ProbabilityTable(vars, values, clazz);
		newTable.stream().forEach(System.out::println);
		newTable.stream().parallel().forEach(System.out::println);
	}

}
