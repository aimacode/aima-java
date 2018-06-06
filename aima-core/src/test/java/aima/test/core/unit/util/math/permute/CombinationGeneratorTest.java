package aima.test.core.unit.util.math.permute;

import aima.core.util.math.permute.CombinationGenerator;
import aima.core.util.math.permute.PermutationGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CombinationGeneratorTest {

    @Test
    public void generateCombinationsTest(){
        List<String> strings = Arrays.asList("Spare","Trunk","Flat","Axle","Ground");
        for (List<String> possibleCombinations :
                CombinationGenerator.generateCombinations(strings,3)) {
            for (String s :
                    possibleCombinations) {
                System.out.print(s);
            }
            System.out.println();
        }
    }
    @Test
    public void generatePermutationsTest(){
        List<String> strings = Arrays.asList("Spare","Trunk","Flat","Axle","Ground");
        for (List<String> possiblePermutations :
                PermutationGenerator.generatePermutations(strings,3)) {
            for (String s :
                    possiblePermutations) {
                System.out.print(s);
            }
            System.out.println();
        }
    }
}
