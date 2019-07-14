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
        Action result;
        if (percept.getCurrState() == LocationState.Dirty) {
            result = ACTION_SUCK;
        } else {
        	// prefer last direction
            switch (Util.randomInt((lastMoveAction != null) ? 6 : 4)) {
                case 0:
                    result = ACTION_MOVE_UP;
                    break;
                case 1:
                    result = ACTION_MOVE_LEFT;
                    break;
                case 2:
                    result = ACTION_MOVE_DOWN;
                    break;
                case 3:
                    result = ACTION_MOVE_RIGHT;
                    break;
                default:
                    result = lastMoveAction;
            }

            // avoid obstacles
            for (int i = 0; i < 2 ; i++) {
				if (result == ACTION_MOVE_UP && "False".equals(percept.getAttribute(CAN_MOVE_UP)))
					result = ACTION_MOVE_LEFT;
				if (result == ACTION_MOVE_LEFT && "False".equals(percept.getAttribute(CAN_MOVE_LEFT)))
					result = ACTION_MOVE_DOWN;
				if (result == ACTION_MOVE_DOWN && "False".equals(percept.getAttribute(CAN_MOVE_DOWN)))
					result = ACTION_MOVE_RIGHT;
				if (result == ACTION_MOVE_RIGHT && "False".equals(percept.getAttribute(CAN_MOVE_RIGHT)))
					result = ACTION_MOVE_UP;
				else
					break;
			}
			lastMoveAction = result;
        }
        return Optional.of(result);
    }
}
