package aima.test.unit.data;

import aima.core.learning.LabeledExample;
import aima.core.learning.NominalAttribute;
import aima.core.learning.api.Attribute;
import aima.core.learning.api.Example;
import aima.core.learning.data.DataSet;
import aima.core.learning.data.DataSetBuilder;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author shantanusinghal
 */
public class GolfData {

  public static final String TRUE = "TRUE";
  public static final String FALSE = "FALSE";

  public enum Humid {
    TRUE {
      @Override
      public String toString() {
        return GolfData.TRUE;
      }
    }, FALSE {
      @Override
      public String toString() {
        return GolfData.FALSE;
      }
    };
    public static Attribute attribute() {
      return new NominalAttribute("humid", Arrays.asList(GolfData.TRUE, GolfData.FALSE));
    }
  }

  public enum Windy {
    TRUE {
      @Override
      public String toString() {
        return GolfData.TRUE;
      }
    }, FALSE {
      @Override
      public String toString() {
        return GolfData.FALSE;
      }
    };
    public static Attribute attribute() {
      return new NominalAttribute("windy", Arrays.asList(GolfData.TRUE, GolfData.FALSE));
    }
  }

  public enum Play {
    TRUE {
      @Override
      public String toString() {
        return GolfData.TRUE;
      }
    }, FALSE {
      @Override
      public String toString() {
        return GolfData.FALSE;
      }
    };
    public static Attribute attribute() {
      return new NominalAttribute("play", Arrays.asList(GolfData.TRUE, GolfData.FALSE));
    }
  }

  private static final DataSetBuilder DATA_SPEC = new DataSetBuilder()
      .withClassAttribute(Play.attribute())
      .withFeatureAttribute(Humid.attribute())
      .withFeatureAttribute(Windy.attribute());

  public static DataSet set() {
    return DATA_SPEC.build();
  }

  public static Example createExample(Humid humid, Windy windy, Play play) {
    return new LabeledExample(Play.attribute(), play.name(), new HashMap<Attribute, String>(){{
      put(Humid.attribute(), humid.toString());
      put(Windy.attribute(), windy.toString());
    }});
  }

}
