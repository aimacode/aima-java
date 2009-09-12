package aima.search.map;


/**
 * Represents a simplified road map of Australia. The initialization
 * method is declared static. So it can also be used to initialize other 
 * specialized subclasses of {@link MapWithSLD} with road map data from
 * Australia. The data was extracted from a class developed by
 * Felix Knittel. 
 * @author R. Lunde
 */
public class SimplifiedRoadMapOfAustralia extends MapWithSLD {
	
	public SimplifiedRoadMapOfAustralia() {
		initMap(this);
	}
	
	// Locations
	public static final String ADELAIDE      = "Adelaide";
	public static final String ALBANY        = "Albany";
	public static final String ALICE_SPRINGS = "AliceSprings";
	public static final String BRISBANE      = "Brisbane";
	public static final String BROKEN_HILL   = "BrokenHill";
	public static final String BROOME        = "Broome";
	public static final String CAIRNS        = "Cairns";
	public static final String CAMARVON      = "Camarvon";
	public static final String CANBERRA      = "Canberra";
	public static final String CHARLEVILLE   = "Charleville";
	public static final String COOBER_PEDY   = "CooberPedy";
	public static final String DARWIN        = "Darwin";
	public static final String DUBBO         = "Dubbo";
	public static final String ESPERANCE     = "Esperance";
	public static final String GERALDTON     = "Geraldton";
	public static final String HALLS_CREEK   = "HallsCreek";
	public static final String HAY           = "Hay";
	public static final String KALGOORLIE    = "Kalgoorlie";
	public static final String KATHERINE     = "Katherine";
	public static final String LAKES_ENTRANCE= "LakesEntrance";
	public static final String LONGREACH     = "Longreach";
	public static final String MACKAY        = "Mackay";
	public static final String MELBOURNE     = "Melbourne";
	public static final String MOUNT_GAMBIER = "MountGambier";
	public static final String MT_ISA        = "MtIsa";
	public static final String NEWCASTLE     = "Newcastle";
	public static final String NORSEMAN      = "Norseman";
	public static final String NYNGAN        = "Nyngan";
	public static final String PERTH         = "Perth";
	public static final String PORT_AUGUSTA  = "PortAugusta";
	public static final String PORT_HEDLAND  = "PortHedland";
	public static final String PORT_LINCOLN  = "PortLincoln";
	public static final String PORT_MACQUARIE= "PortMacquarie";
	public static final String ROCKHAMPTON   = "Rockhampton";
	public static final String SYDNEY        = "Sydney";
	public static final String TAMWORTH      = "Tamworth";
	public static final String TENNANT_CREEK = "TennantCreek";
	public static final String TOWNSVILLE    = "Townsville";
	public static final String WAGGA_WAGGA   = "WaggaWagga";
	public static final String WARNAMBOOL    = "Warnambool";
	public static final String WYNDHAM       = "Wyndham";
	
	/**
	 * Initializes a map with a simplified road map of Australia.
	 */
	public static void initMap(MapWithSLD map) {
		map.clear();
		// Add links
		// Distances from http://maps.google.com
		map.addBidirectionalLink(PERTH, ALBANY, 417);
		map.addBidirectionalLink(PERTH, KALGOORLIE, 593);
		map.addBidirectionalLink(PERTH, GERALDTON, 424);
		map.addBidirectionalLink(PERTH, PORT_HEDLAND, 1637);
		map.addBidirectionalLink(ALBANY, ESPERANCE, 478);
		map.addBidirectionalLink(KALGOORLIE, NORSEMAN, 187);
		map.addBidirectionalLink(ESPERANCE, NORSEMAN, 204);
		map.addBidirectionalLink(NORSEMAN, PORT_AUGUSTA, 1668);
		map.addBidirectionalLink(GERALDTON, CAMARVON, 479);
		map.addBidirectionalLink(CAMARVON, PORT_HEDLAND, 872);
		map.addBidirectionalLink(PORT_HEDLAND, BROOME, 589);
		map.addBidirectionalLink(BROOME, HALLS_CREEK, 685);
		map.addBidirectionalLink(HALLS_CREEK, WYNDHAM, 370);
		map.addBidirectionalLink(HALLS_CREEK, KATHERINE, 874);
		map.addBidirectionalLink(WYNDHAM, KATHERINE, 613);
		map.addBidirectionalLink(KATHERINE, DARWIN, 317);
		map.addBidirectionalLink(KATHERINE, TENNANT_CREEK, 673);
		map.addBidirectionalLink(TENNANT_CREEK, MT_ISA, 663);
		map.addBidirectionalLink(TENNANT_CREEK, ALICE_SPRINGS, 508);
		map.addBidirectionalLink(ALICE_SPRINGS, COOBER_PEDY, 688);
		map.addBidirectionalLink(COOBER_PEDY, PORT_AUGUSTA, 539);
		map.addBidirectionalLink(MT_ISA, TOWNSVILLE, 918);
		map.addBidirectionalLink(TOWNSVILLE, CAIRNS, 346);
		map.addBidirectionalLink(MT_ISA, LONGREACH, 647);
		map.addBidirectionalLink(TOWNSVILLE, MACKAY, 388);
		map.addBidirectionalLink(MACKAY, ROCKHAMPTON, 336);
		map.addBidirectionalLink(LONGREACH, ROCKHAMPTON, 687);
		map.addBidirectionalLink(ROCKHAMPTON, BRISBANE, 616);
		map.addBidirectionalLink(LONGREACH, CHARLEVILLE, 515);
		map.addBidirectionalLink(CHARLEVILLE, BRISBANE, 744);
		map.addBidirectionalLink(CHARLEVILLE, NYNGAN, 657);
		map.addBidirectionalLink(NYNGAN, BROKEN_HILL, 588);
		map.addBidirectionalLink(BROKEN_HILL, PORT_AUGUSTA, 415);
		map.addBidirectionalLink(NYNGAN, DUBBO, 166);
		map.addBidirectionalLink(DUBBO, BRISBANE, 860);
		map.addBidirectionalLink(DUBBO, SYDNEY, 466);
		map.addBidirectionalLink(BRISBANE, TAMWORTH, 576);
		map.addBidirectionalLink(BRISBANE, PORT_MACQUARIE, 555);
		map.addBidirectionalLink(PORT_MACQUARIE, NEWCASTLE, 245);
		map.addBidirectionalLink(TAMWORTH, NEWCASTLE, 284);
		map.addBidirectionalLink(NEWCASTLE, SYDNEY, 159);
		map.addBidirectionalLink(SYDNEY, CANBERRA, 287);
		map.addBidirectionalLink(CANBERRA, WAGGA_WAGGA, 243);
		map.addBidirectionalLink(DUBBO, WAGGA_WAGGA, 400);
		map.addBidirectionalLink(SYDNEY, LAKES_ENTRANCE, 706);
		map.addBidirectionalLink(LAKES_ENTRANCE, MELBOURNE, 317);
		map.addBidirectionalLink(WAGGA_WAGGA, MELBOURNE, 476);
		map.addBidirectionalLink(WAGGA_WAGGA, HAY, 269);
		map.addBidirectionalLink(MELBOURNE, WARNAMBOOL, 269);
		map.addBidirectionalLink(WARNAMBOOL, MOUNT_GAMBIER, 185);
		map.addBidirectionalLink(MOUNT_GAMBIER, ADELAIDE, 449);
		map.addBidirectionalLink(HAY, ADELAIDE, 655);
		map.addBidirectionalLink(PORT_AUGUSTA, ADELAIDE, 306);
		map.addBidirectionalLink(MELBOURNE, ADELAIDE, 728);
		map.addBidirectionalLink(PORT_AUGUSTA, PORT_LINCOLN, 341);
		
		
		// Locations coordinates
		// Alice Springs is taken as central point with coordinates (0|0)
		// Therefore x and y coordinates refer to Alice Springs. Note that
		// the coordinates are not very precise and partly modified to 
		// get a more real shape of Australia.
		map.setCoords(ADELAIDE, 417,-1289);
		map.setCoords(ALBANY, -1559,-1231);
		map.setCoords(ALICE_SPRINGS, 0,0);
		map.setCoords(BRISBANE, 1882,-415);
		map.setCoords(BROKEN_HILL, 709,-873);
		map.setCoords(BROOME, -1189,645);
		map.setCoords(CAIRNS, 1211,791);
		map.setCoords(CAMARVON, -2004,34);
		map.setCoords(CANBERRA, 1524,-1189);
		map.setCoords(CHARLEVILLE, 1256,-268);
		map.setCoords(COOBER_PEDY, 86,-593);
		map.setCoords(DARWIN, -328,1237);
		map.setCoords(DUBBO, 1474,-881);
		map.setCoords(ESPERANCE, -1182,-1132);
		map.setCoords(GERALDTON, -1958,-405);
		map.setCoords(HALLS_CREEK, -630,624);
		map.setCoords(HAY, 985,-1143);
		map.setCoords(KALGOORLIE, -1187,-729);
		map.setCoords(KATHERINE, -183,1025);
		map.setCoords(LAKES_ENTRANCE, 1412,-1609);
		map.setCoords(LONGREACH, 1057,49);
		map.setCoords(MACKAY, 1553,316);
		map.setCoords(MELBOURNE, 1118,-1570);
		map.setCoords(MOUNT_GAMBIER, 602,-1531);
		map.setCoords(MT_ISA, 563,344);
		map.setCoords(NEWCASTLE, 1841,-979);
		map.setCoords(NORSEMAN, -1162,-881);
		map.setCoords(NYNGAN, 1312,-781);
		map.setCoords(PERTH, -1827,-814);
		map.setCoords(PORT_AUGUSTA, 358,-996);
		map.setCoords(PORT_HEDLAND, -1558,438);
		map.setCoords(PORT_LINCOLN, 169,-1205);
		map.setCoords(PORT_MACQUARIE, 1884,-849);
		map.setCoords(ROCKHAMPTON, 1693,59);
		map.setCoords(SYDNEY, 1778,-1079);
		map.setCoords(TAMWORTH, 1752,-722);
		map.setCoords(TENNANT_CREEK, 30,445);
		map.setCoords(TOWNSVILLE, 1318,520);
		map.setCoords(WAGGA_WAGGA, 1322,-1125);
		map.setCoords(WARNAMBOOL, 761,-1665);
		map.setCoords(WYNDHAM, -572,932);
	}
}
