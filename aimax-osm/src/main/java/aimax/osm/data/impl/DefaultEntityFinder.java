package aimax.osm.data.impl;

import java.util.List;
import java.util.StringTokenizer;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.OsmMap;
import aimax.osm.data.Position;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

public class DefaultEntityFinder extends AbstractEntityFinder {

	public DefaultEntityFinder(OsmMap storage) {
		super(storage);
	}

	/**
	 * Searches for entities which comply to the current search specification
	 * and stores them as results.
	 */
	@Override
	protected void find(boolean findMore) {
		BestMatchFinder bmf = new BestMatchFinder(pattern);
		List<MapEntity> results = getResults();
		BoundingBox bb = new BoundingBox(position, nextRadius);
		if (!results.isEmpty())
			bmf.checkMatchQuality(results.get(0));
		if (mode.equals(Mode.ENTITY) || mode.equals(Mode.NODE)) {
			for (MapNode node : getStorage().getPois(bb)) {
				int match = bmf.checkMatchQuality(node);
				if (match >= 0) {
					if (match > 0) {
						results.clear();
						bmf.useAsReference(node);
					}
					if (position.insertInAscendingDistanceOrder(results, node))
						if (results.size() > 100)
							results.remove(99);
				}
			}
		}
		if (mode.equals(Mode.ENTITY) || mode.equals(Mode.WAY)) {
			for (MapWay way : getStorage().getWays(bb)) {
				int match = bmf.checkMatchQuality(way);
				if (match >= 0) {
					if (match > 0) {
						results.clear();
						bmf.useAsReference(way);
					}
					if (position.insertInAscendingDistanceOrder(results, way))
						if (results.size() > 100)
							results.remove(99);
				}
			}
		}
		
		if (mode.equals(Mode.ADDRESS)) {
			List<MapEntity> iResults = getIntermediateResults();
			StringTokenizer tokenizer = new StringTokenizer(pattern, ",");
			String placeName = null;
			String wayName = null;
			if (tokenizer.hasMoreElements())
				placeName = tokenizer.nextToken();
			if (tokenizer.hasMoreElements())
				wayName = tokenizer.nextToken().trim();

			if (placeName != null && !findMore) {
				for (MapNode place : getStorage().getPlaces(placeName)) {
					position.insertInAscendingDistanceOrder(
							iResults, place);
					if (iResults.size() > 100)
						iResults.remove(99);
				}
				nextRadius = -1;
			}
			if (iResults.size() == 1 && wayName != null) {
				MapNode place = (MapNode) iResults.get(0);
				findWay(wayName, new Position(place.getLat(), place.getLon()),
						null);
			}
			
		} else {
			nextRadius *= 2;
			if (results.isEmpty() && getIntermediateResults().isEmpty() && nextRadius <= getMaxRadius())
				find(true);
		}
	}

	/**
	 * Helper class which is used to find the best match when searching for
	 * special entities.
	 * 
	 * @author Ruediger Lunde
	 */
	private static class BestMatchFinder {
		/** String, specifying the entity to be searched for. */
		String searchPattern;
		/**
		 * Value between 1 and 5 which classifies the reference match level. 1:
		 * reference entity name equal to pattern; 2: reference entity has
		 * attribute value which is identical with the pattern; 3: reference
		 * entity has attribute name equal to pattern; 4: reference entity name
		 * contains pattern; 5: no match found.
		 */
		int currMatchLevel = 5;

		/** Creates a match finder for a given search pattern. */
		protected BestMatchFinder(String pattern) {
			searchPattern = pattern.toLowerCase();
		}

		/**
		 * Compares whether a given entity matches the search pattern better or
		 * worse than the previously defined reference entity.
		 * 
		 * @return Match level (-1: forget new entity, 0: new entity match
		 *         quality equal to reference entity, 1: new entity matches
		 *         better then reference entity)
		 */
		protected int checkMatchQuality(MapEntity entity) {
			int matchLevel = getMatchLevel(entity);
			int result = -1;
			if (matchLevel < 5) {
				if (matchLevel < currMatchLevel)
					result = 1;
				else if (matchLevel == currMatchLevel) {
					result = 0;
				}
			}
			return result;
		}

		/** Defines a new reference entity for search pattern match checks. */
		protected void useAsReference(MapEntity entity) {
			currMatchLevel = getMatchLevel(entity);
		}

		private int getMatchLevel(MapEntity entity) {
			String name = entity.getName();
			if (name != null)
				name = name.toLowerCase();
			if (name != null && name.equals(searchPattern))
				return 1;
			if (currMatchLevel >= 2) {
				for (EntityAttribute att : entity.getAttributes())
					if (att.getValue().toLowerCase().equals(searchPattern))
						return 2;
			}
			if (currMatchLevel >= 3) {
				for (EntityAttribute att : entity.getAttributes())
					if (att.getKey().toLowerCase().equals(searchPattern))
						return 3;
			}
			if (name != null && currMatchLevel >= 4
					&& name.contains(searchPattern))
				return 4;
			return 5;
		}
	}
}
