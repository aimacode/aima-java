package aima.core.learning.knowledge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class VersionSpace {

    private HashSet<Hypothesis> mostGeneralSet = new HashSet<>();
    private HashSet<Hypothesis> mostSpecificSet = new HashSet<>();

    public VersionSpace() {
    }

    public HashSet<Hypothesis> immediateGeneralisation(HashSet<Hypothesis> sSet,LogicalExample e){
        Hypothesis toAdd = new Hypothesis("",
                new ArrayList<>(Collections.singletonList(
                                new HashMap<>(e.getAttributes()))));
        sSet.add(toAdd);
        return sSet;
    }

    public HashSet<Hypothesis> immediateSpecialisation(HashSet<Hypothesis> gSet, LogicalExample example){
        Hypothesis toAdd = new Hypothesis("",
                new ArrayList<>(Collections.singletonList(
                        new HashMap<>(example.getAttributes()))));
        gSet.add(toAdd);
        return gSet;
    }

    public boolean predictFromGeneralisedSet(LogicalExample example){
        if (this.mostGeneralSet.isEmpty())
            return true;
        for (Hypothesis hypothesis :
             this.mostGeneralSet) {
            if (hypothesis.predict(example))
                return false;
        }
        return true;
    }

    public boolean predictFromSpecialisedSet(LogicalExample example){
        if (this.mostSpecificSet.isEmpty())
            return false;
        for (Hypothesis hypothesis :
                this.mostSpecificSet) {
            if (hypothesis.predict())
                return true;
        }
        return false;
    }
}
