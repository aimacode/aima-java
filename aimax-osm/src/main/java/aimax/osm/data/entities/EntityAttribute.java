package aimax.osm.data.entities;

/**
 * An entity attribute consists of a key and a value string.
 * @author Ruediger Lunde
 */
public class EntityAttribute implements Comparable<EntityAttribute> {
	private String key;
	private String value;
	
	public EntityAttribute(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public int compareTo(EntityAttribute att) {
		int result = getKey().compareTo(att.getKey());
		if (result == 0)
			result = getValue().compareTo(att.getValue());
		return result;
	}

	public boolean equals(Object obj) {
		if (obj != null &&  getClass() == obj.getClass()) {
			EntityAttribute e = (EntityAttribute) obj;
			return key.equals(e.key) && value.equals(e.value);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return key.hashCode() + 7 * value.hashCode();
	}
	
	public String toString() {
		return key + "=" + value;
	}
}
