package aima.core.nlp.ranking;

import java.util.Hashtable;
import java.util.List;

public class RunHITS {
	
	public static void main( String[] args ) {	
		List<Page> result;
		// build page table 
		Hashtable<String,Page> pageTable = PagesDataset.loadDefaultPages();
		// Create HITS Ranker 
		HITS hits = new HITS(pageTable);
		// run hits
		System.out.println("Ranking...");
		result = hits.rank("man is");
		// report results
		System.out.println("Ranking Finished.");
		hits.report(result);
	}

}
