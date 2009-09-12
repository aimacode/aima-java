package aima.search.map;

/**
 * Represents a simplified road map of Romania. The initialization
 * method is declared static. So it can also be used to initialize other 
 * specialized subclasses of {@link MapWithSLD} with road map data from
 * Australia. Location names and road distances are taken from class
 * <code>aima.search.map.SimplifiedRoadMapOfPartOfRomania</code>. Placement
 * positions are taken from the figure in the text book.
 * @author R. Lunde
 */
public class SimplifiedRoadMapOfRomania extends MapWithSLD {
	
	public SimplifiedRoadMapOfRomania() {
		initMap(this);
	}
	
	// The different locations in the simplified map of part of Romania
	public static final String ORADEA = "Oradea";
	public static final String ZERIND = "Zerind";
	public static final String ARAD = "Arad";
	public static final String TIMISOARA = "Timisoara";
	public static final String LUGOJ = "Lugoj";
	public static final String MEHADIA = "Mehadia";
	public static final String DOBRETA = "Dobreta";
	public static final String SIBIU = "Sibiu";
	public static final String RIMNICU_VILCEA = "RimnicuVilcea";
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

	/**
	 * Initializes a map with a simplified road map of Romania.
	 */
	public static void initMap(MapWithSLD map) {
		// mapOfRomania
		map.clear();
		map.addBidirectionalLink(ORADEA, ZERIND, 71);
		map.addBidirectionalLink(ORADEA, SIBIU, 151);
		map.addBidirectionalLink(ZERIND, ARAD, 75);
		map.addBidirectionalLink(ARAD, TIMISOARA, 118);
		map.addBidirectionalLink(ARAD, SIBIU, 140);
		map.addBidirectionalLink(TIMISOARA, LUGOJ, 111);
		map.addBidirectionalLink(LUGOJ, MEHADIA, 70);
		map.addBidirectionalLink(MEHADIA, DOBRETA, 75);
		map.addBidirectionalLink(DOBRETA, CRAIOVA, 120);
		map.addBidirectionalLink(SIBIU, FAGARAS, 99);
		map.addBidirectionalLink(SIBIU, RIMNICU_VILCEA, 80);
		map.addBidirectionalLink(RIMNICU_VILCEA, PITESTI, 97);
		map.addBidirectionalLink(RIMNICU_VILCEA, CRAIOVA, 146);
		map.addBidirectionalLink(CRAIOVA, PITESTI, 138);
		map.addBidirectionalLink(FAGARAS, BUCHAREST, 211);
		map.addBidirectionalLink(PITESTI, BUCHAREST, 101);
		map.addBidirectionalLink(GIURGIU, BUCHAREST, 90);
		map.addBidirectionalLink(BUCHAREST, URZICENI, 85);
		map.addBidirectionalLink(NEAMT, IASI, 87);
		map.addBidirectionalLink(URZICENI, VASLUI, 142);
		map.addBidirectionalLink(URZICENI, HIRSOVA, 98);
		map.addBidirectionalLink(IASI, VASLUI, 92);
		// addBidirectionalLink(VASLUI - already all linked
		map.addBidirectionalLink(HIRSOVA, EFORIE, 86);
		// addBidirectionalLink(EFORIE - already all linked

		// distances and directions
		// reference location: Bucharest
		map.setDistAndDirToRefLocation(ARAD, 366, 117);
		map.setDistAndDirToRefLocation(BUCHAREST, 0, 360);
		map.setDistAndDirToRefLocation(CRAIOVA, 160, 74);
		map.setDistAndDirToRefLocation(DOBRETA, 242, 82); // !
		map.setDistAndDirToRefLocation(EFORIE, 161, 282);
		map.setDistAndDirToRefLocation(FAGARAS, 176, 142);
		map.setDistAndDirToRefLocation(GIURGIU, 77, 25);
		map.setDistAndDirToRefLocation(HIRSOVA, 151, 260);
		map.setDistAndDirToRefLocation(IASI, 226, 202);
		map.setDistAndDirToRefLocation(LUGOJ, 244, 102);
		map.setDistAndDirToRefLocation(MEHADIA, 241, 92);
		map.setDistAndDirToRefLocation(NEAMT, 234, 181);
		map.setDistAndDirToRefLocation(ORADEA, 380, 131);
		map.setDistAndDirToRefLocation(PITESTI, 100, 116);
		map.setDistAndDirToRefLocation(RIMNICU_VILCEA, 193, 115);
		map.setDistAndDirToRefLocation(SIBIU, 253, 123);
		map.setDistAndDirToRefLocation(TIMISOARA, 329, 105);
		map.setDistAndDirToRefLocation(URZICENI, 80, 247);
		map.setDistAndDirToRefLocation(VASLUI, 199, 222);
		map.setDistAndDirToRefLocation(ZERIND, 374, 125);
	}
}
