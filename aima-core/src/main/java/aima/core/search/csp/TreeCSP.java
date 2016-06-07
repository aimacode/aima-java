package aima.core.search.csp;

/**
 * Artificial Intelligence A Modern Approach (3rd Ed.): Figure 6.10(a), Page 223.<br>
 * <br>
 * The constraint graph of a tree-structured CSP.
 * The tree is duplicated so that there are 2 separated compartments. 
 * 
 * @author Peter Grubmair
 */
public class TreeCSP extends CSP {

	public static final Variable A = new Variable("A");
	public static final Variable B = new Variable("B");
	public static final Variable C = new Variable("C");
	public static final Variable D = new Variable("D");
	public static final Variable E = new Variable("E");
	public static final Variable F = new Variable("F");
	public static final Variable A1 = new Variable("A1");
	public static final Variable B1 = new Variable("B1");
	public static final Variable C1 = new Variable("C1");
	public static final Variable D1 = new Variable("D1");
	public static final Variable E1 = new Variable("E1");
	public static final Variable F1 = new Variable("F1");

	public static final String RED = "RED";
	public static final String GREEN = "GREEN";
	
	public TreeCSP() {
		addVariable(A);
		addVariable(B);
		addVariable(C);
		addVariable(D);
		addVariable(E);
		addVariable(F);
		
		addVariable(A1);
		addVariable(B1);
		addVariable(C1);
		addVariable(D1);
		addVariable(E1);
		addVariable(F1);

		Domain colors = new Domain(new Object[] { RED, GREEN });
		Domain restricted = new Domain( new Object[]{ RED }) ;

		for (Variable var : getVariables()) {
			if ( var != D  && var != D1) {
			  setDomain(var, colors);
			} else {
			  setDomain(var, restricted ) ; // make it a little bit more interesting	
			}
		}		

		addConstraint(new NotEqualConstraint(A, B));
		addConstraint(new NotEqualConstraint(B, C));
		addConstraint(new NotEqualConstraint(B, D));
		addConstraint(new NotEqualConstraint(D, E));
		addConstraint(new NotEqualConstraint(D, F));
		
		addConstraint(new NotEqualConstraint(A1, B1));
		addConstraint(new NotEqualConstraint(B1, C1));
		addConstraint(new NotEqualConstraint(B1, D1));
		addConstraint(new NotEqualConstraint(D1, E1));
		addConstraint(new NotEqualConstraint(D1, F1));

	}
}
