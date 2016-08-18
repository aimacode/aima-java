package aima.core.nlp.ranking;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Jonathon Belotti (thundergolfer)
 *
 */
public interface LinkFinder {

	/**
	 * Take a Page object and return its outlinks as a list of strings. The Page
	 * object must therefore possess the information to determine what it links
	 * to.
	 * 
	 * @param page
	 * @return
	 */
	List<String> getOutlinks(Page page);

	/**
	 * Take a Page object and return its inlinks (who links to it) as a list of
	 * strings.
	 * 
	 * @param page
	 * @param pageTable
	 * @return
	 */
	List<String> getInlinks(Page page, Map<String, Page> pageTable);

}
