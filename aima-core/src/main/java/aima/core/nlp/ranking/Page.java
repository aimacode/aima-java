package aima.core.nlp.ranking;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Jonathon Belotti (thundergolfer)
 *
 */
public class Page {

	public double authority;
	public double hub;
	private String location;
	private String content;
	private List<String> linkTo;
	private List<String> linkedFrom;

	public Page(String location) {
		authority = 0;
		hub = 0;
		this.location = location;
		this.linkTo = new ArrayList<String>();
		this.linkedFrom = new ArrayList<String>();
	}

	public String getLocation() {
		return location;
	}

	public String getContent() {
		return content;
	}

	public boolean setContent(String content) {
		this.content = content;
		return true;
	}

	public List<String> getInlinks() {
		return linkedFrom;
	}

	public List<String> getOutlinks() {
		return linkTo;
	}
}
