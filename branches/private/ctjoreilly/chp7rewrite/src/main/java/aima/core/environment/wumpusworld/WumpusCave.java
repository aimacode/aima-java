package aima.core.environment.wumpusworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Federico Baron
 * @author Alessandro Daniele
 * 
 */
public class WumpusCave {

	private int caveXDimension;
	private int caveYDimension;

	private HashMap<String, WumpusPosition> allowedPositions;

	public WumpusCave(int caveXDimension, int caveYDimension) {
		this.caveXDimension = caveXDimension;
		this.caveYDimension = caveYDimension;
		allowedPositions = new HashMap<String, WumpusPosition>();

		for (int x = 1; x <= caveXDimension; x++) {
			for (int y = 1; y <= caveYDimension; y++) {
				for (int k = 0; k <= 3; k++) { // orientation
					allowedPositions.put(String.valueOf(x) + String.valueOf(y)
							+ String.valueOf(k), new WumpusPosition(x, y, k));
				}

			}
		}
	}

	public WumpusCave(int caveXDimension, int caveYDimension,
			HashMap<String, WumpusPosition> allowedPositions) {
		this.caveXDimension = caveXDimension;
		this.caveYDimension = caveYDimension;
		this.allowedPositions = allowedPositions;
	}

	public List<WumpusPosition> getLocationsLinkedTo(WumpusPosition fromLocation) {

		int x = (int) fromLocation.getLocation().getX();
		int y = (int) fromLocation.getLocation().getY();
		int orientation = fromLocation.getOrientation();

		List<WumpusPosition> result = new ArrayList<WumpusPosition>();

		switch (orientation) {
		case WumpusPosition.ORIENTATION_SOUTH:
			if (allowedPositions.containsKey(String.valueOf(x)
					+ String.valueOf(y)
					+ String.valueOf(WumpusPosition.ORIENTATION_WEST))) {
				result.add(new WumpusPosition(x, y,
						WumpusPosition.ORIENTATION_WEST));
			}
			if (allowedPositions.containsKey(String.valueOf(x)
					+ String.valueOf(y)
					+ String.valueOf(WumpusPosition.ORIENTATION_EAST))) {
				result.add(new WumpusPosition(x, y,
						WumpusPosition.ORIENTATION_EAST));
			}

			if (y > 1) {
				if (allowedPositions.containsKey(String.valueOf(x)
						+ String.valueOf(y - 1)
						+ String.valueOf(WumpusPosition.ORIENTATION_SOUTH))) {
					result.add(new WumpusPosition(x, y - 1,
							WumpusPosition.ORIENTATION_SOUTH));
				}
			}
			break;
		case WumpusPosition.ORIENTATION_NORTH:
			if (allowedPositions.containsKey(String.valueOf(x)
					+ String.valueOf(y)
					+ String.valueOf(WumpusPosition.ORIENTATION_WEST))) {
				result.add(new WumpusPosition(x, y,
						WumpusPosition.ORIENTATION_WEST));
			}
			if (allowedPositions.containsKey(String.valueOf(x)
					+ String.valueOf(y)
					+ String.valueOf(WumpusPosition.ORIENTATION_EAST))) {
				result.add(new WumpusPosition(x, y,
						WumpusPosition.ORIENTATION_EAST));
			}
			if (y < caveYDimension) {
				if (allowedPositions.containsKey(String.valueOf(x)
						+ String.valueOf(y + 1)
						+ String.valueOf(WumpusPosition.ORIENTATION_NORTH))) {
					result.add(new WumpusPosition(x, y + 1,
							WumpusPosition.ORIENTATION_NORTH));
				}
			}
			break;
		case WumpusPosition.ORIENTATION_WEST:
			if (allowedPositions.containsKey(String.valueOf(x)
					+ String.valueOf(y)
					+ String.valueOf(WumpusPosition.ORIENTATION_NORTH))) {
				result.add(new WumpusPosition(x, y,
						WumpusPosition.ORIENTATION_NORTH));
			}
			if (allowedPositions.containsKey(String.valueOf(x)
					+ String.valueOf(y)
					+ String.valueOf(WumpusPosition.ORIENTATION_SOUTH))) {
				result.add(new WumpusPosition(x, y,
						WumpusPosition.ORIENTATION_SOUTH));
			}
			if (x > 1) {
				if (allowedPositions.containsKey(String.valueOf(x - 1)
						+ String.valueOf(y)
						+ String.valueOf(WumpusPosition.ORIENTATION_WEST))) {
					result.add(new WumpusPosition(x - 1, y,
							WumpusPosition.ORIENTATION_WEST));
				}
			}
			break;
		case WumpusPosition.ORIENTATION_EAST:
			if (allowedPositions.containsKey(String.valueOf(x)
					+ String.valueOf(y)
					+ String.valueOf(WumpusPosition.ORIENTATION_NORTH))) {
				result.add(new WumpusPosition(x, y,
						WumpusPosition.ORIENTATION_NORTH));
			}
			if (allowedPositions.containsKey(String.valueOf(x)
					+ String.valueOf(y)
					+ String.valueOf(WumpusPosition.ORIENTATION_SOUTH))) {
				result.add(new WumpusPosition(x, y,
						WumpusPosition.ORIENTATION_SOUTH));
			}
			if (x < caveXDimension) {
				if (allowedPositions.containsKey(String.valueOf(x + 1)
						+ String.valueOf(y)
						+ String.valueOf(WumpusPosition.ORIENTATION_EAST))) {
					result.add(new WumpusPosition(x + 1, y,
							WumpusPosition.ORIENTATION_EAST));
				}
			}
			break;
		default:
			break;
		}

		return result;
	}

	public int getCaveXDimension() {
		return caveXDimension;
	}

	public int getCaveYDimension() {
		return caveYDimension;
	}

	public void addAllowedPosition(WumpusPosition pos) {
		allowedPositions.put(pos.toString(), pos);
	}

}
