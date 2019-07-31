package aima.core.agent;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): pg 50.<br>
 * 
 * The most effective way to handle partial observability is for the agent to
 * keep track of the part of the world it can't see now. That is, the agent
 * should maintain some sort of internal state that depends on the percept
 * history and thereby reflects at least some of the unobserved aspects of the
 * current state.
 *
 * <br>
 * This implementation serves as marker interface but the framework does
 * not require to use it.
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public interface State {

}
