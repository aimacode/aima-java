package aima.search.map;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 3.2, page 63.
 * Figure 3.7 An informal description of the general tree-search algorithm.
 */

/**
 * @author Ciaran O'Reilly
 *
 */
public class SimplifiedRoadMapOfPartOfRomania {
	//
	// The different locations in the simplified map of part of Romania
	public static final String ORADEA         = "Oradea";
	public static final String ZERIND         = "Zerind";
	public static final String ARAD           = "Arad";
	public static final String TIMISOARA      = "Timisoara";
	public static final String LUGOJ          = "Lugoj";
	public static final String MEHADIA        = "Mehadia";
	public static final String DROBETA        = "Drobeta";
	public static final String SIBIU          = "Sibiu";
	public static final String RIMNICU_VILCEA = "Rimnicu Vilcea";
	public static final String CRAIOVA        = "Craiova";
	public static final String FAGARAS        = "Fagaras";
	public static final String PITESTI        = "Pitesti";
	public static final String GIURGIU        = "Giurgiu";
	public static final String BUCHAREST      = "Bucharest";
	public static final String NEAMT          = "Neamt";
	public static final String URZICENI       = "Urziceni";
	public static final String IASI           = "Iasi";
	public static final String VASLUI         = "Vaslui";
	public static final String HIRSOVA        = "Hirsova";
	public static final String EFORIE         = "Eforie";
	
	public static final String[] LOCATIONS_IN_ROMANIA = {
		ORADEA,   ZERIND,  ARAD,    TIMISOARA,      LUGOJ,
		MEHADIA,  DROBETA, SIBIU,   RIMNICU_VILCEA, CRAIOVA,
		FAGARAS,  PITESTI, GIURGIU, BUCHAREST,      NEAMT,
		URZICENI, IASI,    VASLUI,  HIRSOVA,        EFORIE
		};
	
	//
	//
	private static Map _mapOfRomania = null;
	//
	static {
		_init();
	}
	
	public static Map getMapOfRomania() {
		return _mapOfRomania;
	}
	
	//
	// PRIVATE METHODS
	//
	private static void _init() {
		_mapOfRomania = new Map(LOCATIONS_IN_ROMANIA);
		
		_mapOfRomania.addBidirectionalLink(ORADEA, ZERIND, 71);
		_mapOfRomania.addBidirectionalLink(ORADEA, SIBIU,  151);
		//
		_mapOfRomania.addBidirectionalLink(ZERIND, ARAD, 75);
		//
		_mapOfRomania.addBidirectionalLink(ARAD, TIMISOARA, 118);
		_mapOfRomania.addBidirectionalLink(ARAD, SIBIU,     140);
		//
		_mapOfRomania.addBidirectionalLink(TIMISOARA, LUGOJ, 111);
		//
		_mapOfRomania.addBidirectionalLink(LUGOJ, MEHADIA, 70);
		//
		_mapOfRomania.addBidirectionalLink(MEHADIA, DROBETA, 75);
		//
		_mapOfRomania.addBidirectionalLink(DROBETA, CRAIOVA, 120);
		//
		_mapOfRomania.addBidirectionalLink(SIBIU, FAGARAS,        99);
		_mapOfRomania.addBidirectionalLink(SIBIU, RIMNICU_VILCEA, 80);
		//
		_mapOfRomania.addBidirectionalLink(RIMNICU_VILCEA, PITESTI, 97);
		_mapOfRomania.addBidirectionalLink(RIMNICU_VILCEA, CRAIOVA, 146);
		//
		_mapOfRomania.addBidirectionalLink(CRAIOVA, PITESTI, 138);
		//
		_mapOfRomania.addBidirectionalLink(FAGARAS, BUCHAREST, 211);
		//
		_mapOfRomania.addBidirectionalLink(PITESTI, BUCHAREST, 101);
		// 
		_mapOfRomania.addBidirectionalLink(GIURGIU, BUCHAREST, 90);
		//
		_mapOfRomania.addBidirectionalLink(BUCHAREST, URZICENI, 85);
		//
		_mapOfRomania.addBidirectionalLink(NEAMT, IASI, 87);
		//
		_mapOfRomania.addBidirectionalLink(URZICENI, VASLUI,  142);
		_mapOfRomania.addBidirectionalLink(URZICENI, HIRSOVA, 98);
		//
		_mapOfRomania.addBidirectionalLink(IASI, VASLUI, 92);
		//
		//_mapOfRomania.addBidirectionalLink(VASLUI - already all linked
		//
		_mapOfRomania.addBidirectionalLink(HIRSOVA, EFORIE, 86);
		//
		//_mapOfRomania.addBidirectionalLink(EFORIE - already all linked     
	}
}
