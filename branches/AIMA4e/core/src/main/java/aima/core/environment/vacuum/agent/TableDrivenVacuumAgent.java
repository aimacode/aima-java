package aima.core.environment.vacuum.agent;

import aima.core.agent.BasicTableDrivenAgent;
import aima.core.api.agent.Action;
import aima.core.api.agent.Percept;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.core.environment.vacuum.perceive.LocalPercept;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 * Figure ?? Partial tabulation of a simple agent function for the
 * vacuum-cleaner world shown in Figure ??.
 *
 * @author Ciaran O'Reilly
 */
public class TableDrivenVacuumAgent extends BasicTableDrivenAgent<LocalPercept> {
    public TableDrivenVacuumAgent() {
        super(generatePartialTabulationOfActionFunction());
    }


    public static Map<List<Percept>, Action>  generatePartialTabulationOfActionFunction() {
        Map<List<Percept>, Action> perceptSequenceToAction = new HashMap<>();

        // NOTE: While this particular table could be setup simply
        // using a few loops, the intent is to show how quickly a table
        // based approach grows and becomes unusable.
        
        //
        // Level 1: 4 states
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);

        //
        // Level 2: 4x4 states
        // 1
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 2
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 3
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 4
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);

        //
        // Level 3: 4x4x4 states
        // 1-1
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 1-2
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 1-3
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 1-4
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 2-1
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 2-2
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 2-3
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 2-4
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 3-1
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 3-2
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 3-3
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 3-4
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 4-1
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 4-2
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 4-3
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        // 4-4
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Right);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_A,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Clean)), VacuumEnvironment.Left);
        perceptSequenceToAction.put(Arrays.asList(new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty), new LocalPercept(
                VacuumEnvironment.LOCATION_B,
                VacuumEnvironment.Status.Dirty)), VacuumEnvironment.Suck);

        //
        // Level 4: 4x4x4x4 states
        // ...

        return perceptSequenceToAction;

    }
}
