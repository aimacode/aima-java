package aima.core.probability.proposition;

public abstract class AbstractDerivedProposition extends AbstractProposition
		implements DerivedProposition {

	private String name = null;

	public AbstractDerivedProposition(String name) {
		this.name = name;
	}

	//
	// START-DerivedProposition
	public String getDerivedName() {
		return name;
	}

	// END-DerivedProposition
	//
}