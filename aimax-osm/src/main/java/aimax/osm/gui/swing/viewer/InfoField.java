package aimax.osm.gui.swing.viewer;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JTextField;

import aimax.osm.data.MapEvent;
import aimax.osm.data.MapEventListener;
import aimax.osm.data.OsmMap;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.Track;
import aimax.osm.viewer.MapViewEvent;
import aimax.osm.viewer.MapViewEventListener;

/**
 * Text field which informs the user about results of the last map interaction.
 * @author Ruediger Lunde
 *
 */
public class InfoField extends JTextField {
	private static final long serialVersionUID = 1L;

	private MapEventHandler eventHandler;

	public InfoField(MapViewPane view, OsmMap map) {
		super(20);
		setEditable(false);
		eventHandler = new MapEventHandler(this, view, map);
	}

	public MapEventListener getMapDataEventListener() {
		return eventHandler;
	}

	public MapViewEventListener getMapViewEventListener() {
		return eventHandler;
	}

	/**
	 * Updates the info field based on events sent by the MapViewPane.
	 * 
	 * @author Ruediger Lunde
	 */
	static class MapEventHandler implements MapViewEventListener,
			MapEventListener {

		private JTextField infoField;
		private MapViewPane view;
		private OsmMap map;

		public MapEventHandler(JTextField infoField, MapViewPane view,
				OsmMap map) {
			this.infoField = infoField;
			this.map = map;
			this.view = view;
		}

		@Override
		public void eventHappened(MapViewEvent event) {
			if (event.getType() == MapViewEvent.Type.ZOOM) {
				if (map.getMarkers().isEmpty()) {
					DecimalFormat f = new DecimalFormat("#0.0");
					double scale = view.getTransformer().computeScale();
					String text = "Scale: 1 / ";
					if (scale <= 1e-4)
						text += (int) (0.001f / scale) + " 000";
					else
						text += (int) (1f / scale) + "";
					text += "  Display Factor: "
							+ f.format(view.getRenderer().getDisplayFactor());
					// text += "  (" + (int)
					// view.getTransformer().getDotsPerDeg() + ")";
					infoField.setText(text);
				}
			}
		}

		@Override
		public void eventHappened(MapEvent event) {
			if (event.getType() == MapEvent.Type.MAP_NEW) {
				infoField.setText("Ways: " + map.getWayCount() + ", Nodes: "
						+ map.getNodeCount() + ", POIs: " + map.getPoiCount());
			} else if (event.getType() == MapEvent.Type.MARKER_ADDED) {
				List<MapNode> nodes = map.getMarkers();
				DecimalFormat f1 = new DecimalFormat("#0.00");
				MapNode marker = nodes.get(nodes.size() - 1);
				infoField.setText("Marker " + marker.getName() + ": Lat "
						+ f1.format(marker.getLat()) + "; Lon "
						+ f1.format(marker.getLon()));
			} else if (event.getType() == MapEvent.Type.TRACK_MODIFIED) {
				Track track = map.getTrack(event.getObjId());
				if (track != null) {
					List<MapNode> nodes = track.getNodes();
					DecimalFormat f1 = new DecimalFormat("#0.00");
					double km = Position.getTrackLengthKM(nodes);
					String info = track.getName() + ": Length " + f1.format(km)
							+ " km";
					if (nodes.size() == 2) {
						DecimalFormat f2 = new DecimalFormat("#000");
						MapNode m1 = nodes.get(nodes.size() - 2);
						MapNode m2 = nodes.get(nodes.size() - 1);
						int course = new Position(m1).getCourseTo(m2);
						info += "; Direction " + f2.format(course);
					}
					infoField.setText(info);
				}
			} else {
				infoField.setText("");
			}
		}
	}
}
