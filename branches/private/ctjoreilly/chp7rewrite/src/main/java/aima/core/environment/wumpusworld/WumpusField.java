package aima.core.environment.wumpusworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Federico Baron
 * @author Alessandro Daniele
 * 
 */
public class WumpusField {

	private int dimRow;
	private HashMap<String,WumpusPosition> allowedPositions;
	
	public WumpusField(int dimRow) {
		this.dimRow = dimRow;
		allowedPositions = new HashMap<String,WumpusPosition>();
		
		for (int i=1; i<=dimRow; i++)
			for (int j=1; j<=dimRow; j++)
				for (int k=0; k<3; k++)
					allowedPositions.put(String.valueOf(i)+String.valueOf(j)+String.valueOf(k),new WumpusPosition(i, j, k));
	}
	
	public WumpusField(int dimRow, HashMap<String,WumpusPosition> allowedPositions) {
		this.dimRow = dimRow;
		this.allowedPositions = allowedPositions;
	}
	
	public List<WumpusPosition> getLocationsLinkedTo(WumpusPosition fromLocation) {
		
		int x = (int)fromLocation.getLocation().getX();
		int y = (int)fromLocation.getLocation().getY();
		int orientation = fromLocation.getOrientation();
		
		List<WumpusPosition> result = new ArrayList<WumpusPosition>();
		
		switch (orientation) {
		case WumpusPosition.ORIENTATION_DOWN:
			if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_LEFT)))
				result.add(new WumpusPosition(x, y, WumpusPosition.ORIENTATION_LEFT));
			if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_RIGHT)))
				result.add(new WumpusPosition(x, y, WumpusPosition.ORIENTATION_RIGHT));
			if (y>1)
				if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y-1)+String.valueOf(WumpusPosition.ORIENTATION_DOWN)))
					result.add(new WumpusPosition(x, y-1, WumpusPosition.ORIENTATION_DOWN));
			break;
		case WumpusPosition.ORIENTATION_UP:
			if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_LEFT)))
				result.add(new WumpusPosition(x, y, WumpusPosition.ORIENTATION_LEFT));
			if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_RIGHT)))
				result.add(new WumpusPosition(x, y, WumpusPosition.ORIENTATION_RIGHT));
			if (y<dimRow)
				if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y+1)+String.valueOf(WumpusPosition.ORIENTATION_UP)))
					result.add(new WumpusPosition(x, y+1, WumpusPosition.ORIENTATION_UP));
			break;
		case WumpusPosition.ORIENTATION_LEFT:
			if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_UP)))
				result.add(new WumpusPosition(x, y, WumpusPosition.ORIENTATION_UP));
			if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_DOWN)))
				result.add(new WumpusPosition(x, y, WumpusPosition.ORIENTATION_DOWN));
			if (x>1)
				if (allowedPositions.containsKey(String.valueOf(x-1)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_LEFT)))
					result.add(new WumpusPosition(x-1, y, WumpusPosition.ORIENTATION_LEFT));
			break;
		case WumpusPosition.ORIENTATION_RIGHT:
			if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_UP)))
				result.add(new WumpusPosition(x, y, WumpusPosition.ORIENTATION_UP));
			if (allowedPositions.containsKey(String.valueOf(x)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_DOWN)))
				result.add(new WumpusPosition(x, y, WumpusPosition.ORIENTATION_DOWN));
			if (x<dimRow)
				if (allowedPositions.containsKey(String.valueOf(x+1)+String.valueOf(y)+String.valueOf(WumpusPosition.ORIENTATION_RIGHT)))
					result.add(new WumpusPosition(x+1, y, WumpusPosition.ORIENTATION_RIGHT));
			break;
		default:
			break;
		}
		
		return result;
	}
	
	public int getDimRow() {
		return dimRow;
	}
	
	public void addAllowedPosition(WumpusPosition pos) {
		allowedPositions.put(pos.toString(), pos);
	}
	
}
