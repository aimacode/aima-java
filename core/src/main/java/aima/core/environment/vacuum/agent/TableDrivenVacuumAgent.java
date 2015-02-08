package aima.core.environment.vacuum.agent;

import aima.core.agent.AbstractTableDrivenAgent;
import aima.core.api.agent.Action;
import aima.core.api.agent.Percept;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.perceive.LocalPercept;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 * Figure ?? Partial tabulation of a simple agent function for the
 * vacuum-cleaner world shown in Figure ??.
 *
 * @author Ciaran O'Reilly
 */
public class TableDrivenVacuumAgent extends AbstractTableDrivenAgent<LocalPercept> {
    public TableDrivenVacuumAgent() {
        super(generatePartialTabulationOfActionFunction());
    }


    public static Map<List<Percept>, Action>  generatePartialTabulationOfActionFunction() {
        Map<List<Percept>, Action> perceptSequenceToAction = new HashMap<>();

        // NOTE: While this particular table could be setup simply
        // using a few loops, the intent is to show how quickly a table
        // based approach grows and becomes unusable.
        List<Percept> ps;
        //
        // Level 1: 4 states
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);

        //
        // Level 2: 4x4 states
        // 1
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 2
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 3
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 4
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);

        //
        // Level 3: 4x4x4 states
        // 1-1
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 1-2
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 1-3
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 1-4
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 2-1
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 2-2
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 2-3
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 2-4
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 3-1
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 3-2
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 3-3
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 3-4
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 4-1
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 4-2
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 4-3
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        // 4-4
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Right);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Left);
        ps = createPerceptSequence(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty));
        perceptSequenceToAction.put(ps, VacuumEnvironment.Suck);

        //
        // Level 4: 4x4x4x4 states
        // ...

        return perceptSequenceToAction;

    }

    private static List<Percept> createPerceptSequence(Percept... percepts) {
        List<Percept> perceptSequence = new ArrayList<>();

        for (Percept p : percepts) {
            perceptSequence.add(p);
        }

        return perceptSequence;
    }
}
