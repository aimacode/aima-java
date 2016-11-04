package aimax.osm.gui.swing.viewer.agent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MoveToAction;
import aima.gui.swing.framework.AgentAppEnvironmentView;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapBuilder;
import aimax.osm.data.OsmMap;
import aimax.osm.data.Position;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapNode;
import aimax.osm.reader.Bz2OsmReader;
import aimax.osm.reader.MapReader;
import aimax.osm.routing.MapAdapter;
import aimax.osm.viewer.MapStyleFactory;
import aimax.osm.viewer.UColor;
import aimax.osm.gui.swing.viewer.MapViewPane;
import aimax.osm.gui.swing.viewer.MapViewPopup;

/**
 * Visualizes agent positions and movements in an OSM map. It is assumed that
 * agents are only added but never removed from an environment.
 * 
 * @author Ruediger Lunde
 */
public class OsmAgentView extends AgentAppEnvironmentView {

	private static final long serialVersionUID = 1L;
	public static final String TRACK_NAME = "Track";

	MapViewPane mapViewPane;

	/**
	 * Creates an agent view which displays agent positions within a map using
	 * the given map data. This implementation assumes that for the same
	 * <code>mapData</code> instance only one agent view is created.
	 */
	public OsmAgentView(OsmMap mapData) {
		MapStyleFactory msf = new MapStyleFactory();
		EntityClassifier<EntityViewInfo> eClassifier = msf
				.createDefaultClassifier();
		eClassifier.addRule("track_type", TRACK_NAME + 0, msf
				.createTrackInfo(UColor.RED));
		eClassifier.addRule("track_type", TRACK_NAME + 1, msf
				.createTrackInfo(UColor.GREEN));
		eClassifier.addRule("track_type", TRACK_NAME + 2, msf
				.createTrackInfo(UColor.BLUE));
		mapData.setEntityClassifier(eClassifier);
		mapViewPane = new MapViewPane();
		mapViewPane.setMap(mapData);
		mapViewPane.setPopupMenu(new MapViewPopupWithLoad());
		setLayout(new BorderLayout());
		add(mapViewPane, BorderLayout.CENTER);
	}

	public MapViewPane getMapViewPane() {
		return mapViewPane;
	}

	protected MapEnvironment getMapEnv() {
		return (MapEnvironment) env;
	}

	/**
	 * Reacts on environment changes and updates the tracks.
	 */
	@Override
	public void agentAdded(Agent agent, Environment source) {
		String loc = getMapEnv().getAgentLocation(agent);
		updateTrack(agent, loc);
	}

	/**
	 * Reacts on environment changes and updates the tracks.
	 */
	@Override
	public void agentActed(Agent agent, Action command, Environment source) {
		String msg = "";
		if (env.getAgents().size() > 1)
			msg = "A" + env.getAgents().indexOf(agent) + ": ";
		notify(msg + command.toString());
		if (command instanceof MoveToAction) {
			updateTrack(agent, getMapEnv().getAgentLocation(agent));
		}
	}

	private void updateTrack(Agent agent, String location) {
		MapAdapter map = (MapAdapter) getMapEnv().getMap();
		MapNode node = map.getWayNode(location);
		if (node != null) {
			int aIdx = getMapEnv().getAgents().indexOf(agent);
			map.getOsmMap().addToTrack(TRACK_NAME + aIdx,
					new Position(node.getLat(), node.getLon()));
		}
	}

	// ////////////////////////////////////////////////////////////////////
	// inner classes

	/** Extends the <code>MapViewPopup</code> by a load map menu item. */
	private class MapViewPopupWithLoad extends MapViewPopup {

		private static final long serialVersionUID = 1L;
		MapReader mapReader;
		JMenuItem loadMenuItem;
		JFileChooser loadFileChooser;

		MapViewPopupWithLoad() {
			mapReader = new Bz2OsmReader();
			loadMenuItem = new JMenuItem("Load Map");
			loadMenuItem.addActionListener(this);
			add(loadMenuItem, 3);
		}

		/**
		 * Creates a file chooser for map loading if necessary (lazy
		 * instantiation).
		 */
		JFileChooser getLoadFileChooser() {
			JFileChooser result = loadFileChooser;
			if (result == null) {
				result = new JFileChooser();
				String[] exts = mapReader.fileFormatDescriptions();
				for (int i = 0; i < exts.length; i++) {
					FileFilter filter = new FileNameExtensionFilter(exts[i],
							mapReader.fileFormatExtensions()[i]);
					result.addChoosableFileFilter(filter);
				}
				result.setFileFilter(result.getChoosableFileFilters()[0]);
				loadFileChooser = result;
			}
			return result;
		}

		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == loadMenuItem) {
				JFileChooser fc = getLoadFileChooser();
				int status = fc.showOpenDialog(pane);
				if (status == JFileChooser.APPROVE_OPTION) {
					MapBuilder mapBuilder = pane.getMap().getBuilder();
					mapReader.readMap(fc.getSelectedFile(), mapBuilder);
					mapBuilder.buildMap();
				}
			} else {
				super.actionPerformed(ae);
			}
		}
	}
}
