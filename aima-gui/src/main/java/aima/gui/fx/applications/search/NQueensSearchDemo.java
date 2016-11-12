package aima.gui.fx.applications.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import aima.core.agent.Action;
import aima.core.environment.nqueens.AttackingPairsHeuristic;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensBoard.Config;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.environment.nqueens.NQueensGenAlgoUtil;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.local.FitnessFunction;
import aima.core.search.local.GeneticAlgorithm;
import aima.core.search.local.HillClimbingSearch;
import aima.core.search.local.Individual;
import aima.core.search.local.Scheduler;
import aima.core.search.local.SimulatedAnnealingSearch;
import aima.core.search.uninformed.DepthFirstSearch;

/**
 * Command line demo which demonstrates how different search algorithms
 * solve the n-queens problem.
 * 
 * @author Ruediger Lunde
 */
public class NQueensSearchDemo {
	private int boardSize = 8;
	public int populationSize = 10;
	public double mutationProbability = 0.2;
	public int k = 30;
	public double lambda = 2.0 / 100;
	public int maxIterations = 500;

	private static Random random = new Random();
	private GeneticAlgorithm<Integer> genAlgo;
	private SearchForActions search;
	private NQueensBoard board;
	private List<ProgressTracer> progressTracers = new ArrayList<>();

	public static void main(String[] args) {
		NQueensSearchDemo demo = new NQueensSearchDemo();
		// prog.setBoardSize(32);
		demo.addProgressTracer(demo::printProgress);

		System.out.println("NQueens depth-first search experiment (boardSize=" + demo.boardSize + ") -->");
		demo.initExperiment(Config.EMPTY);
		demo.startExperiment(new DepthFirstSearch(new TreeSearch()));
		demo.printResult();

		System.out.println("NQueens hill climbing search experiment (boardSize=" + demo.boardSize + ") -->");
		demo.initExperiment(Config.QUEENS_IN_FIRST_ROW);
		demo.startHillClimbingExperiment();
		demo.printResult();

		System.out.println("NQueens simulated annealing experiment (boardSize=" + demo.boardSize + ", maxIterations="
				+ demo.maxIterations + ") -->");
		demo.initExperiment(Config.QUEENS_IN_FIRST_ROW);
		demo.startSimulatedAnnealingExperiment();
		demo.printResult();

		System.out.println("NQueens genetic algorithm experiment (boardSize=" + demo.boardSize + ", popSize="
				+ demo.populationSize + ", mutProb=" + demo.mutationProbability + ") -->");
		demo.initExperiment(Config.EMPTY);
		demo.startGenAlgoExperiment(false);
		demo.printResult();
	}

	public void setBoardSize(int size) {
		boardSize = size;
		board = new NQueensBoard(boardSize);
	}

	public void addProgressTracer(ProgressTracer tracer) {
		progressTracers.add(tracer);
	}

	public void initExperiment(Config config) {
		board = new NQueensBoard(boardSize, config);
		genAlgo = null;
		search = null;
	}

	public void startExperiment(SearchForActions search) {

		search.getNodeExpander()
				.addNodeListener(n -> notifyProgressTracers((NQueensBoard) n.getState(), search.getMetrics()));

		Problem problem = null;
		if (board.getNumberOfQueensOnBoard() == 0)
			problem = new Problem(board, NQueensFunctionFactory.getIActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
		else
			problem = new Problem(board, NQueensFunctionFactory.getCActionsFunction(),
					NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
		List<Action> actions = search.findActions(problem);
		for (Action action : actions)
			board = (NQueensBoard) NQueensFunctionFactory.getResultFunction().result(board, action);

		notifyProgressTracers(board, search.getMetrics());
	}

	public void startHillClimbingExperiment() {
		// board = new NQueensBoard(boardSize, Config.QUEEN_IN_EVERY_COL);
		Problem problem = new Problem(board, NQueensFunctionFactory.getCActionsFunction(),
				NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
		search = new HillClimbingSearch(new AttackingPairsHeuristic());
		search.getNodeExpander()
				.addNodeListener(n -> notifyProgressTracers((NQueensBoard) n.getState(), search.getMetrics()));
		search.findActions(problem);

		board = (NQueensBoard) ((HillClimbingSearch) search).getLastSearchState();
		notifyProgressTracers(board, search.getMetrics());
	}

	public void startSimulatedAnnealingExperiment() {
		Problem problem = new Problem(board, NQueensFunctionFactory.getCActionsFunction(),
				NQueensFunctionFactory.getResultFunction(), new NQueensGoalTest());
		Scheduler scheduler = new Scheduler(k, lambda, maxIterations);
		search = new SimulatedAnnealingSearch(new AttackingPairsHeuristic(), scheduler);
		search.getNodeExpander()
				.addNodeListener(n -> notifyProgressTracers((NQueensBoard) n.getState(), search.getMetrics()));
		search.findActions(problem);

		board = (NQueensBoard) ((SimulatedAnnealingSearch) search).getLastSearchState();
		notifyProgressTracers(board, search.getMetrics());
	}

	public void startGenAlgoExperiment(boolean randomConfig) {
		Collection<Integer> alphabet = NQueensGenAlgoUtil.getFiniteAlphabetForBoardOfSize(boardSize);
		FitnessFunction<Integer> fitnessFn = NQueensGenAlgoUtil.getFitnessFunction();

		genAlgo = new GeneticAlgorithm<Integer>(boardSize, alphabet, mutationProbability, random) {
			protected void updateMetrics(Collection<Individual<Integer>> pop, int itCount, long time) {
				super.updateMetrics(pop, itCount, time);
				double avg = 0.0;
				double max = Double.NEGATIVE_INFINITY;
				for (Individual<Integer> ind : pop) {
					double fval = fitnessFn.apply(ind);
					avg += fval;
					max = Math.max(max, fval);
				}
				avg /= pop.size();
				metrics.set("fitMax", max);
				metrics.set("fitAvg", avg);
			}
		};
		genAlgo.addProgressTracer((it, pop) -> notifyProgressTracers(pop, fitnessFn));

		List<Individual<Integer>> population = new ArrayList<>();
		List<Integer> rep = new ArrayList<Integer>();
		for (int i = 0; i < boardSize; i++)
			rep.add(0);
		for (int i = 0; i < populationSize; i++)
			if (randomConfig)
				population.add(NQueensGenAlgoUtil.generateRandomIndividual(boardSize));
			else
				population.add(new Individual<Integer>(rep));

		Individual<Integer> result = genAlgo.geneticAlgorithm(population, fitnessFn, maxIterations);

		board = NQueensGenAlgoUtil.getBoardForIndividual(result);
	}

	public NQueensBoard getBoard() {
		return board;
	}

	private void printProgress(NQueensBoard board, Metrics metrics) {
		System.out.println(board.getNumberOfAttackingPairs() + " attacking pairs " + metrics);
	}

	private void printResult() {
		if (board != null) {
			System.out.println("Final State:\n" + board);
			System.out.println("Attacking pairs: " + board.getNumberOfAttackingPairs());
		}
		if (genAlgo != null)
			System.out.println("Metrics: " + genAlgo.getMetrics());
		if (search != null)
			System.out.println("Metrics: " + search.getMetrics());
		System.out.println("Experiment finished.\n");
	}

	private void notifyProgressTracers(Collection<Individual<Integer>> population, FitnessFunction<Integer> fitnessFn) {
		Individual<Integer> best = genAlgo.retrieveBestIndividual(population, fitnessFn);
		notifyProgressTracers(NQueensGenAlgoUtil.getBoardForIndividual(best), genAlgo.getMetrics());
	}

	private void notifyProgressTracers(NQueensBoard board, Metrics metrics) {
		for (ProgressTracer tracer : progressTracers)
			tracer.traceProgress(board, metrics);
	}

	public interface ProgressTracer {
		void traceProgress(NQueensBoard board, Metrics metrics);
	}
}
