/*
 * Created on Sep 20, 2004
 *
 */
package aima.logic.fol.parsing.ast;

import java.util.List;

import aima.logic.common.ParseTreeNode;
import aima.logic.fol.parsing.FOLVisitor;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface FOLNode extends ParseTreeNode {
	String getSymbolicName();

	boolean isCompound();

	List<? extends FOLNode> getArgs();

	Object accept(FOLVisitor v, Object arg);

	FOLNode copy();
}
