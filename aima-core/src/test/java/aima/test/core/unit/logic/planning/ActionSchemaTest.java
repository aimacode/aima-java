package aima.test.core.unit.logic.planning;

import aima.core.logic.fol.kb.data.Literal;
import aima.core.logic.fol.parsing.ast.Constant;
import aima.core.logic.fol.parsing.ast.Predicate;
import aima.core.logic.fol.parsing.ast.Term;
import aima.core.logic.fol.parsing.ast.Variable;
import aima.core.logic.planning.ActionSchema;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author samagra
 */
public class ActionSchemaTest {
    ActionSchema concreteFlyAction;
    private List<Term> flyVars;

    @Before
    public void setup() {
        Variable p = new Variable("p");
        Variable from = new Variable("from");
        Variable to = new Variable("to");
        flyVars = Arrays.asList(p, from, to);
    }

    @Test
    public void actionSchemaConstructorTest() {
        //fly action
        ActionSchema flyAction = new ActionSchema("Fly", flyVars,
                "At(p,from)^Plane(p)^Airport(from)^Airport(to)",
                "~At(p,from)^At(p,to)");
        Assert.assertEquals("Fly", flyAction.getName());
        Assert.assertEquals(3, flyAction.getVariables().size());
        Assert.assertEquals(flyVars, flyAction.getVariables());
        Literal preCondOne = new Literal(new Predicate("At",
                Arrays.asList(new Variable("p"), new Variable("from"))));
        Literal preCondTwo = new Literal(
                new Predicate("Plane",
                        Collections.singletonList(new Variable("p"))));
        Literal preCondThree = new Literal(new
                Predicate("Airport", Collections.singletonList(new Variable("from"))));
        Literal preCondFour = new Literal(new
                Predicate("Airport", Collections.singletonList(new Variable("to"))));
        List<Literal> preCond = Arrays.asList(preCondOne, preCondTwo, preCondThree, preCondFour);
        Assert.assertEquals(preCondOne, flyAction.getPrecondition().get(0));
        Assert.assertEquals(preCondTwo, flyAction.getPrecondition().get(1));
        Assert.assertEquals(preCondThree, flyAction.getPrecondition().get(2));
        Assert.assertEquals(preCondFour, flyAction.getPrecondition().get(3));
        Assert.assertEquals(preCond, flyAction.getPrecondition());
        Literal effectOne = new Literal(new Predicate("At",
                Arrays.asList(new Variable("p"), new Variable("from"))), true);
        Literal effectTwo = new Literal(new Predicate("At",
                Arrays.asList(new Variable("p"), new Variable("to"))));
        List<Literal> effects = Arrays.asList(effectOne, effectTwo);
        List<Literal> posEffects = Arrays.asList(effectTwo);
        List<Literal> negEffects = Arrays.asList(effectOne);
        Assert.assertEquals(effectOne, flyAction.getEffects().get(0));
        Assert.assertEquals(effectTwo, flyAction.getEffects().get(1));
        Assert.assertEquals(effects, flyAction.getEffects());
        Assert.assertEquals(posEffects, flyAction.getEffectsPositiveLiterals());
        Assert.assertEquals(negEffects, flyAction.getEffectsNegativeLiterals());
    }

    @Test
    public void concreteActionTest() {
        concreteFlyAction = new ActionSchema("Fly", null,
                "At(P1,SFO)^Plane(P1)^Airport(SFO)^Airport(JFK)",
                "~At(P1,SFO)^At(P1,JFK)");
        Literal preCondOne = new Literal(new Predicate("At",
                Arrays.asList(new Constant("P1"), new Constant("SFO"))));
        Literal preCondTwo = new Literal(
                new Predicate("Plane",
                        Collections.singletonList(new Constant("P1"))));
        Literal preCondThree = new Literal(new
                Predicate("Airport", Collections.singletonList(new Constant("SFO"))));
        Literal preCondFour = new Literal(new
                Predicate("Airport", Collections.singletonList(new Constant("JFK"))));
        List<Literal> preCond = Arrays.asList(preCondOne, preCondTwo, preCondThree, preCondFour);
        Assert.assertEquals(preCondOne, concreteFlyAction.getPrecondition().get(0));
        Assert.assertEquals(preCondTwo, concreteFlyAction.getPrecondition().get(1));
        Assert.assertEquals(preCondThree, concreteFlyAction.getPrecondition().get(2));
        Assert.assertEquals(preCondFour, concreteFlyAction.getPrecondition().get(3));
        Assert.assertEquals(preCond, concreteFlyAction.getPrecondition());
        Literal effectOne = new Literal(new Predicate("At",
                Arrays.asList(new Constant("P1"), new Constant("SFO"))), true);
        Literal effectTwo = new Literal(new Predicate("At",
                Arrays.asList(new Constant("P1"), new Constant("JFK"))));
        List<Literal> effects = Arrays.asList(effectOne, effectTwo);
        List<Literal> posEffects = Arrays.asList(effectTwo);
        List<Literal> negEffects = Arrays.asList(effectOne);
        Assert.assertTrue(effectOne.equals(concreteFlyAction.getEffects().get(0)));
        Assert.assertEquals(effectTwo, concreteFlyAction.getEffects().get(1));
        Assert.assertEquals(effects, concreteFlyAction.getEffects());
        Assert.assertEquals(posEffects, concreteFlyAction.getEffectsPositiveLiterals());
        Assert.assertEquals(negEffects, concreteFlyAction.getEffectsNegativeLiterals());

    }

    @Test
    public void actionSubstitutionTest() {
        ActionSchema flyAction = new ActionSchema("Fly", flyVars,
                "At(p,from)^Plane(p)^Airport(from)^Airport(to)",
                "~At(p,from)^At(p,to)");
        Constant P1 = new Constant("P1");
        Constant SFO = new Constant("SFO");
        Constant JFK = new Constant("JFK");
        concreteFlyAction = new ActionSchema("Fly", null,
                "At(P1,SFO)^Plane(P1)^Airport(SFO)^Airport(JFK)",
                "~At(P1,SFO)^At(P1,JFK)");
        ActionSchema newAction = flyAction.getActionBySubstitution(Arrays.asList(P1, SFO, JFK));
        Assert.assertEquals(concreteFlyAction, newAction);
    }
}
