package aima.core.environment.map;

/**
 * Represents a simplified road map of Australia. The initialization method is
 * declared static. So it can also be used to initialize other specialized
 * subclasses of {@link ExtendableMap} with road map data from Australia. The
 * data was extracted from a class developed by Felix Knittel.
 * 
 * @author Ruediger Lunde
 */
public class SimplifiedRoadMapOfAustralia extends ExtendableMap {

	public SimplifiedRoadMapOfAustralia() {
		initMap(this);
	}

	// Locations
	public static final String ADELAIDE = "Adelaide";
	public static final String ALBANY = "Albany";
	public static final String ALICE_SPRINGS = "AliceSprings";
	public static final String BRISBANE = "Brisbane";
	public static final String BROKEN_HILL = "BrokenHill";
	public static final String BROOME = "Broome";
	public static final String CAIRNS = "Cairns";
	public static final String CAMARVON = "Camarvon";
	public static final String CANBERRA = "Canberra";
	public static final String CHARLEVILLE = "Charleville";
	public static final String COOBER_PEDY = "CooberPedy";
	public static final String DARWIN = "Darwin";
	public static final String DUBBO = "Dubbo";
	public static final String ESPERANCE = "Esperance";
	public static final String GERALDTON = "Geraldton";
	public static final String HALLS_CREEK = "HallsCreek";
	public static final String HAY = "Hay";
	public static final String KALGOORLIE = "Kalgoorlie";
	public static final String KATHERINE = "Katherine";
	public static final String LAKES_ENTRANCE = "LakesEntrance";
	public static final String LONGREACH = "Longreach";
	public static final String MACKAY = "Mackay";
	public static final String MELBOURNE = "Melbourne";
	public static final String MOUNT_GAMBIER = "MountGambier";
	public static final String MT_ISA = "MtIsa";
	public static final String NEWCASTLE = "Newcastle";
	public static final String NORSEMAN = "Norseman";
	public static final String NYNGAN = "Nyngan";
	public static final String PERTH = "Perth";
	public static final String PORT_AUGUSTA = "PortAugusta";
	public static final String PORT_HEDLAND = "PortHedland";
	public static final String PORT_LINCOLN = "PortLincoln";
	public static final String PORT_MACQUARIE = "PortMacquarie";
	public static final String ROCKHAMPTON = "Rockhampton";
	public static final String SYDNEY = "Sydney";
	public static final String TAMWORTH = "Tamworth";
	public static final String TENNANT_CREEK = "TennantCreek";
	public static final String TOWNSVILLE = "Townsville";
	public static final String WAGGA_WAGGA = "WaggaWagga";
	public static final String WARNAMBOOL = "Warnambool";
	public static final String WYNDHAM = "Wyndham";

	/**
	 * Initializes a map with a simplified road map of Australia.
	 */
	public static void initMap(ExtendableMap map) {
		map.clear();
		// Add links
		// Distances from http://maps.google.com
		map.addBidirectionalLink(PERTH, ALBANY, 417.0);
		map.addBidirectionalLink(PERTH, KALGOORLIE, 593.0);
		map.addBidirectionalLink(PERTH, GERALDTON, 424.0);
		map.addBidirectionalLink(PERTH, PORT_HEDLAND, 1637.0);
		map.addBidirectionalLink(ALBANY, ESPERANCE, 478.0);
		map.addBidirectionalLink(KALGOORLIE, NORSEMAN, 187.0);
		map.addBidirectionalLink(ESPERANCE, NORSEMAN, 204.0);
		map.addBidirectionalLink(NORSEMAN, PORT_AUGUSTA, 1668.0);
		map.addBidirectionalLink(GERALDTON, CAMARVON, 479.0);
		map.addBidirectionalLink(CAMARVON, PORT_HEDLAND, 872.0);
		map.addBidirectionalLink(PORT_HEDLAND, BROOME, 589.0);
		map.addBidirectionalLink(BROOME, HALLS_CREEK, 685.0);
		map.addBidirectionalLink(HALLS_CREEK, WYNDHAM, 370.0);
		map.addBidirectionalLink(HALLS_CREEK, KATHERINE, 874.0);
		map.addBidirectionalLink(WYNDHAM, KATHERINE, 613.0);
		map.addBidirectionalLink(KATHERINE, DARWIN, 317.0);
		map.addBidirectionalLink(KATHERINE, TENNANT_CREEK, 673.0);
		map.addBidirectionalLink(TENNANT_CREEK, MT_ISA, 663.0);
		map.addBidirectionalLink(TENNANT_CREEK, ALICE_SPRINGS, 508.0);
		map.addBidirectionalLink(ALICE_SPRINGS, COOBER_PEDY, 688.0);
		map.addBidirectionalLink(COOBER_PEDY, PORT_AUGUSTA, 539.0);
		map.addBidirectionalLink(MT_ISA, TOWNSVILLE, 918.0);
		map.addBidirectionalLink(TOWNSVILLE, CAIRNS, 346.0);
		map.addBidirectionalLink(MT_ISA, LONGREACH, 647.0);
		map.addBidirectionalLink(TOWNSVILLE, MACKAY, 388.0);
		map.addBidirectionalLink(MACKAY, ROCKHAMPTON, 336.0);
		map.addBidirectionalLink(LONGREACH, ROCKHAMPTON, 687.0);
		map.addBidirectionalLink(ROCKHAMPTON, BRISBANE, 616.0);
		map.addBidirectionalLink(LONGREACH, CHARLEVILLE, 515.0);
		map.addBidirectionalLink(CHARLEVILLE, BRISBANE, 744.0);
		map.addBidirectionalLink(CHARLEVILLE, NYNGAN, 657.0);
		map.addBidirectionalLink(NYNGAN, BROKEN_HILL, 588.0);
		map.addBidirectionalLink(BROKEN_HILL, PORT_AUGUSTA, 415.0);
		map.addBidirectionalLink(NYNGAN, DUBBO, 166.0);
		map.addBidirectionalLink(DUBBO, BRISBANE, 860.0);
		map.addBidirectionalLink(DUBBO, SYDNEY, 466.0);
		map.addBidirectionalLink(BRISBANE, TAMWORTH, 576.0);
		map.addBidirectionalLink(BRISBANE, PORT_MACQUARIE, 555.0);
		map.addBidirectionalLink(PORT_MACQUARIE, NEWCASTLE, 245.0);
		map.addBidirectionalLink(TAMWORTH, NEWCASTLE, 284.0);
		map.addBidirectionalLink(NEWCASTLE, SYDNEY, 159.0);
		map.addBidirectionalLink(SYDNEY, CANBERRA, 287.0);
		map.addBidirectionalLink(CANBERRA, WAGGA_WAGGA, 243.0);
		map.addBidirectionalLink(DUBBO, WAGGA_WAGGA, 400.0);
		map.addBidirectionalLink(SYDNEY, LAKES_ENTRANCE, 706.0);
		map.addBidirectionalLink(LAKES_ENTRANCE, MELBOURNE, 317.0);
		map.addBidirectionalLink(WAGGA_WAGGA, MELBOURNE, 476.0);
		map.addBidirectionalLink(WAGGA_WAGGA, HAY, 269.0);
		map.addBidirectionalLink(MELBOURNE, WARNAMBOOL, 269.0);
		map.addBidirectionalLink(WARNAMBOOL, MOUNT_GAMBIER, 185.0);
		map.addBidirectionalLink(MOUNT_GAMBIER, ADELAIDE, 449.0);
		map.addBidirectionalLink(HAY, ADELAIDE, 655.0);
		map.addBidirectionalLink(PORT_AUGUSTA, ADELAIDE, 306.0);
		map.addBidirectionalLink(MELBOURNE, ADELAIDE, 728.0);
		map.addBidirectionalLink(PORT_AUGUSTA, PORT_LINCOLN, 341.0);

		// Locations coordinates
		// Alice Springs is taken as central point with coordinates (0|0)
		// Therefore x and y coordinates refer to Alice Springs. Note that
		// the coordinates are not very precise and partly modified to
		// get a more real shape of Australia.
		map.setPosition(ADELAIDE, 417, 1289);
		map.setPosition(ALBANY, -1559, 1231);
		map.setPosition(ALICE_SPRINGS, 0, 0);
		map.setPosition(BRISBANE, 1882, 415);
		map.setPosition(BROKEN_HILL, 709, 873);
		map.setPosition(BROOME, -1189, -645);
		map.setPosition(CAIRNS, 1211, -791);
		map.setPosition(CAMARVON, -2004, -34);
		map.setPosition(CANBERRA, 1524, 1189);
		map.setPosition(CHARLEVILLE, 1256, 268);
		map.setPosition(COOBER_PEDY, 86, 593);
		map.setPosition(DARWIN, -328, -1237);
		map.setPosition(DUBBO, 1474, 881);
		map.setPosition(ESPERANCE, -1182, 1132);
		map.setPosition(GERALDTON, -1958, 405);
		map.setPosition(HALLS_CREEK, -630, -624);
		map.setPosition(HAY, 985, 1143);
		map.setPosition(KALGOORLIE, -1187, 729);
		map.setPosition(KATHERINE, -183, -1025);
		map.setPosition(LAKES_ENTRANCE, 1412, 1609);
		map.setPosition(LONGREACH, 1057, -49);
		map.setPosition(MACKAY, 1553, -316);
		map.setPosition(MELBOURNE, 1118, 1570);
		map.setPosition(MOUNT_GAMBIER, 602, 1531);
		map.setPosition(MT_ISA, 563, -344);
		map.setPosition(NEWCASTLE, 1841, 979);
		map.setPosition(NORSEMAN, -1162, 881);
		map.setPosition(NYNGAN, 1312, 781);
		map.setPosition(PERTH, -1827, 814);
		map.setPosition(PORT_AUGUSTA, 358, 996);
		map.setPosition(PORT_HEDLAND, -1558, -438);
		map.setPosition(PORT_LINCOLN, 169, 1205);
		map.setPosition(PORT_MACQUARIE, 1884, 849);
		map.setPosition(ROCKHAMPTON, 1693, -59);
		map.setPosition(SYDNEY, 1778, 1079);
		map.setPosition(TAMWORTH, 1752, 722);
		map.setPosition(TENNANT_CREEK, 30, -445);
		map.setPosition(TOWNSVILLE, 1318, -520);
		map.setPosition(WAGGA_WAGGA, 1322, 1125);
		map.setPosition(WARNAMBOOL, 761, 1665);
		map.setPosition(WYNDHAM, -572, -932);
	}
}
