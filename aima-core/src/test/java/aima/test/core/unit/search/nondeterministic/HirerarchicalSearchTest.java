package aima.core.search.nondeterministic;

import static org.junit.Assert.*;
import org.junit.Test;

public class HirerarchicalSearchTest {
	@Test
	public void test1_Hierarchical_Search() {
		 Problem p=new Problem();
	       p.initialState="Home";
	       p.goal="San Francisco Airport";
	       HierarchicalSearch hr=new HierarchicalSearch();
	       hr.frontier[0]="Home to San Fransisco Airport";
	       hr.front++;
	       hr.hla[0]="Home";
	       hr.hla[1]="Walk";
	       hr.hla[2]="Taxi";
	       hr.hla[3]="Get Inside Airport";
	       hr.hla[4]="San Francisco airport";
	       hr.hla[5]=""; //to mark end of hls's
           hr.storePrefixSuffix[0]=""; //no action before it
           hr.storePrefixSuffix[1]="Walk";
           hr.storePrefixSuffix[2]="Move Legs";
           hr.storePrefixSuffix[3]="Taxi Stand";
           hr.storePrefixSuffix[4]="Taxi";
           hr.storePrefixSuffix[5]="Get Inside Airport";
           hr.storePrefixSuffix[6]="Taxi Stand";
           hr.storePrefixSuffix[7]="San Francisco Airport";
           hr.storePrefixSuffix[8]="Get Inside Airport";
           hr.storeOutcome[0]="Home";
           hr.storeOutcome[1]="Walk";
           hr.storeOutcome[2]="Taxi";
           hr.storeOutcome[3]="Get Inside Airport";
           hr.storeOutcome[4]="San Francisco Airport";
           hr.storeOutcome[5]="San Francisco Airport";
          String actualResult="Home -> Move Legs -> Walk -> Taxi -> Taxi Stand -> Taxi Stand -> Get Inside Airport -> Get Inside Airport -> San Francisco Airport";
	       assertEquals("Qwerty",actualResult,hr.Hierarchical_Search(p,8));
	}
}
