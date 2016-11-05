package aima.gui.fx.applications.search.local;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm.ProgressTracer;
import aima.core.search.local.GeneticAlgorithmForNumbers;
import aima.core.search.local.Individual;

/**
 * Command line demo which demonstrates how the generic algorithm for numbers
 * can be used to find maximums in a mathematical function.
 * 
 * @author Ruediger Lunde
 */
public class GeneticMaximumFinderDemo {

	private Function<Double, Double> func = Functions.f1;
	private double mutProb = 0.2;

	private int populationSize = 20;
	private int maxIterations = 100;

	FitnessFunction<Double> fitnessFn;

	public static void main(String[] args) {
		System.out.println("Genetic Maximum Finder Experiment (f1, mutProb=0.2) -->");
		GeneticMaximumFinderDemo demo = new GeneticMaximumFinderDemo();
		demo.startExperiment(demo::printGeneration);
		System.out.println("Experiment finished.");
	}
	
	public void setFunction(Function<Double, Double> func) {
		this.func = func;
	}

	public void setMutationProb(double mutProb) {
		this.mutProb = mutProb;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public void startExperiment(ProgressTracer<Double> pTracer) {
		GeneticAlgorithmForNumbers genAlgo = new GeneticAlgorithmForNumbers(1, Functions.minX, Functions.maxX, mutProb);
		genAlgo.addProgressTracer(pTracer);
		fitnessFn = ind -> func.apply(ind.getRepresentation().get(0));
		List<Individual<Double>> population = new ArrayList<Individual<Double>>();
		for (int i = 0; i < populationSize; i++)
			population.add(genAlgo.createRandomIndividual());
		@SuppressWarnings("unused")
		Individual<Double> best = genAlgo.geneticAlgorithm(population, fitnessFn, maxIterations);
	}

	private void printGeneration(int itCount, Collection<Individual<Double>> gen) {
		System.out.println("\n" + getIterationInfo(itCount, gen));
		List<Individual<Double>> generation = new ArrayList<>();
		generation.addAll(gen);
		Collections.sort(generation, Comparator.comparingDouble(fitnessFn::apply));
		for (Individual<Double> ind : generation)
			System.out.println(ind + " -> " + fitnessFn.apply(ind));
	}

	public String getIterationInfo(int itCount, Collection<Individual<Double>> gen) {
		double avg = 0.0;
		double max = Double.NEGATIVE_INFINITY;
		DecimalFormat f = new DecimalFormat("#0.00");
		for (Individual<Double> ind : gen) {
			double fval = fitnessFn.apply(ind);
			avg += fval;
			max = Math.max(max, fval);
		}
		avg /= gen.size();
		return "Generation: " + itCount + " Avg: " + f.format(avg) + " Max: " + f.format(max);
	}
}
