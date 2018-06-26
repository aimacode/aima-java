package aima.test.core.unit.logic.fol.inference;

import aima.core.logic.fol.inference.FOLBCAsk;
import aima.core.logic.fol.kb.FOLKnowledgeBase;
import aima.core.logic.fol.kb.FOLKnowledgeBaseFactory;
import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.*;
import aima.core.logic.planning.Utils;
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
        System.out.println("Results!!!!!!!!!!");
        System.out.println(results.toString());
        Assert.assertTrue(substituted.contains(Utils.parse("American(West)").get(0)));
        Assert.assertTrue(substituted.contains(Utils.parse("Missile(M1)").get(0)));
        Assert.assertTrue(substituted.contains(Utils.parse("Weapon(M1)").get(0)));
        Assert.assertTrue(substituted.contains(Utils.parse("Missile(M1)").get(0)));
        Assert.assertTrue(substituted.contains(Utils.parse("Owns(Nono,M1)").get(0)));
        Assert.assertTrue(substituted.contains(Utils.parse("Sells(West,M1,Nono)").get(0)));
        Assert.assertTrue(substituted.contains(Utils.parse("Enemy(Nono,America)").get(0)));
        Assert.assertTrue(substituted.contains(Utils.parse("Hostile(Nono)").get(0)));
        System.out.println("Finallllllllll");
        System.out.println(folbcAsk.ask(weaponsKb,query.getAtomicSentence()).getProofs().get(0).getSteps().toString());
    }
}
