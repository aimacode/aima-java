package aima.gui.support.code;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 */
public class CodeRepresentation {
    public final String                   codeTypeName;
    public final String                   source;
    public final Map<String, CodeCommand> commandIdToCommand;

    public CodeRepresentation(String codeTypeName, String source, List<CodeCommand> codeCommands) {
        this.codeTypeName = codeTypeName;
        this.source       = source;

        Map<String, CodeCommand> cmdIdToCommand = new LinkedHashMap<>();
        codeCommands.forEach(cc -> cmdIdToCommand.put(cc.commandId, cc));
        this.commandIdToCommand = Collections.unmodifiableMap(cmdIdToCommand);
    }
}
