package aima.core.learning.knowledge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author samagra
 */
public class KnowledgeLearningProblemFactory {
    public static List<LogicalExample> getRestaurantLogicalExample() {
        String[] attrs = {"Alt", "Bar", "Fri", "Hun", "Pat", "Price", "Rain", "Res", "Type", "Est"};
        List<String> attributes = Arrays.asList(attrs);
        List<LogicalExample> result = new ArrayList<>();
        result.add(getExample(attributes, true, "Yes", "No", "No", "Yes", "Some",
                "$$$", "No", "Yes", "French", "0-10"));
        result.add(getExample(attributes, false, "Yes", "No", "No", "Yes", "Full",
                "$", "No", "No", "Thai", "30-60"));
        result.add(getExample(attributes, true, "No", "Yes", "No", "No", "Some",
                "$", "No", "No", "Burger", "0-10"));
        result.add(getExample(attributes, true, "Yes", "No", "Yes", "Yes", "Full",
                "$", "Yes", "No", "Thai", "10-30"));
        result.add(getExample(attributes, false, "Yes", "No", "Yes", "No", "Full",
                "$$$", "No", "Yes", "French", ">60"));
        result.add(getExample(attributes, true, "No", "Yes", "No", "Yes", "Some",
                "$$", "Yes", "Yes", "Italian", "0-10"));
        result.add(getExample(attributes, false, "No", "Yes", "No", "No", "None",
                "$", "Yes", "No", "Burger", "0-10"));
        result.add(getExample(attributes, true, "No", "No", "No", "Yes", "Some",
                "$$", "Yes", "Yes", "Thai", "0-10"));
        result.add(getExample(attributes, false, "No", "Yes", "Yes", "No", "Full",
                "$$", "Yes", "No", "Burger", ">60"));
        result.add(getExample(attributes, false, "Yes", "Yes", "Yes", "Yes", "Full",
                "$$$", "No", "Yes", "Italian", "10-30"));
        result.add(getExample(attributes, false, "No", "No", "No", "No", "None",
                "$", "No", "No", "Thai", "0-10"));
        result.add(getExample(attributes, true, "Yes", "Yes", "Yes", "Yes", "Full",
                "$", "No", "No", "Burger", "30-60"));

        return result;
    }

    public static List<LogicalExample> getConductanceMeasurementProblem(){
        String[] attrs = {"Sample", "Mass", "Temperature", "Material", "Size", "Goal"};
        List<String> attributes = Arrays.asList(attrs);
        List<LogicalExample> result = new ArrayList<>();
        result.add(getExample(attributes,"S1","12","26","Copper","3","0.59"));
        result.add(getExample(attributes,"S1","12","100","Copper","3","0.57"));
        result.add(getExample(attributes,"S2","24","26","Copper","6","0.59"));
        result.add(getExample(attributes, "S3","12","26","Lead","2","0.05"));
        result.add(getExample(attributes, "S3","12","100","Lead","2","0.04"));
        result.add(getExample(attributes,"S4","24","26","Lead","4","0.05"));
        result.add(getExample(attributes,"S5","28","26","Iron","4","0.55"));
        return result;
    }

    public static LogicalExample getExample(List<String> attrs,
                                            boolean goal, String... values) {
        try {
            return new LogicalExample(attrs, new ArrayList<>(Arrays.asList(values)), goal);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static LogicalExample getExample(List<String> attrs, String... values) {
        try {
            return new LogicalExample(attrs, new ArrayList<>(Arrays.asList(values)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
