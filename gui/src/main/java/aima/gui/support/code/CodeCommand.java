package aima.gui.support.code;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ciaran O'Reilly
 */
public class CodeCommand {
    public final String            commandId;
    public final List<SourceIndex> sourceIndexes;

    public CodeCommand(String commandId, List<SourceIndex> sourceIndexes) {
        this.commandId     = commandId;
        this.sourceIndexes = Collections.unmodifiableList(new ArrayList<>(sourceIndexes));
    }
}
