package aima.core.logic.propositional.parsing;

import aima.core.logic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.propositional.parsing.ast.PropositionSymbol;

/**
 * <b>Propositional Logic Visitor:</b> A <a
 * href="http://en.wikipedia.org/wiki/Visitor_pattern">Visitor Pattern/</a> for
 * traversing the abstract syntax tree structural representation of
 * propositional logic used in this library. The key difference between the
 * default Visitor pattern and the code here, is that in the former the visit()
 * methods have a void visit(ConcreteNode) signature while the visitors used
 * here have a Object visit(ConcreteNode, Object arg) signature. This simplifies
 * testing and allows some recursive code that is hard with the former .
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface PLVisitor {
	/**
	 * Visit a proposition symbol (e.g A).
	 * 
	 * @param sentence
	 *            a Sentence that is a propositional symbol.
	 * @param arg
	 *            optional argument to be used by the visitor.
	 * @return optional return value to be used by the visitor.
	 */
	Object visitPropositionSymbol(PropositionSymbol sentence, Object arg);

	/**
	 * Visit a unary complex sentence (e.g. ~A).
	 * 
	 * @param sentence
	 *            a Sentence that is a unary complex sentence.
	 * @param arg
	 *            optional argument to be used by the visitor.
	 * @return optional return value to be used by the visitor.
	 */
	Object visitUnarySentence(ComplexSentence sentence, Object arg);

	/**
	 * Visit a binary complex sentence (e.g. A & B).
	 * 
	 * @param sentence
	 *            a Sentence that is a binary complex sentence.
	 * @param arg
	 *            optional argument to be used by the visitor.
	 * @return optional return value to be used by the visitor.
	 */
	Object visitBinarySentence(ComplexSentence sentence, Object arg);
}