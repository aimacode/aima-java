package aima.core.environment.wumpusworld;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 236.<br>
 * <br>
 * The <b>wumpus world</b> is a cave consisting of rooms connected by
 * passageways. The rooms are always organized into a grid. See Figure 7.2 for
 * an example.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 */
public class WumpusCave {

	private int caveXDimension; // starts bottom left -> right
	private int caveYDimension; // starts bottom left ^ up

	private Set<AgentPosition> allowedPositions = new HashSet<AgentPosition>();
	
	/**
	 * Default Constructor. Create a Wumpus Case of default dimensions 4x4.
	 */
	public WumpusCave() {
		this(4,4);
	}

	/**
	 * Create a grid of rooms of dimensions x and y, representing the wumpus's
	 * cave.
	 * 
	 * @param caveXDimension
	 *            the cave's x dimension.
	 * @param caveYDimension
	 *            the cave's y dimension.
	 */
	public WumpusCave(int caveXDimension, int caveYDimension) {
		this(caveXDimension, caveYDimension, defaultAllowedPositions(
				caveXDimension, caveYDimension));
	}

	/**
	 * Create a grid of rooms of dimensions x and y, representing the wumpus's
	 * cave.
	 * 
	 * @param caveXDimension
	 *            the cave's x dimension.
	 * @param caveYDimension
	 *            the cave's y dimension.
	 * @param allowedPositions
	 *            the set of legal agent positions that can be reached within
	 *            the cave.
	 */
	public WumpusCave(int caveXDimension, int caveYDimension,
			Set<AgentPosition> allowedPositions) {
		if (caveXDimension < 1) {
			throw new IllegalArgumentException(
					"Cave must have x dimension >= 1");
		}
		if (caveYDimension < 1) {
			throw new IllegalArgumentException(
					"Case must have y dimension >= 1");
		}
		this.caveXDimension = caveXDimension;
		this.caveYDimension = caveYDimension;
		this.allowedPositions.addAll(allowedPositions);
	}

	public int getCaveXDimension() {
		return caveXDimension;
	}

	public int getCaveYDimension() {
		return caveYDimension;
	}

	public List<AgentPosition> getLocationsLinkedTo(AgentPosition fromLocation) {

		int x = fromLocation.getX();
		int y = fromLocation.getY();
		AgentPosition.Orientation orientation = fromLocation.getOrientation();

		List<AgentPosition> result = new ArrayList<AgentPosition>();

		AgentPosition currentForwardNorth = new AgentPosition(x, y + 1,
				AgentPosition.Orientation.FACING_NORTH);
		AgentPosition currentForwardSouth = new AgentPosition(x, y - 1,
				AgentPosition.Orientation.FACING_SOUTH);
		AgentPosition currentForwardEast = new AgentPosition(x + 1, y,
				AgentPosition.Orientation.FACING_EAST);
		AgentPosition currentForwardWest = new AgentPosition(x - 1, y,
				AgentPosition.Orientation.FACING_WEST);
		AgentPosition currentNorth = new AgentPosition(x, y,
				AgentPosition.Orientation.FACING_NORTH);
		AgentPosition currentSouth = new AgentPosition(x, y,
				AgentPosition.Orientation.FACING_SOUTH);
		AgentPosition currentEast = new AgentPosition(x, y,
				AgentPosition.Orientation.FACING_EAST);
		AgentPosition currentWest = new AgentPosition(x, y,
				AgentPosition.Orientation.FACING_WEST);

		switch (orientation) {
		case FACING_NORTH:
			addIfAllowed(currentForwardNorth, result);
			addIfAllowed(currentEast, result);
			addIfAllowed(currentWest, result);
			break;
		case FACING_SOUTH:
			addIfAllowed(currentForwardSouth, result);
			addIfAllowed(currentEast, result);
			addIfAllowed(currentWest, result);
			break;
		case FACING_EAST:
			addIfAllowed(currentNorth, result);
			addIfAllowed(currentSouth, result);
			addIfAllowed(currentForwardEast, result);
			break;
		case FACING_WEST:
			addIfAllowed(currentNorth, result);
			addIfAllowed(currentSouth, result);
			addIfAllowed(currentForwardWest, result);
			break;
		}

		return result;
	}

	//
	// PRIVATE
	//
	private static Set<AgentPosition> defaultAllowedPositions(
			int caveXDimension, int caveYDimension) {
		Set<AgentPosition> allowedPositions = new HashSet<AgentPosition>();
		// Create the default set of allowed positions within the cave that
		// an agent may occupy.
		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {
				for (AgentPosition.Orientation orientation : AgentPosition.Orientation
						.values()) {
					allowedPositions.add(new AgentPosition(x, y, orientation));
				}

			}
		}
		return allowedPositions;
	}

	private void addIfAllowed(AgentPosition position,
			List<AgentPosition> positions) {
		if (allowedPositions.contains(position)) {
			positions.add(position);
		}
	}

}
