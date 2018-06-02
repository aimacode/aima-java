package aima.test.core.unit.learning.knowledge;

import org.junit.Test;

import java.util.HashMap;

public class CurrentBestLearning {
    @Test
    public void currentBestTest(){
        HashMap<String,String> test = new HashMap<>();
        String s = "alpha";
        test.put("a",s);
        test.put("b","Beta");
        System.out.println(test.toString());
        HashMap<String,String> testTwo = new HashMap<>(test);
        s+="gamma";
        System.out.println(test.toString());
        System.out.println(testTwo.toString());
    }
}
