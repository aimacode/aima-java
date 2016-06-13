package aima.test.core.unit.search.support;

public class InLocationState {
	private String location;

	public InLocationState(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof InLocationState) {
			return this.getLocation().equals(((InLocationState) obj).getLocation());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return getLocation().hashCode();
	}

	@Override
	public String toString() {
		return "In("+getLocation()+")";
	}
}
