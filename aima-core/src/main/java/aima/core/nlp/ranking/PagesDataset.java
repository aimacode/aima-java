package aima.core.nlp.ranking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Jonathon Belotti (thundergolfer)
 *
 */
public class PagesDataset {

	static String wikiPagesFolderPath = "src\\main\\resources\\aima\\core\\ranking\\data\\pages";
	static String testFilesFolderPath = "src\\main\\resources\\aima\\core\\ranking\\data\\pages\\test_pages";

	private static WikiLinkFinder wlf;

	public static Map<String, Page> loadDefaultPages() {
		return loadPages(wikiPagesFolderPath);
	}

	public static Map<String, Page> loadTestPages() {
		return loadPages(testFilesFolderPath);
	}

	/**
	 * Access a folder of .txt files containing wikipedia html source, and give
	 * back a hashtable of pages, which each page having it's correct inlink
	 * list and outlink list.
	 * 
	 * @param folderPath
	 * @return a hashtable of Page objects, accessed by article name (which is a
	 *         location for wikipedia: \wiki\*article name*)
	 */
	public static Map<String, Page> loadPages(String folderPath) {

		Map<String, Page> pageTable = new Hashtable<String, Page>();
		Page currPage;
		File[] listOfFiles;
		wlf = new WikiLinkFinder();

		File folder = new File(folderPath);
		if (folder.exists() && folder.isDirectory()) {
			listOfFiles = folder.listFiles();
		} else {
			return null;
		} // maybe should throw exception instead?

		// Access each .txt file to create a new Page object for that file's
		// article
		for (int i = 0; i < listOfFiles.length; i++) {
			File currFile = listOfFiles[i];
			if (currFile.isFile()) {
				currPage = wikiPageFromFile(folder, currFile);
				pageTable.put(currPage.getLocation(), currPage);
			}
		}
		// now that all pages are loaded and their outlinks have been
		// determined,
		// we can determine a page's inlinks and then return the loaded table
		return pageTable = determineAllInlinks(pageTable);
	} // end loadPages()

	public static Page wikiPageFromFile(File folder, File f) {
		Page p;
		String pageLocation = getPageName(f); // will be like: \wiki\*article
												// name*.toLowercase()
		String content = loadFileText(folder, f); // get html source as string
		p = new Page(pageLocation); // create the page object
		p.setContent(content); // give the page its html source as a string
		p.getOutlinks().addAll(wlf.getOutlinks(p)); // search html source for
													// links
		return p;
	}

	public static Map<String, Page> determineAllInlinks(Map<String, Page> pageTable) {
		Page currPage;
		Set<String> keySet = pageTable.keySet();
		Iterator<String> keySetIterator = keySet.iterator();
		while (keySetIterator.hasNext()) {
			currPage = pageTable.get(keySetIterator.next());
			// add the inlinks to an currently empty List<String> object
			currPage.getInlinks().addAll(wlf.getInlinks(currPage, pageTable));
		}
		return pageTable;
	}

	public static String getPageName(File f) {

		String wikiPrefix = "/wiki/";
		String filename = f.getName();
		if (filename.indexOf(".") > 0)
			filename = filename.substring(0, filename.lastIndexOf("."));
		return wikiPrefix + filename.toLowerCase();
	} // end getPageName()

	public static String loadFileText(File folder, File file) {

		StringBuilder pageContent = new StringBuilder();
		BufferedReader br = null;

		// repeat for all files
		try {
			String sCurrentLine;
			String folderPath = folder.getAbsolutePath();
			String fileName = file.getName();

			br = new BufferedReader(new FileReader(folderPath + File.separator + fileName));

			while ((sCurrentLine = br.readLine()) != null) {
				pageContent.append(sCurrentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return pageContent.toString();
	} // end loadFileText()

	// TODO:
	// Be able to automatically retrieve an arbitrary number of
	// wikipaedia pages and create a hashtable of Pages from them.

	// TODO:
	// Be able to automatically retreive an arbitraru number of webpages
	// that are in a network conducive to application of the HITS algorithm
}
