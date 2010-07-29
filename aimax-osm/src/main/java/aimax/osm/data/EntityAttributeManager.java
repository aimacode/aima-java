package aimax.osm.data;

import java.util.HashSet;
import java.util.Hashtable;

import aimax.osm.data.entities.EntityAttribute;

/**
 * Singleton, maintaining entity attributes. This manager maintains
 * a hash table for attribute key and value strings, a second for
 * attributes consisting of two such strings, and additionally some filters
 * for attributes to be ignored. This manager tries to reduce storage
 * consumption by storing just one representation for each string.
 * @author Ruediger Lunde
 */
public class EntityAttributeManager {
	private static EntityAttributeManager instance;
	
	Hashtable<String, String> internedTexts;
	Hashtable<EntityAttribute, EntityAttribute> internedAttributes;
	HashSet<String> ignoredAttKeys;
	boolean ignorePathKeys;
	
	/** Private constructor. */
	private EntityAttributeManager() {
		internedTexts = new Hashtable<String, String>();
		internedAttributes = new Hashtable<EntityAttribute, EntityAttribute>();
		ignoredAttKeys = new HashSet<String>();
	}
	
	/** Returns the singleton instance. */
	public static EntityAttributeManager instance() {
		if (instance == null) {
			instance = new EntityAttributeManager();
		}
		return instance;
	}
	
	/** Clears all hash tables. */
	public void clearHash() {
		internedTexts.clear();
		internedAttributes.clear();
	}
	
	/** Clears the attribute key filters. */
	public void clearFilter() {
		ignoredAttKeys.clear();
		ignorePathKeys = false;
	}
	
	/** Advises the manager to ignore attributes with certain keys. */
	public void ignoreAttKeys(String[] attKeys, boolean ignorePathKeys) {
		ignoredAttKeys.clear();
		for (String key : attKeys)
			ignoredAttKeys.add(key);
		this.ignorePathKeys = ignorePathKeys;
	}
	
	/**
	 * Checks whether <code>att</code> has to be ignored with respect to
	 * the attribute filter in use. Otherwise <code>att</code> is added to
	 * the internal hash tables and the interned version of it is returned.
	 */
	public EntityAttribute intern(EntityAttribute att) {
		EntityAttribute result = null;
		// this version seems to have no measurable advantage:
//		result = internedAttributes.get(att);
//		if (result == null && !ignoredAttNames.contains(att.getName())
//				&& (!ignorePathNames || !att.getName().contains(":"))) {
//			result = new EntityAttribute
//			(intern(att.getName()), intern(att.getValue()));
//			internedAttributes.put(result, result);
//		}
//		return result;
		if (!ignoredAttKeys.contains(att.getKey())
				&& (!ignorePathKeys || !att.getKey().contains(":"))) {
			att.setKey(intern(att.getKey()));
			att.setValue(intern(att.getValue()));
			result = att;
		}
		return result;
	}
	
	/** Gets a unified representation of <code>text</code>. */
	private String intern(String text) {
		String result = internedTexts.get(text);
		if (result == null) {
			internedTexts.put(text, text);
			result = text;
		}
		return text;
	}
}
