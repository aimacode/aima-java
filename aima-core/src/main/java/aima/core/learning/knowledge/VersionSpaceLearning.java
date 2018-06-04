package aima.core.learning.knowledge;

import aima.core.learning.framework.Example;

import java.util.List;

public class VersionSpaceLearning {
    public VersionSpace versionSpaceLearning(List<LogicalExample> examples){
        VersionSpace v = new VersionSpace();
        for (LogicalExample e :
                examples) {
            if (v != null) {
                v = versionSpaceUpdate(v,e);
            }
        }
        return v;
    }

    private VersionSpace versionSpaceUpdate(VersionSpace v, LogicalExample e) {
        if (e.getGoal() && !v.predictFromSpecialisedSet(e)){
            v.setMostSpecificSet(v.immediateGeneralisation(v.getMostSpecificSet(),e));
        }
        if (!e.getGoal() && v.predictFromGeneralisedSet(e)){
            v.setMostGeneralSet(v.immediateSpecialisation(v.getMostGeneralSet(),e));
        }
        return v;
    }
}
