package aima.core.environment.map2d;

public class GoAction {
	private String goTo;

	public GoAction(String goTo) {
		this.goTo = goTo;
	}

	public String getGoTo() {
		return goTo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof GoAction) {
			return this.getGoTo().equals(((GoAction) obj).getGoTo());
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return getGoTo().hashCode();
	}

	@Override
	public String toString() {
		return "Go("+getGoTo()+")";
	}
}
