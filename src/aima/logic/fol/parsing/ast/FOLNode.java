/*
 * Created on Sep 20, 2004
 *
 */
package aima.logic.fol.parsing.ast;

import aima.logic.common.ParseTreeNode;
import aima.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * 
 */
public interface FOLNode extends ParseTreeNode {
	public Object accept(FOLVisitor v, Object arg);

	public String toString();

	public FOLNode copy();

}
