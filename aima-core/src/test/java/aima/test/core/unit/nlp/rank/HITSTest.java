package aima.test.core.unit.nlp.rank;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import aima.core.nlp.ranking.HITS;
import aima.core.nlp.ranking.Page;
import aima.core.nlp.ranking.PagesDataset;

public class HITSTest {
	
	HITS hits;
	
	@Before
	public void setUp() {
		Map<String,Page> pageTable = PagesDataset.loadTestPages();
		hits = new HITS(pageTable);
	}

	@Test
	public void testMatches() {
		String query = "purple horse";
		String queryTwo = "puurple horse";
		String queryThree = "green";
		String text  = "This text contains the words 'purple horse' and the word 'green'";
		assertTrue( hits.matches(query, text) );
		assertFalse( hits.matches(queryTwo, text) );
		assertTrue( hits.matches(queryThree, text) );
	}
	
	@Test
	public void testNormalize() {
		ArrayList<Page> pages = new ArrayList<Page>();
		Page p1 = new Page(""); Page p2 = new Page("");
		Page p3 = new Page(""); Page p4 = new Page("");
		p1.hub = 3; p1.authority = 2;
		p2.hub = 2; p2.authority = 3;
		p3.hub = 1; p1.authority = 4;
		p4.hub = 0; p4.authority = 10;
		pages.add(p1); pages.add(p2); pages.add(p3); pages.add(p4);
		// hub total will be 9 + 4 + 1 + 0 = 14
		// authority total will 4 + 9 + 16 + 100 = 129
		double p1HubNorm = 0.214285; double p2HubNorm = 0.142857;
		hits.normalize(pages);
		assertEquals( "Out of tolerance", p1HubNorm, pages.get(0).hub, 0.02); 
		assertEquals( "Out of tolerance", pages.get(1).hub, p2HubNorm, 0.02 );
	}
	
	@Ignore("TODO")
	@Test
	public void testSumInlinkHubScore() {
		
	}
	
	@Ignore("TODO")
	@Test
	public void testSumOutlinkAuthorityScore() {
		
	}
	
	@Ignore("TODO")
	@Test 
	public void testConvergence() {
		
	}
	
	@Test
	public void testGetAveDelta() {
		double[] one = {0,1,2,3,4,5}; 
		double[] two = {0.5,1.5,2.5,3.5,4.5,5.5};
		
		double aveDelta = hits.getAveDelta(one, two);
		assertEquals( aveDelta, 0.5, 0 );
	}

	@Test
	public void testSortAuthorityOrdersByDescendingAuthority() {
		// authority order (A,B,C) differs from hub order (C,A,B), so a sort
		// which uses the wrong field cannot pass.
		Page a = page("A", 0.9, 0.5);
		Page b = page("B", 0.5, 0.9);
		Page c = page("C", 0.1, 0.1);
		List<Page> result = new ArrayList<>(Arrays.asList(c, a, b));

		hits.sortAuthority(result);

		assertEquals(Arrays.asList("A", "B", "C"), locations(result));
	}

	@Test
	public void testSortHubOrdersByDescendingHub() {
		// hub order (B,A,C) differs from authority order (A,B,C).
		Page a = page("A", 0.9, 0.5);
		Page b = page("B", 0.5, 0.9);
		Page c = page("C", 0.1, 0.1);
		List<Page> result = new ArrayList<>(Arrays.asList(c, a, b));

		hits.sortHub(result);

		assertEquals(Arrays.asList("B", "A", "C"), locations(result));
	}

	@Test
	public void testSortBreaksTiesByLocation() {
		Page a = page("A", 0.5, 0.5);
		Page b = page("B", 0.5, 0.5);
		Page c = page("C", 0.5, 0.5);
		List<Page> result = new ArrayList<>(Arrays.asList(c, a, b));

		hits.sortAuthority(result);

		assertEquals(Arrays.asList("A", "B", "C"), locations(result));
	}

	@Test
	public void testReportLabelsMaxHubByHubNotAuthority() {
		// The summary lines must report the page with the highest hub as the
		// hub winner, which is a different page from the authority winner here.
		Page highAuthority = page("highAuthority", 1.0, 0.0);
		Page highHub = page("highHub", 0.0, 1.0);
		List<Page> result = new ArrayList<>(
				Arrays.asList(highAuthority, highHub));

		PrintStream original = System.out;
		ByteArrayOutputStream captured = new ByteArrayOutputStream();
		System.setOut(new PrintStream(captured, true));
		try {
			hits.report(result);
		} finally {
			System.setOut(original);
		}
		String output = captured.toString();

		assertTrue(output.contains("highest Authority score: highAuthority"));
		assertTrue(output.contains("highest Hub score: highHub"));
	}

	private static Page page(String location, double authority, double hub) {
		Page page = new Page(location);
		page.authority = authority;
		page.hub = hub;
		return page;
	}

	private static List<String> locations(List<Page> pages) {
		List<String> locations = new ArrayList<>();
		for (Page page : pages)
			locations.add(page.getLocation());
		return locations;
	}
}
