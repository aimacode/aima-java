package aima.core.logic.api.firstorderlogic;

import aima.core.logic.basic.firstorder.kb.data.Literal;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FOLKnowledgeBase {
    /**
     *
     * @param sentence
     */
    void tell(String sentence);

    /**
     *
     * @param sentences
     */
    void tell(Set<? extends Literal> sentences);

    /**
     *
     * @return
     */
    List<Clause> getAllDefiniteClauseImplications();

    boolean isRenaming(Literal sentence);
    boolean isRenaming(Literal sentence,Set<Literal> base);

    Set<Map<Variable,Term>> fetch(Clause q);



}
