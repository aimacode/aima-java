package aima.core.logic.propositional.parsing.ast;

import aima.core.logic.common.ParseTreeNode;
import aima.core.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public abstract class Sentence implements ParseTreeNode {

	public abstract Object accept(PLVisitor plv, Object arg);
}