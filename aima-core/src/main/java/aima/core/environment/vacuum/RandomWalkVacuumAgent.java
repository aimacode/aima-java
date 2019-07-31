package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.impl.SimpleAgent;
import aima.core.util.Util;
import static aima.core.environment.vacuum.MazeVacuumEnvironment.*;

import java.util.Optional;

/**
 * This vacuum agent tries to clean up a checkerboard-like world of squares. Percepts inform
 * the agent about the state of the current square and about possible next movement directions.
 * A simple random walk strategy is used.
 *
 * @author Ruediger Lunde
 */
public class RandomWalkVacuumAgent extends SimpleAgent<VacuumPercept, Action> {

    private Action lastMoveAction;

    @Override
    public Optional<Action> act(VacuumPercept percept) {
        Action action;
        if (percept.getCurrState() == LocationState.Dirty) {
            action = ACTION_SUCK;
        } else {
        	// prefer last direction
            switch (Util.randomInt((lastMoveAction != null) ? 6 : 4)) {
                case 0:
                    action = ACTION_MOVE_UP;
                    break;
                case 1:
                    action = ACTION_MOVE_LEFT;
                    break;
                case 2:
                    action = ACTION_MOVE_DOWN;
                    break;
                case 3:
                    action = ACTION_MOVE_RIGHT;
                    break;
                default:
                    action = lastMoveAction;
            }

            // avoid obstacles
            for (int i = 0; i < 2 ; i++) {
				if (action == ACTION_MOVE_UP && "False".equals(percept.getAttribute(ATT_CAN_MOVE_UP)))
					action = ACTION_MOVE_LEFT;
				if (action == ACTION_MOVE_LEFT && "False".equals(percept.getAttribute(ATT_CAN_MOVE_LEFT)))
					action = ACTION_MOVE_DOWN;
				if (action == ACTION_MOVE_DOWN && "False".equals(percept.getAttribute(ATT_CAN_MOVE_DOWN)))
					action = ACTION_MOVE_RIGHT;
				if (action == ACTION_MOVE_RIGHT && "False".equals(percept.getAttribute(ATT_CAN_MOVE_RIGHT)))
					action = ACTION_MOVE_UP;
				else
					break;
			}
			lastMoveAction = action;
        }
        return Optional.of(action);
    }
}
