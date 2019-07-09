package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.impl.SimpleAgent;
import aima.core.util.Util;

import java.util.Optional;

import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_MOVE_LEFT;
import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_MOVE_RIGHT;
import static aima.core.environment.vacuum.VacuumEnvironment.ACTION_SUCK;
import static aima.core.environment.vacuum.MazeVacuumEnvironment.ACTION_MOVE_DOWN;
import static aima.core.environment.vacuum.MazeVacuumEnvironment.ACTION_MOVE_UP;

/**
 * This vacuum agent tries to clean up a checkerboard-like world of squares. By means of
 * percepts, the agent is informed about the state of the current square, about possible
 * next movement directions, and about the existence of dirty squares. After the job
 * is done, the agent moves to the up-left corner and stops acting.
 * A simple random walk strategy is used.
 *
 * @author Ruediger Lunde
 */
public class RandomWalkVacuumAgent extends SimpleAgent<VacuumPercept, Action> {

    private Action lastMoveAction;

    @Override
    public Optional<Action> act(VacuumPercept percept) {
        Action result;
        if (percept.getCurrState() == VacuumEnvironment.LocationState.Dirty) {
            result = ACTION_SUCK;
        } else {
            int bound;
            if ("True".equals(percept.getAttribute("isAllClean"))) {
                if ("A".equals(percept.getCurrLocation()))
                    return Optional.empty();
                bound = 2;
            } else if (lastMoveAction != null)
                bound = 6;
            else
                bound = 4;
            switch (Util.randomInt(bound)) {
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

            for (int i = 0; i < 2 ; i++) {
				if (result == ACTION_MOVE_UP && "False".equals(percept.getAttribute("canMoveUp")))
					result = ACTION_MOVE_LEFT;
				if (result == ACTION_MOVE_LEFT && "False".equals(percept.getAttribute("canMoveLeft")))
					result = bound != 2 ? ACTION_MOVE_DOWN : ACTION_MOVE_UP;
				if (result == ACTION_MOVE_DOWN && "False".equals(percept.getAttribute("canMoveDown")))
					result = ACTION_MOVE_RIGHT;
				if (result == ACTION_MOVE_RIGHT && "False".equals(percept.getAttribute("canMoveRight")))
					result = ACTION_MOVE_UP;
			}
			lastMoveAction = result;
        }
        return Optional.of(result);
    }
}
