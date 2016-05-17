package aima.core.search.basic.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.Search;
import aima.core.search.basic.support.BasicNodeFactory;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 * 
 * <pre>
 * function SIMULATED-ANNEALING(problem, schedule) returns a solution state
 *                    
 *   current &lt;- MAKE-NODE(problem.INITIAL-STATE)
 *   for t = 1 to INFINITY do
 *     T &lt;- schedule(t)
 *     if T = 0 then return current
 *     next &lt;- a randomly selected successor of current
 *     /\E &lt;- next.VALUE - current.value
 *     if /\E &gt; 0 then current &lt;- next
 *     else current &lt;- next only with probability e&circ;(/\E/T)
 * </pre>
 * 
 * Figure ?? The simulated annealing search algorithm, a version of stochastic
 * hill climbing where some downhill moves are allowed. Downhill moves are
 * accepted readily early in the annealing schedule and then less often as time
 * goes on. The schedule input determines the value of the temperature T as a
 * function of time.
 * 
 * @author Anurag Rai
 * @author Ravi Mohan
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class SimulatedAnnealingSearch<A, S> implements Search<A, S> {
	private ToDoubleFunction<Node<A, S>> h;
	private Scheduler scheduler;
	private NodeFactory<A, S> nodeFactory;
	
	public SimulatedAnnealingSearch(ToDoubleFunction<Node<A, S>> h) {
		this(h, new Scheduler());
	}
	
	public SimulatedAnnealingSearch(ToDoubleFunction<Node<A, S>> h, Scheduler scheduler) {
		this(h, scheduler, new BasicNodeFactory<>());
	}
			
	public SimulatedAnnealingSearch(ToDoubleFunction<Node<A, S>> h, Scheduler scheduler, NodeFactory<A, S> nodeFactory) {
		this.h = h;
		this.scheduler = scheduler;
		this.nodeFactory = nodeFactory;
	}
	
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// current <- MAKE-NODE(problem.INITIAL-STATE)
		Node<A, S> current = nodeFactory.newRootNode(problem.initialState(), 0);
		List<A> ret = new ArrayList<A>();
		// for t = 1 to INFINITY do
		int timeStep = 0;
		while ( true ) {
			// temperature <- schedule(t)		
			double temperature = scheduler.getTemp(timeStep);
			timeStep++;
			// if temperature = 0 then return current
			if ( temperature == 0.0 ) {
				ret = solution(current);
				break;
			}
			
			List<Node<A, S>> children = new ArrayList<>();
			// expand the current node to find the children
			for (A action : problem.actions(current.state())) {
	               children.add(nodeFactory.newChildNode(problem, current, action));
	        }
			if ( !children.isEmpty() ) {
				// next <- a randomly selected successor of current
				Node<A, S> next = children.get(new Random().nextInt(children.size()));
				// /\E <- next.VALUE - current.value
				double deltaE = getValue(h, next) - getValue(getHeuristicFunctionH(), current);

				if (shouldAccept(temperature, deltaE)) {
					current = next;
				}
			}
		}
		return ret;
	}
	

	/**
	 * Returns <em>e</em><sup>&delta<em>E / T</em></sup>
	 * 
	 * @param temperature
	 *            <em>T</em>, a "temperature" controlling the probability of
	 *            downward steps
	 * @param deltaE
	 *            VALUE[<em>next</em>] - VALUE[<em>current</em>]
	 * @return <em>e</em><sup>&delta<em>E / T</em></sup>
	 */
	public double probabilityOfAcceptance(double temperature, double deltaE) {
		return Math.exp(deltaE / temperature);
	}


	// if /\E > 0 then current <- next
	// else current <- next only with probability e^(/\E/T)
	public boolean shouldAccept(double temperature, double deltaE) {
		return (deltaE > 0.0)
				|| (new Random().nextDouble() <= probabilityOfAcceptance(
						temperature, deltaE));
	}

	public double getValue(ToDoubleFunction<Node<A, S>> hf, Node<A, S> n) {
		// assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
		// SA deals with gradient DESCENT
		return -1 * hf.applyAsDouble(n);
	}
	
    public ToDoubleFunction<Node<A, S>> getHeuristicFunctionH() {
    	return h;
    }
    
    public Scheduler getScheduler() {
    	return scheduler;
    }
    
    /**
     * The Scheduler for Simulated Annealing.
     * 
	 * @author Ravi Mohan
	 * @author Anurag Rai
     */
    public static class Scheduler {

      	private final int k, limit;
    	private final double lam;
    	
    	//default constructor
    	public Scheduler() {
    		//base value
    		this.k = 20;
    		this.lam = 0.045;
    		//time limit
    		this.limit = 100;
    	}
    	
    	public Scheduler(int k, double lam, int limit) {
    		this.k = k;
    		this.lam = lam;
    		this.limit = limit;
    	}
    	
    	/*
    	* The probability also decreases as the temperature T goes down: 
    	* bad moves are more likely to be allowed at the start
    	* when T is high, and they become more unlikely as T decreases.
    	*
    	* @param t
    	*		the time that has gone by from the start of the algo
    	*
    	* @return the value of schedule calculated as a function of given time
    	*/
    	public double getTemp(int t) {
    	  if (t < limit) {
    			double res = k * Math.exp((-1) * lam * t);
    			return res;
    		} else {
    			return 0.0;
    		}
    	}
    }
}
