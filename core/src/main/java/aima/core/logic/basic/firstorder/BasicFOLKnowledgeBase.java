package aima.core.logic.basic.firstorder;

import aima.core.logic.api.firstorderlogic.Clause;
import aima.core.logic.api.firstorderlogic.FOLKnowledgeBase;
import aima.core.logic.basic.firstorder.domain.FOLDomain;
import aima.core.logic.basic.firstorder.kb.data.Literal;
import aima.core.logic.basic.firstorder.parsing.FOLLexer;
import aima.core.logic.basic.firstorder.parsing.FOLParser;
import aima.core.logic.basic.firstorder.parsing.ast.Sentence;
import aima.core.logic.basic.firstorder.parsing.ast.Term;
import aima.core.logic.basic.firstorder.parsing.ast.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BasicFOLKnowledgeBase implements FOLKnowledgeBase {

    private List<Sentence> sentences = new ArrayList<>();
    private FOLParser parser;
    private List<Clause> allDefiniteClauseImplications = new ArrayList<>();

    public BasicFOLKnowledgeBase(FOLDomain domain){
        this.parser = new FOLParser(domain);
    }

    @Override
    public void tell(String sentence) {
        Sentence parsedSentence = parser.parse(sentence);
        sentences.add(parsedSentence);
        //TODO: A list of all the implication clauses
    }

    public int size(){
        return sentences.size();
    }

    @Override
    public void tell(Set<? extends Literal> sentences) {


    }

    @Override
    public List<Clause> getAllDefiniteClauseImplications() {
        return allDefiniteClauseImplications;
    }

    @Override
    public boolean isRenaming(Literal sentence) {
        return false;
    }

    @Override
    public boolean isRenaming(Literal sentence, Set<Literal> base) {
        return false;
    }

    @Override
    public Set<Map<Variable, Term>> fetch(Clause q) {
        return null;
    }
}
