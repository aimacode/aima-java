package aima.core.logic.propositional.agent;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.logic.propositional.kb.KnowledgeBase;
import aima.core.logic.propositional.parsing.ast.Sentence;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 7.1, page
 * 236.<br>
 * <br>
 * 
 * <pre>
 * function KB-AGENT(percept) returns an action
 *   persistent: KB, a knowledge base
 *               t, a counter, initially 0, indicating time
 *           
 *   TELL(KB, MAKE-PERCEPT-SENTENCE(percept, t))
 *   action &lt;- ASK(KB, MAKE-ACTION-QUERY(t))
 *   TELL(KB, MAKE-ACTION-SENTENCE(action, t))
 *   t &lt;- t + 1
 *   return action
 * 
 * </pre>
 * 
 * Figure 7.1 A generic knowledge-based agent. Given a percept, the agent adds
 * the percept to its knowledge base, asks the knowledge base for the best
 * action, and tells the knowledge base that it has in fact taken that action.
 * 
 * @author Ciaran O'Reilly
 */
public abstract class KBAgent extends AbstractAgent {
	// persistent: KB, a knowledge base
	protected KnowledgeBase KB;
	// t, a counter, initially 0, indicating time
	private int t = 0;

	public KBAgent(KnowledgeBase KB) {
		this.KB = KB;
	}

	// function KB-AGENT(percept) returns an action
	@Override
	public Action execute(Percept percept) {
		// TELL(KB, MAKE-PERCEPT-SENTENCE(percept, t))
		KB.tell(makePerceptSentence(percept, t));
		// action &lt;- ASK(KB, MAKE-ACTION-QUERY(t))
		Action action = ask(KB, makeActionQuery(t));
		
		// TELL(KB, MAKE-ACTION-SENTENCE(action, t))
		KB.tell(makeActionSentence(action, t));
		// t &lt;- t + 1
		t = t + 1;
		// return action
		return action;
	}

	/**
	 * MAKE-PERCEPT-SENTENCE constructs a sentence asserting that the agent
	 * perceived the given percent at the given time.
	 * 
	 * @param percept
	 *            the given percept
	 * @param t
	 *            the given time
	 * @return a sentence asserting that the agent perceived the given percept
	 *         at the given time.
	 */
	// MAKE-PERCEPT-SENTENCE(percept, t)
	public abstract Sentence makePerceptSentence(Percept percept, int t);

	/**
	 * MAKE-ACTION-QUERY constructs a sentence that asks what action should be
	 * done at the current time.
	 * 
	 * @param t
	 *            the current time.
	 * @return a sentence that asks what action should be done at the current
	 *         time.
	 */
	// MAKE-ACTION-QUERY(t)
	public abstract Sentence makeActionQuery(int t);

	/**
	 * MAKE-ACTION-SENTENCE constructs a sentence asserting that the chosen action was executed.
	 * @param action
	 *        the chose action.
	 * @param t
	 *        the time at which the action was executed.
	 * @return a sentence asserting that the chosen action was executed.
	 */
	// MAKE-ACTION-SENTENCE(action, t)
	public abstract Sentence makeActionSentence(Action action, int t);
	
	/**
	 * A wrapper around the KB's ask() method which translates the action (in the form of
	 * a sentence) determined by the KB into an allowed 'Action' object from the current
	 * environment in which the KB-AGENT resides.
	 * 
	 * @param KB
	 *        the KB to ask.
	 * @param actionQuery
	 *        an action query.
	 * @return the Action to be performed in response to the given query.
	 */
	// ASK(KB, MAKE-ACTION-QUERY(t))
	public abstract Action ask(KnowledgeBase KB, Sentence actionQuery);
}
