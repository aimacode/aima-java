package aima.gui.applications.search.csp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Hashtable;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Variable;
import aima.gui.framework.EmptyEnvironmentView;

/**
 * Cooperates with a {@link CSPEnvironment} and visualizes its state.
 * The viewer shows the structure of the CSP network, the current state
 * of all domains, and the current assignment.
 * 
 * @author Ruediger Lunde
 */
public class CSPView extends EmptyEnvironmentView {
	private static final long serialVersionUID = 1L;
	
	/** Maintains logical 2D-coordinates for the variables of the CSP. */
	protected Hashtable<Variable, int[]> positionMapping = new Hashtable<Variable, int[]>();
	/** Maps domain values to colors. */
	protected Hashtable<Object, Color> colorMapping = new Hashtable<Object, Color>();

	public void clearMappings() {
		positionMapping.clear();
		colorMapping.clear();
	}
	
	/**
	 * Defines a logical 2D-position for a variable. If no position is given
	 * for a certain variable, the viewer selects a position on a grid.
	 */
	public void setPositionMapping(Variable var, int x, int y) {
		positionMapping.put(var, new int[]{x, y});
	}
	
	/**
	 * Defines a color for a domain value. It is used to visualize the
	 * current assignment. If no color mapping is found,
	 * the node coloring feature is disabled for the corresponding variable.
	 * @param value
	 * @param color
	 */
	public void setColorMapping(Object value, Color color) {
		colorMapping.put(value, color);
	}
	
	/** Agent and resulting state may be null. */
	@Override
	public void agentActed(Agent agent, Action action,
			EnvironmentState resultingState) {
		super.agentAdded(agent, resultingState);
		notify(action.toString());
	}

	@Override
	public void agentAdded(Agent agent, EnvironmentState resultingState) {
		super.agentAdded(agent, resultingState);
	}
	
	@Override
	public void paint(Graphics g) {
		int maxX = 1;
		int maxY = 1;
		for (int[] pos : positionMapping.values()) {
			maxX = Math.max(maxX, pos[0]);
			maxY = Math.max(maxY, pos[1]);
		}
		adjustTransformation(0, 0, maxX*3/2, maxY*4/3);
		Graphics2D g2 = (Graphics2D) g;
		g2.setBackground(Color.white);
		g2.clearRect(0, 0, getWidth(), getHeight());
		CSP csp = getCSP();
		if (csp != null) {
			for (Constraint constraint : csp.getConstraints())
				drawConstraint(g2, constraint);
			for (Variable var : csp.getVariables())
				drawVariable(g2, var);
		}
	}
	
	protected void drawConstraint(Graphics2D g2, Constraint constraint) {
		List<Variable> scope = constraint.getScope();
		if (scope.size() == 2) { // we show only binary constraints...
			int[] pos0 = getPosition(scope.get(0));
			int[] pos1 = getPosition(scope.get(1));
			g2.drawLine(pos0[0]+20, pos0[1]+20, pos1[0]+20, pos1[1]+20);
		}
	}
	
	protected void drawVariable(Graphics2D g2, Variable var) {
		int[] pos = getPosition(var);
		String label = var.getName();
		Object value = null;
		Color fillColor = null;
		Assignment assignment = ((CSPEnvironment) env).getAssignment();
		if (assignment != null)
			value = assignment.getAssignment(var);
		if (value != null) {
			label += " = " + value;
			fillColor = colorMapping.get(value);
		}
		g2.setColor(fillColor != null ? fillColor : Color.WHITE);
		g2.fillOval(pos[0], pos[1], 40, 40);
		g2.setColor(Color.BLACK);
		g2.drawOval(pos[0], pos[1], 40, 40);
		g2.drawString(label, pos[0], pos[1]);
		g2.drawString(getCSP().getDomain(var).toString(), pos[0], pos[1]+52);
	}
	
	protected int[] getPosition(Variable var) {
		int[] pos = positionMapping.get(var);
		if (pos != null)
			return new int[]{x(pos[0]), y(pos[1])}; 
		else {
			int vIndex = getCSP().indexOf(var);
			int rows = getHeight() / 80;
			int x = (vIndex / rows) * 160 + 20;
			int y = (vIndex % rows) * 80 + 20;
			return new int[]{x, y};
		}
	}
	
	protected CSP getCSP() {
		return ((CSPEnvironment) env).getCSP();
	}
}
