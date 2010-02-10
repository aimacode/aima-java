package aimax.osm.data;

import java.util.HashSet;
import java.util.Hashtable;

import aimax.osm.data.entities.EntityAttribute;

/**
 * Singleton, maintaining entity attributes. This manager maintains
 * a hash table for attribute name and value strings, a second for
 * attributes consisting of two such strings, and additionally some filters
 * for attributes to be ignored. Using this manager reduces storage
 * consumption significantly, because strings and attributes with
 * same values are stored only once.
 */
public class EntityAttributeManager {
	private static EntityAttributeManager instance;
	
	Hashtable<String, String> internedTexts;
	Hashtable<EntityAttribute, EntityAttribute> internedAttributes;
	HashSet<String> ignoredAttNames;
	boolean ignorePathNames;
	
	/** Private constructor. */
	private EntityAttributeManager() {
		internedTexts = new Hashtable<String, String>();
		internedAttributes = new Hashtable<EntityAttribute, EntityAttribute>();
		ignoredAttNames = new HashSet<String>();
	}
	
	/** Returns the singleton instance. */
	public static EntityAttributeManager instance() {
		if (instance == null) {
			instance = new EntityAttributeManager();
		}
		return instance;
	}
	
	/** Clears all hash tables and all filters. */
	public void clear() {
		internedTexts.clear();
		internedAttributes.clear();
		ignoredAttNames.clear();
		ignorePathNames = false;
	}
	
	/** Advises the manager to ignore attributes with certain names. */
	public void ignoreAttNames(String[] attNames, boolean ignorePathnames) {
		this.ignorePathNames = ignorePathnames;
		for (String att : attNames)
			ignoredAttNames.add(att);
	}
	
	/**
	 * Checks whether an attribute equal to <code>att</code> already exists and
	 * returns it in this case. Otherwise <code>att</code> is added to the
	 * internal hash tables and the interned version of it is returned.
	 */
	public EntityAttribute intern(EntityAttribute att) {
		EntityAttribute result = internedAttributes.get(att);
		if (result == null && !ignoredAttNames.contains(att.getName())
				&& (!ignorePathNames || !att.getName().contains(":"))) {
			result = new EntityAttribute
			(intern(att.getName()), intern(att.getValue()));
			internedAttributes.put(result, result);
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
