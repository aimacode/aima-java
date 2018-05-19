package aima.core.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Utils for parsing planning problems.
 *
 * @author samagra
 */
public class Utils {

    public static List<Literal> parse(String s) {
        if (s.isEmpty())
            return new ArrayList<>();
        s = s.replaceAll("\\s+", "");
        String[] tokens = s.split("\\^");
        Literal literal;
        ArrayList<Literal> literals = new ArrayList<>();
        for (String token :
                tokens) {
            String[] terms = token.split("[(,)]");
            ArrayList<Term> literalTerms = new ArrayList<>();
            Term term;
            String termString;
            Boolean negated = false;
            for (int i = 1; i < terms.length; i++) {
                termString = terms[i];
                if (termString.equals(termString.toLowerCase())) {
                    term = new Variable(termString);
                } else {
                    term = new Constant(termString);
                }
                literalTerms.add(term);
            }

            String name = terms[0];
            if (name.charAt(0) == '~') {
                negated = true;
                name = name.substring(1);
            }
            literal = new Literal(new Predicate(name, literalTerms), negated);
            literals.add(literal);
        }
        return literals;
    }
}
