/*
 * Created on Sep 21, 2004
 *
 */
package aima.search.csp;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */

public class MapCSP extends CSP {
	public static String WA = "WA";

	public static String NT = "NT";

	public static String SA = "SA";

	public static String Q = "Q";

	public static String NSW = "NSW";

	public static String V = "V";

	public static String T = "T";

	public static String RED = "RED";

	public static String BLUE = "BLUE";

	public static String GREEN = "GREEN";

	private MapCSP(List<String> variables, Constraint constraints) {
		super(variables, constraints);

	}

	public static CSP getMap() {
		List<String> variables = new ArrayList<String>();
		variables.add(WA);
		variables.add(NT);
		variables.add(SA);
		variables.add(Q);
		variables.add(NSW);
		variables.add(V);
		variables.add(T);

		List<String> colors = new ArrayList<String>();
		colors.add(RED);
		colors.add(BLUE);
		colors.add(GREEN);

		Domain domains = new Domain(variables);
		for (int i = 0; i < variables.size(); i++) {
			String variable = variables.get(i);
			domains.addToDomain(variable, colors);
		}

		Hashtable<String, List<String>> neighbors = new Hashtable<String, List<String>>();
		addToNeighbors(neighbors, T);
		addToNeighbors(neighbors, WA, NT, SA);
		addToNeighbors(neighbors, NT, WA, SA, Q);
		addToNeighbors(neighbors, SA, WA, NT, Q, NSW, V);
		addToNeighbors(neighbors, Q, NT, SA, NSW);
		addToNeighbors(neighbors, NSW, SA, Q, V);
		addToNeighbors(neighbors, V, SA, NSW);
		Constraint mapConstraints = new MapColoringConstraint(neighbors);

		return new CSP(variables, mapConstraints, domains);
	}

	public static void addToNeighbors(
			Hashtable<String, List<String>> neighbors, String whose) {
		List<String> l = new ArrayList<String>();
		neighbors.put(whose, l);
	}

	public static void addToNeighbors(
			Hashtable<String, List<String>> neighbors, String whose, String one) {
		List<String> l = new ArrayList<String>();
		l.add(one);
		neighbors.put(whose, l);
	}

	public static void addToNeighbors(
			Hashtable<String, List<String>> neighbors, String whose,
			String one, String two) {
		List<String> l = new ArrayList<String>();
		l.add(one);
		l.add(two);
		neighbors.put(whose, l);
	}

	public static void addToNeighbors(
			Hashtable<String, List<String>> neighbors, String whose,
			String one, String two, String three) {
		List<String> l = new ArrayList<String>();
		l.add(one);
		l.add(two);
		l.add(three);
		neighbors.put(whose, l);
	}

	public static void addToNeighbors(
			Hashtable<String, List<String>> neighbors, String whose,
			String one, String two, String three, String four) {
		List<String> l = new ArrayList<String>();
		l.add(one);
		l.add(two);
		l.add(three);
		l.add(four);
		neighbors.put(whose, l);
	}

	public static void addToNeighbors(
			Hashtable<String, List<String>> neighbors, String whose,
			String one, String two, String three, String four, String five) {
		List<String> l = new ArrayList<String>();
		l.add(one);
		l.add(two);
		l.add(three);
		l.add(four);
		l.add(five);
		neighbors.put(whose, l);
	}
}