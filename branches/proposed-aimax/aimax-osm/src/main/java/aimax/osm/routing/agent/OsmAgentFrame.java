package aimax.osm.routing.agent;

import java.awt.BorderLayout;

import aima.gui.applications.search.SearchFactory;
import aima.gui.applications.search.map.MapAgentFrame;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.AgentAppModel;
import aimax.osm.data.MapDataStore;
import aimax.osm.viewer.MapViewPane;

/** Simple frame which embeds the OSM viewer as agent view. */
public class OsmAgentFrame extends MapAgentFrame {
	private OsmAgentViewer osmAgentViewer;
	
	/** Creates a new frame. */
	public OsmAgentFrame(OsmMap map) {
		setTitle("OAS - the Osm Agent Simulator");
		setSelectors(new String[]{
				SCENARIO_SEL, AGENT_SEL,
				SEARCH_SEL, SEARCH_MODE_SEL, HEURISTIC_SEL},
				new String[]{
				"Select Scenario", "Select Agent",
				"Select Search Strategy", "Select Search Mode", "Select Heuristic"}
		);
		setSelectorItems(SCENARIO_SEL,
				new String[] {"Use any way", "Travel by car", "Travel by bicycle"}, 0);
		setSelectorItems(AGENT_SEL,
				new String[] {"Offline Search", "Online Search (LRTA*)"}, 0);
		setSelectorItems(SEARCH_SEL,
				SearchFactory.getInstance().getSearchStrategyNames(), 5);
		setSelectorItems(SEARCH_MODE_SEL, SearchFactory.getInstance()
				.getSearchModeNames(), 1); // change the default!
		setSelectorItems(HEURISTIC_SEL, new String[] { "H1 (=0)",
				"H2 (sld to goal)" }, 1);
		osmAgentViewer = new OsmAgentViewer(map.getMapData());
		setAgentView(osmAgentViewer);
		this.setUpdateDelay(0);
	}
	
	/** Returns the embedded map view pane. */
	public MapViewPane getMapViewer() {
		return osmAgentViewer.mapViewer;
	}
	
	
	//////////////////////////////////////////////////////////
	// inner classes
	
	/**
	 * Wrapper around a <code>MapViewPane</code> to let it appear as
	 * if it would be an agent view.
	 */
	public class OsmAgentViewer extends AgentAppFrame.AbstractAgentView {

		MapViewPane mapViewer;
		
		public OsmAgentViewer(MapDataStore model) {
			mapViewer = new MapViewPane();
			mapViewer.setModel(model);
			this.setLayout(new BorderLayout());
			add(mapViewer, BorderLayout.CENTER);
		}
		/**
		 * Does nothing. Why: The embedded {@link #mapViewer}
		 * is directly informed by its model - the map data
		 * store within the <code>OsmMap</code>.
		 */
		@Override
		public void updateView(AgentAppModel model) {
		}
	}
}
