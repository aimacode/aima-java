package aima.test.core.unit.search.csp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.csp.Domain;

/**
 * @author Ravi Mohan
 * 
 */
public class DomainsTest {
	private Domain domains;

	@Before
	public void setUp() {
		List<String> vars = new ArrayList<String>();
		vars.add("x");
		domains = new Domain(vars);
	}

	@Test
	public void testDomain() {
		List<String> l = new ArrayList<String>();
		l.add("x");
		Domain d = new Domain(l);
		Assert.assertNotNull(d.getDomainOf("x"));
		Assert.assertEquals(new ArrayList(), d.getDomainOf("x"));
	}

	@Test
	public void testEmptyDomains() {
		Assert.assertEquals(new ArrayList(), domains.getDomainOf("x"));
	}

	@Test
	public void testNonEmptyDomains() {
		List<Object> dom = new ArrayList<Object>();
		dom.add("Ravi");
		Assert.assertEquals(new ArrayList(), domains.getDomainOf("x"));
		domains.add("x", "Ravi");
		Assert.assertEquals(dom, domains.getDomainOf("x"));
		domains.remove("x", "Ravi");
		Assert.assertEquals(new ArrayList(), domains.getDomainOf("x"));
	}
}
