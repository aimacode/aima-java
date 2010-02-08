package aimax.osm.routing;

import aima.core.agent.Action;
import aima.core.search.framework.ResultFunction;

public class OsmResultFunction implements ResultFunction {
	@Override
	public Object result(Object s, Action a) {
		if (a instanceof OsmMoveAction)
			return ((OsmMoveAction) a).getTo();
		else
			return s;
	}
}
