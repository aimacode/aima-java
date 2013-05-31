package aima.core.logic.propositional.parsing.ast;

import aima.core.logic.common.ParseTreeNode;
import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 244.<br>
 * <br>
 * The base of the knowledge representation language for propositional logic.
 * Note: this class hierarchy defines the abstract syntax representation used
 * for representing propositional logic.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * 
 */
public abstract class Sentence implements ParseTreeNode {
	public abstract Object accept(PLVisitor plv, Object arg);
}