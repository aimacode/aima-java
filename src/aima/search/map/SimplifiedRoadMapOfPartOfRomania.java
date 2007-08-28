package aima.search.map;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 3.2, page 63.
 * 
 * Figure 3.7 An informal description of the general tree-search algorithm.
 * 
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 4.1, page 95.
 * 
 * Figure 4.1 Values of hSLD - straight-line distances to Bucharest.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */

public class SimplifiedRoadMapOfPartOfRomania {
	//
	// The different locations in the simplified map of part of Romania
	public static final String ORADEA = "Oradea";

	public static final String ZERIND = "Zerind";

	public static final String ARAD = "Arad";

	public static final String TIMISOARA = "Timisoara";

	public static final String LUGOJ = "Lugoj";

	public static final String MEHADIA = "Mehadia";

	public static final String DROBETA = "Drobeta";

	public static final String SIBIU = "Sibiu";

	public static final String RIMNICU_VILCEA = "Rimnicu Vilcea";

	public static final String CRAIOVA = "Craiova";

	public static final String FAGARAS = "Fagaras";

	public static final String PITESTI = "Pitesti";

	public static final String GIURGIU = "Giurgiu";

	public static final String BUCHAREST = "Bucharest";

	public static final String NEAMT = "Neamt";

	public static final String URZICENI = "Urziceni";

	public static final String IASI = "Iasi";

	public static final String VASLUI = "Vaslui";

	public static final String HIRSOVA = "Hirsova";

	public static final String EFORIE = "Eforie";

	public static final String[] LOCATIONS_IN_ROMANIA = { ORADEA, ZERIND, ARAD,
			TIMISOARA, LUGOJ, MEHADIA, DROBETA, SIBIU, RIMNICU_VILCEA, CRAIOVA,
			FAGARAS, PITESTI, GIURGIU, BUCHAREST, NEAMT, URZICENI, IASI,
			VASLUI, HIRSOVA, EFORIE };

	//
	//
	private static Map mapOfRomania = null;

	private static Map mapOfStraightLineDistancesToBucharest = null;
	//
	static {
		init();
	}

	public static Map getMapOfRomania() {
		return mapOfRomania;
	}

	public static Map getStraightLineDistancesToBucharest() {
		return mapOfStraightLineDistancesToBucharest;
	}

	//
	// PRIVATE METHODS
	//
	private static void init() {
		//
		// mapOfRomania
		mapOfRomania = new Map(LOCATIONS_IN_ROMANIA);

		mapOfRomania.addBidirectionalLink(ORADEA, ZERIND, 71);
		mapOfRomania.addBidirectionalLink(ORADEA, SIBIU, 151);

		mapOfRomania.addBidirectionalLink(ZERIND, ARAD, 75);

		mapOfRomania.addBidirectionalLink(ARAD, TIMISOARA, 118);
		mapOfRomania.addBidirectionalLink(ARAD, SIBIU, 140);

		mapOfRomania.addBidirectionalLink(TIMISOARA, LUGOJ, 111);

		mapOfRomania.addBidirectionalLink(LUGOJ, MEHADIA, 70);

		mapOfRomania.addBidirectionalLink(MEHADIA, DROBETA, 75);

		mapOfRomania.addBidirectionalLink(DROBETA, CRAIOVA, 120);

		mapOfRomania.addBidirectionalLink(SIBIU, FAGARAS, 99);
		mapOfRomania.addBidirectionalLink(SIBIU, RIMNICU_VILCEA, 80);

		mapOfRomania.addBidirectionalLink(RIMNICU_VILCEA, PITESTI, 97);
		mapOfRomania.addBidirectionalLink(RIMNICU_VILCEA, CRAIOVA, 146);

		mapOfRomania.addBidirectionalLink(CRAIOVA, PITESTI, 138);

		mapOfRomania.addBidirectionalLink(FAGARAS, BUCHAREST, 211);

		mapOfRomania.addBidirectionalLink(PITESTI, BUCHAREST, 101);

		mapOfRomania.addBidirectionalLink(GIURGIU, BUCHAREST, 90);

		mapOfRomania.addBidirectionalLink(BUCHAREST, URZICENI, 85);

		mapOfRomania.addBidirectionalLink(NEAMT, IASI, 87);

		mapOfRomania.addBidirectionalLink(URZICENI, VASLUI, 142);
		mapOfRomania.addBidirectionalLink(URZICENI, HIRSOVA, 98);

		mapOfRomania.addBidirectionalLink(IASI, VASLUI, 92);

		// mapOfRomania.addBidirectionalLink(VASLUI - already all linked

		mapOfRomania.addBidirectionalLink(HIRSOVA, EFORIE, 86);

		// mapOfRomania.addBidirectionalLink(EFORIE - already all linked

		//
		// mapOfStraightLineDistancesToBucharest
		mapOfStraightLineDistancesToBucharest = new Map(LOCATIONS_IN_ROMANIA);
		//
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(ARAD,
				BUCHAREST, 366);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(BUCHAREST,
				BUCHAREST, 0);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(CRAIOVA,
				BUCHAREST, 160);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(DROBETA,
				BUCHAREST, 242);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(EFORIE,
				BUCHAREST, 161);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(FAGARAS,
				BUCHAREST, 176);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(GIURGIU,
				BUCHAREST, 77);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(HIRSOVA,
				BUCHAREST, 151);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(IASI,
				BUCHAREST, 226);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(LUGOJ,
				BUCHAREST, 244);
		//
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(MEHADIA,
				BUCHAREST, 241);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(NEAMT,
				BUCHAREST, 234);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(ORADEA,
				BUCHAREST, 380);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(PITESTI,
				BUCHAREST, 100);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(
				RIMNICU_VILCEA, BUCHAREST, 193);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(SIBIU,
				BUCHAREST, 253);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(TIMISOARA,
				BUCHAREST, 329);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(URZICENI,
				BUCHAREST, 80);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(VASLUI,
				BUCHAREST, 199);
		mapOfStraightLineDistancesToBucharest.addBidirectionalLink(ZERIND,
				BUCHAREST, 374);
	}
}
