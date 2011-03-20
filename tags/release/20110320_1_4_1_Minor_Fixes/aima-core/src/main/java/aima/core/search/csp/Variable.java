package aima.core.search.csp;

/**
 * A variable is a distinguishable object with a name.
 * 
 * @author Ruediger Lunde
 */
public class Variable {
	private String name;

	public Variable(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Variable) {
			return this.name.equals(((Variable)obj).name);
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
