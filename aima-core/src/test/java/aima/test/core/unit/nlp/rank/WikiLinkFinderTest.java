package aima.test.core.unit.nlp.rank;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import aima.core.nlp.ranking.Page;
import aima.core.nlp.ranking.WikiLinkFinder;

public class WikiLinkFinderTest {
	
	Page testPage; Hashtable<String,Page> pageTable;
	WikiLinkFinder wLF;
	
	@Before
	public void setUp() {
		testPage = new Page("tester");
		pageTable = new Hashtable<String,Page>();
		wLF = new WikiLinkFinder();
	}

	@Test
	public void testGetOutlinks() {
		List<String> outLinks;
		List<String> validLinks = new ArrayList<String>( Arrays.asList("/wiki/thisisthefinallink"));
		String content = "Some example text with certain <aa href=\"link1\"></aa> links"
				+ "inside. Here is another href=\"link2\" without the surrounding tags. "
				+ "This isn't a link because there are no quotes -> href=notALink. The following"
				+ "is a link < href=\"www.link3.com\" ></> and should be found. Let's do a couple"
				+ "more. Penultimate link is <a href=\"penultimateLink.com.au\">hyperlink</a>. Final"
				+ "link is href href=\"/wiki/thisIsTheFinalLink\" href=notLink2, href\"notLink4\". Done";
		testPage.setContent(content);
		outLinks = wLF.getOutlinks(testPage);
		assertTrue( outLinks.containsAll(validLinks)); // note that locations are stored in lowercase
		assertTrue( !outLinks.contains("notALink"));
		assertTrue( !outLinks.contains("notLink4"));
		
	}
	
	@Test
	public void testGetInlinks() {
		
		Page targetP = new Page("targetPage");
		// create some test Pages
		Page test1 = new Page("test1"); Page test2 = new Page("test2"); 
		Page test3 = new Page("test3"); Page test4 = new Page("test4");
		test1.getOutlinks().addAll(Arrays.asList("a","b","targetPage","d"));
		test2.getOutlinks().addAll(Arrays.asList("targetpage","b","c","d","e"));
		test3.getOutlinks().addAll(Arrays.asList("target","page","c","d"));
		test4.getOutlinks().addAll(Arrays.asList("TARGETPAGE","b"));
		pageTable.put("test1", test1); pageTable.put("test2", test2);
		pageTable.put("test3", test3); pageTable.put("test4", test4);
		List<String> outLinks = wLF.getInlinks(targetP, pageTable);
		assertTrue( outLinks.contains("test1"));
		assertTrue( outLinks.containsAll(Arrays.asList("test1","test2","test4")));
		assertTrue( !outLinks.contains("test3"));
	}

}
