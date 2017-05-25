package aima.core.search.csp;

import aima.core.search.framework.Metrics;

/**
 * Interface which allows interested clients to register at a CSP solver
 * and follow its progress step by step.
 *
 * @author Ruediger Lunde
 */
public interface CspListener<VAR extends Variable, VAL> {
    /**
     * Informs about changed assignments and inference steps.
     *
     * @param csp        a CSP, possibly changed by an inference step.
     * @param assignment a new assignment or null if the last processing step was an inference step.
     * @param variable   a variable, whose domain or assignment value has been changed (may be null).
     */
    void stateChanged(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR variable);

    /**
     * A simple CSP listener implementation which counts assignment changes and changes caused by
     * inference steps and provides some metrics.
     * @author Ruediger Lunde
     */
    class StepCounter<VAR extends Variable, VAL> implements CspListener<VAR, VAL> {
        private int assignmentCount = 0;
        private int inferenceCount = 0;

        @Override
        public void stateChanged(CSP<VAR, VAL> csp, Assignment<VAR, VAL> assignment, VAR variable) {
            if (assignment != null)
                ++assignmentCount;
            else
                ++inferenceCount;
        }

        public void reset() {
            assignmentCount = 0;
            inferenceCount = 0;
        }

        public Metrics getResults() {
            Metrics result = new Metrics();
            result.set("assignmentCount", assignmentCount);
            if (inferenceCount != 0)
                result.set("inferenceCount", inferenceCount);
            return result;
        }
    }
}
