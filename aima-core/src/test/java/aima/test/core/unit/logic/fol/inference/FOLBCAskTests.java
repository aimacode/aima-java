package aima.test.core.unit.logic.fol.inference;

import aima.core.logic.fol.Unifier;
import aima.core.logic.fol.inference.FOLBCAsk;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.*;
import aima.core.logic.planning.Utils;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FOLBCAskTests {
    @Test
    public void folbcAsktest(){
        FOLBCAsk folbcAsk = new FOLBCAsk();
        FOLKnowledgeBase weaponsKb = FOLKnowledgeBaseFactory.createWeaponsKnowledgeBase(folbcAsk);
        Literal query = new Literal(new Predicate("Criminal",Collections.singletonList(new Variable("x"))));
        List<HashMap<Variable,Term>> results = folbcAsk.folBcAsk(weaponsKb,query);
        List<Literal> substituted = folbcAsk.getSubstitutedLiterals();
        System.out.println("Results!");
        System.out.println(results.toString());
        System.out.println("Final");
        System.out.println(folbcAsk.ask(weaponsKb,query.getAtomicSentence()).getProofs().get(0).getSteps().toString());

        System.out.println((new Unifier().unify(new Predicate("King",Collections.singletonList(new Variable("x"))),
                new Predicate("King",Collections.singletonList(new Constant("George"))))).toString());
        System.out.println("Kings Base");
        query = new Literal(new Predicate("King",Collections.singletonList(new Variable("x"))));
        FOLKnowledgeBase kings = FOLKnowledgeBaseFactory.createKingsKnowledgeBase(folbcAsk);
        results = folbcAsk.folBcAsk(kings,query);
        System.out.println(results.toString());


    }
}
