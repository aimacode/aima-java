package aimax.osm.routing;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ResultFunction;

/**
 * Returns the end node of the movement if an <code>OsmMoveAction</code>
 * is provided, otherwise returns the unchanged state. 
 * @author Ruediger Lunde
 */
public class OsmResultFunction implements ResultFunction {
	@Override
	public Object result(Object s, Action a) {
		if (a instanceof OsmMoveAction)
			return ((OsmMoveAction) a).getTo();
		else
			return s;
	}
}
