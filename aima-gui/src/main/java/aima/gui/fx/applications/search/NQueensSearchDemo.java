package aima.gui.fx.applications.search;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensBoard.Config;
import aima.core.environment.nqueens.NQueensFunctions;
import aima.core.environment.nqueens.NQueensGenAlgoUtil;
import aima.core.environment.nqueens.QueenAction;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.local.*;
import aima.core.search.uninformed.DepthFirstSearch;

import java.util.*;

/**
 * Command line demo which demonstrates how different search algorithms
 * solve the n-queens problem.
 * 
 * @author Ruediger Lunde
 */
public class NQueensSearchDemo {
	private int boardSize = 8;
	private int populationSize = 10;
	private double mutationProbability = 0.2;
	public int k = 30;
	private double lambda = 2.0 / 100;
	private int maxIterations = 500;

	private static Random random = new Random();
	private GeneticAlgorithm<Integer> genAlgo;
	private SearchForActions<NQueensBoard, QueenAction> search;
	private NQueensBoard board;
	private List<ProgressTracker> progressTracers = new ArrayList<>();

	public static void main(String[] args) {
		NQueensSearchDemo demo = new NQueensSearchDemo();
		// prog.setBoardSize(32);
		demo.addProgressTracker(demo::printProgress);

		System.out.println("NQueens depth-first search experiment (boardSize=" + demo.boardSize + ") -->");
		demo.initExperiment(Config.EMPTY);
		demo.startExperiment(new DepthFirstSearch<>(new TreeSearch<>()));
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

	public void addProgressTracker(ProgressTracker tracer) {
		progressTracers.add(tracer);
	}

	public void initExperiment(Config config) {
		board = new NQueensBoard(boardSize, config);
		genAlgo = null;
		search = null;
	}

	public void startExperiment(SearchForActions<NQueensBoard, QueenAction> search) {

		search.addNodeListener(n -> notifyProgressTrackers(n.getState(), search.getMetrics()));

		Problem<NQueensBoard, QueenAction> problem;
		if (board.getNumberOfQueensOnBoard() == 0)
			problem = new GeneralProblem<>(board, NQueensFunctions::getIFActions,
					NQueensFunctions::getResult, NQueensFunctions::testGoal);
		else
			problem = new GeneralProblem<>(board, NQueensFunctions::getCSFActions,
					NQueensFunctions::getResult, NQueensFunctions::testGoal);
		Optional<List<QueenAction>> actions = search.findActions(problem);
		if (actions.isPresent())
			for (QueenAction action : actions.get())
				board = NQueensFunctions.getResult(board, action);

		notifyProgressTrackers(board, search.getMetrics());
	}

	public void startHillClimbingExperiment() {
		// board = new NQueensBoard(boardSize, Config.QUEEN_IN_EVERY_COL);
		Problem<NQueensBoard, QueenAction> problem = new GeneralProblem<>(board, NQueensFunctions::getCSFActions,
				NQueensFunctions::getResult, NQueensFunctions::testGoal);
		search = new HillClimbingSearch<>(n -> -NQueensFunctions.getNumberOfAttackingPairs(n));
		search.addNodeListener(n -> notifyProgressTrackers(n.getState(), search.getMetrics()));
		search.findActions(problem);

		board = (NQueensBoard) ((HillClimbingSearch) search).getLastState();
		notifyProgressTrackers(board, search.getMetrics());
	}

	public void startSimulatedAnnealingExperiment() {
		Problem<NQueensBoard, QueenAction> problem = new GeneralProblem<>(board, NQueensFunctions::getCSFActions,
				NQueensFunctions::getResult, NQueensFunctions::testGoal);
		Scheduler scheduler = new Scheduler(k, lambda, maxIterations);
		search = new SimulatedAnnealingSearch<>(NQueensFunctions::getNumberOfAttackingPairs, scheduler);
		search.addNodeListener(n -> notifyProgressTrackers(n.getState(), search.getMetrics()));
		search.findActions(problem);

		board = (NQueensBoard) ((SimulatedAnnealingSearch) search).getLastState();
		notifyProgressTrackers(board, search.getMetrics());
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
		genAlgo.addProgressTracer((it, pop) -> notifyProgressTrackers(pop, fitnessFn));

		List<Individual<Integer>> population = new ArrayList<>();
		List<Integer> rep = new ArrayList<>();
		for (int i = 0; i < boardSize; i++)
			rep.add(0);
		for (int i = 0; i < populationSize; i++)
			if (randomConfig)
				population.add(NQueensGenAlgoUtil.generateRandomIndividual(boardSize));
			else
				population.add(new Individual<>(rep));

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

	private void notifyProgressTrackers(Collection<Individual<Integer>> population, FitnessFunction<Integer> fitnessFn) {
		Individual<Integer> best = genAlgo.retrieveBestIndividual(population, fitnessFn);
		notifyProgressTrackers(NQueensGenAlgoUtil.getBoardForIndividual(best), genAlgo.getMetrics());
	}

	private void notifyProgressTrackers(NQueensBoard board, Metrics metrics) {
		for (ProgressTracker tracker : progressTracers)
			tracker.trackProgress(board, metrics);
	}

	public interface ProgressTracker {
		void trackProgress(NQueensBoard board, Metrics metrics);
	}
}
