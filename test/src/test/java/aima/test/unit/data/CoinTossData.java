package aima.test.unit.data;

import aima.core.learning.LabeledExample;
import aima.core.learning.NominalAttribute;
import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.data.DataSet;
import aima.core.learning.data.DataSetBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * @author shantanusinghal
 */
public class CoinTossData {

  public static final String YES = "YES";
  public static final String NO = "NO";
  public static final String HEADS = "HEADS";
  public static final String TAILS = "TAILS";

  public enum FingersCrossed {
    YES {
      @Override
      public String toString() {
        return CoinTossData.YES;
      }
    }, NO {
      @Override
      public String toString() {
        return CoinTossData.NO;
      }
    };
    public static Attribute attribute() {
      return new NominalAttribute("fingersCrossed", Arrays.asList(CoinTossData.YES, CoinTossData.NO));
    }
  }

  public enum TossResult {
    HEADS {
      @Override
      public String toString() {
        return CoinTossData.HEADS;
      }
    }, TAILS {
      @Override
      public String toString() {
        return CoinTossData.TAILS;
      }
    };
    public static Attribute attribute() {
      return new NominalAttribute("tossResult", Arrays.asList(CoinTossData.HEADS, CoinTossData.TAILS));
    }
  }

  private static final DataSetBuilder DATA_SPEC = new DataSetBuilder()
      .withClassAttribute(TossResult.attribute())
      .withFeatureAttribute(FingersCrossed.attribute());

  public static DataSet set() {
    return DATA_SPEC.build();
  }

  public static Example createExample(TossResult tossResult) {
    return new LabeledExample(TossResult.attribute(), tossResult.name(), Collections.emptyMap());
  }

  public static Example createExample(FingersCrossed fingersCrossed, TossResult tossResult) {
    return new LabeledExample(TossResult.attribute(), tossResult.name(), new HashMap<Attribute, String>(){{
      put(FingersCrossed.attribute(), fingersCrossed.toString());
    }});
  }

}
