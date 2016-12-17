package aima.test.core.unit.nlp.rank;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import aima.core.nlp.ranking.Page;
import aima.core.nlp.ranking.PagesDataset;

public class PagesDatasetTest {
	
	Map<String,Page> pageTable;
	// resource folder of .txt files to test with
	String testFilesFolderPath = "src/main/resources/aima/core/ranking/data/pages/test_pages";
	
	@Test
	public void testGetPageName() {
		File file = new File("test/file/path.txt");
		File fileTwo = new File("test/file/PATHTWO.txt");
		String p = PagesDataset.getPageName(file);
		assertEquals( p, "/wiki/path");
		assertEquals( PagesDataset.getPageName(fileTwo), "/wiki/pathtwo");
	}
	
	@Ignore("testFilesFolderPath currently breaks portability")
	@Test
	public void testLoadPages() {
		String folderPath = testFilesFolderPath;
		pageTable = PagesDataset.loadPages(folderPath);
		assertTrue( pageTable.size() > 0 );
		assertTrue( pageTable.containsKey("/wiki/TestMan".toLowerCase()));
	}
	
	@Ignore("testFilesFolderPath currently breaks portability")
	@Test
	public void testLoadPagesInlinks() {
		String folderPath = testFilesFolderPath;
		pageTable = PagesDataset.loadPages(folderPath);
		// TestMan.txt should have the following inlinks
		// "/wiki/testdog", "/wiki/testgorilla", "/wiki/testliving", "/wiki/testturnerandhooch"
		Page testPage = pageTable.get("/wiki/testman");
		assertTrue( testPage.getInlinks().containsAll(Arrays.asList("/wiki/testdog",
																	"/wiki/testgorilla",
																	"/wiki/testliving",
																	"/wiki/testturnerandhooch")));	
	}

	@Ignore("testFilesFolderPath currently breaks portability")
	@Test
	public void testLoadFileText() {
		String testFilePath = "TestMan.txt";
		File folder = new File(testFilesFolderPath);
		File f = new File(testFilePath);
		String content = PagesDataset.loadFileText(folder, f);
		assertNotEquals( content, null);
		assertNotEquals( content, "");
		assertTrue( content.contains("Keyword String 1: A man is a male human."));
		
	}
}
