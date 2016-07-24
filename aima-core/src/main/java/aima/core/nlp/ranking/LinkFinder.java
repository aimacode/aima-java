package aima.core.nlp.ranking;

import java.util.Hashtable;
import java.util.List;

public interface LinkFinder {
	
	/**
	 * Take a Page object and return its outlinks as a list of strings.
	 * The Page object must therefore possess the information to determine
	 * what it links to.
	 * @param page
	 * @return
	 */
	public List<String> getOutlinks( Page page );
	
	/**
	 * Take a Page object and return its inlinks (who links to it) as a list of strings.
	 * @param page
	 * @param pageTable
	 * @return
	 */
	public List<String> getInlinks( Page page, Hashtable<String,Page> pageTable );

}
