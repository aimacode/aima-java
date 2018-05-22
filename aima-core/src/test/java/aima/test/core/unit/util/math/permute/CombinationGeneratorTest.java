package aima.test.core.unit.util.math.permute;

import aima.core.util.math.permute.CombinationGenerator;
import aima.core.util.math.permute.PermutationGenerator;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CombinationGeneratorTest {
    @Test
    public void combinationTest(){
        for (List<Integer> arr :
                CombinationGenerator.combination(3, 5)) {
            for (int a: arr){
                System.out.print(a);
            }
            System.out.println();
        }
    }

    @Test
    public void permutationTest(){
        for (List<Integer> arr :
                PermutationGenerator.permutations(4)) {
            for (int a: arr){
                System.out.print(a);
            }
            System.out.println();
        }
    }

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
