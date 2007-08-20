/*
 * Created on Sep 15, 2003 by Ravi Mohan
 *  
 */
package aima.logic.propositional.parsing.ast;

import aima.logic.common.ParseTreeNode;
import aima.logic.propositional.parsing.PLVisitor;

/**
 * @author Ravi Mohan
 * 
 */

public abstract class Sentence implements ParseTreeNode {

	public abstract Object accept(PLVisitor plv, Object arg);

}