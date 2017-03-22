package aima.extra.search.adversarial;

import aima.core.search.api.Game;
import aima.core.search.api.SearchForAdversarialActionFunction;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Implements an iterative deepening Minimax search with alpha-beta pruning and
 * action ordering. Maximal computation time is specified in seconds. The
 * algorithm is implemented as template method and can be configured and tuned
 * by subclassing.
 *
 * @author Ruediger Lunde
 * @author manthan.
 * 
 * @param <S>
 *            Type which is used for states in the game.
 * @param <A>
 *            Type which is used for actions in the game.
 * @param <P>
 *            Type which is used for players in the game.
 */
public class IterativeDeepeningAlphaBetaSearch<S, A, P> implements SearchForAdversarialActionFunction<S, A> {
    private Game<S, A, P> game;
    private double utilMax;
    private double utilMin;
    private int currDepthLimit;
    private boolean heuristicEvaluationUsed = true;
    private Timer timer;

    public static <S, A, P> IterativeDeepeningAlphaBetaSearch<S, A, P> createFor(Game<S, A, P> game, double utilMin, double utilMax, int time) {
        return new IterativeDeepeningAlphaBetaSearch<>(game, utilMin, utilMax, time);
    }

    public IterativeDeepeningAlphaBetaSearch(Game<S, A, P> game, double utilMin, double utilMax, int time) {
        this.game = game;
        this.utilMin = utilMin;
        this.utilMax = utilMax;
        this.timer = new Timer(time);
    }

    @Override
    public A apply(S state) {
        P player = game.player(state);
        List<A> results = game.actions(state);
        timer.start();
        Map<Double, A> newResults;
        for (currDepthLimit = 2; !timer.timeOutOccured() && heuristicEvaluationUsed; currDepthLimit++) {
            heuristicEvaluationUsed = false;
            newResults = new TreeMap<>();
            for (A action : results) {
                double value = minValue(game.result(state, action), player, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
                if (timer.timeOutOccured())
                    break;
                newResults.put(value, action);
            }
            if (newResults.size() > 0) {
                results = newResults.values().stream().collect(Collectors.toList());
                if (!timer.timeOutOccured()) {
                    if (hasSafeWinner(newResults.keySet().iterator().next()))
                        break;
                    else if (newResults.size() > 1)
                        break;
                }
            }
        }
        return results.get(results.size() - 1);
    }

    private double maxValue(S state, P player, double alpha, double beta, int depth) {
        if (game.isTerminalState(state) || depth >= currDepthLimit || timer.timeOutOccured()) {
            return eval(state, player);
        } else {
            double value = Double.NEGATIVE_INFINITY;
            for (A action : game.actions(state)) {
                value = Math.max(value, minValue(game.result(state, action), player, alpha, beta, depth + 1));
                if (value >= beta)
                    return value;
                alpha = Math.max(alpha, value);
            }
            return value;
        }
    }

    private double minValue(S state, P player, double alpha, double beta, int depth) {
        if (game.isTerminalState(state) || depth >= currDepthLimit || timer.timeOutOccured()) {
            return eval(state, player);
        } else {
            double value = Double.POSITIVE_INFINITY;
            for (A action : game.actions(state)) {
                value = Math.min(value, maxValue(game.result(state, action), player, alpha, beta, depth + 1));
                if (value <= alpha)
                    return value;
                beta = Math.min(beta, value);
            }
            return value;
        }
    }

    private boolean hasSafeWinner(double resultUtility) {
        return resultUtility <= utilMin || resultUtility >= utilMax;
    }

    private double eval(S state, P player) {
        if (game.isTerminalState(state)) {
            return game.utility(state, player);
        } else {
            heuristicEvaluationUsed = true;
            return (utilMin + utilMax) / 2;
        }
    }

    private static class Timer {
        private long duration;
        private long startTime;

        Timer(int maxSeconds) {
            this.duration = 1000l * maxSeconds;
        }

        void start() {
            startTime = System.currentTimeMillis();
        }

        boolean timeOutOccured() {
            return System.currentTimeMillis() > startTime + duration;
        }
    }
}
