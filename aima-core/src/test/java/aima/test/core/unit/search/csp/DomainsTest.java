package aima.test.core.unit.search.csp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.csp.Domains;
import aima.core.search.csp.Variable;

/**
 * @author Ravi Mohan
 * 
 */
public class DomainsTest {
	private static final Variable X = new Variable("x");
	
	private Domains domains;

	@Before
	public void setUp() {
		List<Variable> vars = new ArrayList<Variable>();
		vars.add(X);
		domains = new Domains(vars);
	}

	@Test
	public void testDomain() {
		List<Variable> l = new ArrayList<Variable>();
		l.add(X);
		Domains d = new Domains(l);
		Assert.assertNotNull(d.getDomain(X));
		Assert.assertEquals(new ArrayList<Object>(), d.getDomain(X));
	}

	@Test
	public void testEmptyDomains() {
		Assert.assertEquals(new ArrayList<Object>(), domains.getDomain(X));
	}

	@Test
	public void testNonEmptyDomains() {
		List<Object> dom = new ArrayList<Object>();
		dom.add("Ravi");
		Assert.assertEquals(new ArrayList<Object>(), domains.getDomain(X));
		domains.addToDomain(X, "Ravi");
		Assert.assertEquals(dom, domains.getDomain(X));
		domains.removeFromDomain(X, "Ravi");
		Assert.assertEquals(new ArrayList<Object>(), domains.getDomain(X));
	}
}
