package aimax.osm.data;

import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;
/**
 * The visitor pattern is used here, to iterate across sets
 * of different kinds of map entities in a flexible manner.
 * This interface supports for example the implementation
 * of different renderers, which find the right code for a
 * given entity instance without class checking in if statements.
 * @author Ruediger Lunde
 */
public interface EntityVisitor {
	public void visitMapNode(MapNode node);
	public void visitMapWay(MapWay way);
	public void visitTrack(Track track);
}
