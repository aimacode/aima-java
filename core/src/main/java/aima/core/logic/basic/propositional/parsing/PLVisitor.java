package aima.core.logic.basic.propositional.parsing;

import aima.core.logic.basic.propositional.parsing.ast.ComplexSentence;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;

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
 * 
 * @param <A>
 *            the argument type to be passed to the visitor methods.
 * @param <R>
 *            the return type to be returned from the visitor methods.
 */
public interface PLVisitor<A, R> {
	/**
	 * Visit a proposition symbol (e.g A).
	 * 
	 * @param sentence
	 *            a Sentence that is a propositional symbol.
	 * @param arg
	 *            optional argument to be used by the visitor.
	 * @return optional return value to be used by the visitor.
	 */
	R visitPropositionSymbol(PropositionSymbol sentence, A arg);

	/**
	 * Visit a unary complex sentence (e.g. ~A).
	 * 
	 * @param sentence
	 *            a Sentence that is a unary complex sentence.
	 * @param arg
	 *            optional argument to be used by the visitor.
	 * @return optional return value to be used by the visitor.
	 */
	R visitUnarySentence(ComplexSentence sentence, A arg);

	/**
	 * Visit a binary complex sentence (e.g. A & B).
	 * 
	 * @param sentence
	 *            a Sentence that is a binary complex sentence.
	 * @param arg
	 *            optional argument to be used by the visitor.
	 * @return optional return value to be used by the visitor.
	 */
	R visitBinarySentence(ComplexSentence sentence, A arg);
}