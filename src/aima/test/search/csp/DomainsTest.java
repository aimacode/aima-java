
package aima.test.search.csp;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.search.csp.Domain;

/**
 * @author Ravi Mohan
 *
 */

public class DomainsTest extends TestCase {
	private Domain domains;
	public void setUp(){
		List<String> vars = new ArrayList<String>();
		vars.add("x");
		domains = new Domain(vars);
	}
	
	public void testEmptyDomains(){
		assertEquals(new ArrayList(),domains.getDomainOf("x")); 
	}
	public void testNonEmptyDomains(){
		List<Object> dom = new ArrayList<Object>();
		dom.add("Ravi");
		assertEquals(new ArrayList(),domains.getDomainOf("x")); 
		domains.addToDomain("x","Ravi");
		assertEquals(dom,domains.getDomainOf("x")); 
		domains.removeFromDomain("x","Ravi");
		assertEquals(new ArrayList(),domains.getDomainOf("x")); 
		
	}
}
