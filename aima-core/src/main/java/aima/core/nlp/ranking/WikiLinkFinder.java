package aima.core.nlp.ranking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Jonathon Belotti (thundergolfer)
 *
 */
public class WikiLinkFinder implements LinkFinder {

	// TODO
	// Make more intelligent link search
	public List<String> getOutlinks(Page page) {

		String content = page.getContent();
		List<String> outLinks = new ArrayList<String>();
		// search content for all href="x" outlinks
		List<String> allMatches = new ArrayList<String>();
		Matcher m = Pattern.compile("href=\"(/wiki/.*?)\"").matcher(content);
		while (m.find()) {
			allMatches.add(m.group());
		}
		for (int i = 0; i < allMatches.size(); i++) {
			String match = allMatches.get(i);
			String[] tokens = match.split("\"");
			String location = tokens[1].toLowerCase(); // also, tokens[0] = the
														// text before the first
														// quote,
														// and tokens[2] is the
														// text after the second
														// quote
			outLinks.add(location);
		}

		return outLinks;
	}

	@Override
	public List<String> getInlinks(Page target, Map<String, Page> pageTable) {

		String location = target.getLocation().toLowerCase(); // make comparison
																// case
																// insensitive
		List<String> inlinks = new ArrayList<String>(); // initialise a list for
														// the inlinks

		// go through all pages and if they link back to target then add that
		// page's location to the target's inlinks
		Iterator<String> keySetIterator = pageTable.keySet().iterator();
		while (keySetIterator.hasNext()) {
			Page p = pageTable.get(keySetIterator.next());
			for (int i = 0; i < p.getOutlinks().size(); i++) {
				String pForward = p.getOutlinks().get(i).toLowerCase().replace('\\', '/');
				String pBackward = p.getOutlinks().get(i).toLowerCase().replace('/', '\\');
				if (pForward.equals(location) || pBackward.equals(location)) {
					inlinks.add(p.getLocation().toLowerCase());
					break;
				}
			}
		}
		return inlinks;
	}

}
