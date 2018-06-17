package aima.core.learning.knowledge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

/**
 * We can represent the entire version space using just two boundary sets: a most general boundary (the G-set) and a most
 * specific boundary (the S-set). Everything in between is guaranteed to be consistent with the
 * examples.
 *
 * @author samagra
 */
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
            if (hypothesis.predict(example))
                return true;
        }
        return false;
    }

    public HashSet<Hypothesis> getMostGeneralSet() {
        return mostGeneralSet;
    }

    public void setMostGeneralSet(HashSet<Hypothesis> mostGeneralSet) {
        this.mostGeneralSet = mostGeneralSet;
    }

    public void setMostSpecificSet(HashSet<Hypothesis> mostSpecificSet) {
        this.mostSpecificSet = mostSpecificSet;
    }

    public HashSet<Hypothesis> getMostSpecificSet() {
        return mostSpecificSet;
    }

    public boolean predict(LogicalExample example){
        boolean fromGeneral = this.predictFromGeneralisedSet(example);
        boolean fromSpecific = this.predictFromSpecialisedSet(example);
        if (fromGeneral==fromSpecific)
            return fromGeneral;
        if (Math.random()<0.5)
            return fromGeneral;
        else
            return fromSpecific;
    }
}
