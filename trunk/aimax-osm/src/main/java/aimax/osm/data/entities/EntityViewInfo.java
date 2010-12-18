package aimax.osm.data.entities;

/**
 * Entity view informations are attached to map entities and describe
 * how the entity shall be drawn. This interface hides presentation layer
 * aspects from the application layer. For data organization on application
 * layer, only the minimal visible scale is relevant.
 * @author Ruediger Lunde
 */
public interface EntityViewInfo {
	/** Defines, at which abstraction level the entity is still relevant. */
	public float getMinVisibleScale();
}
