package aima.extra.logic.propositional.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.logic.basic.propositional.parsing.ast.Sentence;

public class PLParserWrapper implements PLParser {

	@Override
	public Sentence parse(String stringToBeParsed) {
		ANTLRInputStream input = new ANTLRInputStream(stringToBeParsed);
		PropositionalLogicLexer lexer = new PropositionalLogicLexer(input);
		TokenStream tokens = new CommonTokenStream(lexer);
		PropositionalLogicParser parser = new PropositionalLogicParser(tokens);
		parser.removeErrorListeners();
		ParseTree tree = parser.parse();
		Sentence s = new PropositionalVisitor().visit(tree);
		return s;
	}
}
