package aima.core.environment.twoply;

public class TwoPlyGameState {
	private String location;
	
	public TwoPlyGameState(String location) {
		this.location = location;
	}
	
	public String getLocation() {
		return location;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TwoPlyGameState) {
			return this.getLocation().equals(((TwoPlyGameState) obj).getLocation());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return getLocation().hashCode();
	}
	
	@Override
	public String toString() {
		return location;
	}
}
