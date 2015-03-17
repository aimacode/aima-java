package aima.gui.support.code;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 */
public class CodeRepresentation {
    public final String                   codeTypeName;
    public final List<String>             sourceLines;
    public final Map<String, CodeCommand> commandIdToCommand;

    public CodeRepresentation(String codeTypeName, List<String> sourceLines, List<CodeCommand> codeCommands) {
        this.codeTypeName = codeTypeName;
        this.sourceLines = Collections.unmodifiableList(new ArrayList<>(sourceLines));

        Map<String, CodeCommand> cmdIdToCommand = new HashMap<>();
        codeCommands.forEach(cc -> cmdIdToCommand.put(cc.commandId, cc));
        this.commandIdToCommand = Collections.unmodifiableMap(cmdIdToCommand);
    }
}
