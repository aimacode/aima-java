package aimax.osm.data.entities;

/**
 * An entity attribute consists of a name and a value string.
 * @author R. Lunde
 */
public class EntityAttribute implements Comparable<EntityAttribute> {
	private String name;
	private String value;
	
	public EntityAttribute(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}
	
	public int compareTo(EntityAttribute att) {
		int result = getName().compareTo(att.getName());
		if (result == 0)
			result = getValue().compareTo(att.getValue());
		return result;
	}

	public boolean equals(Object obj) {
		if (obj instanceof EntityAttribute) {
			EntityAttribute e = (EntityAttribute) obj;
			return name.equals(e.name) && value.equals(e.value);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode() + 7 * value.hashCode();
	}
	
	public String toString() {
		return name + "=" + value;
	}
}
