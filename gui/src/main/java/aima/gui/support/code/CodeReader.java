package aima.gui.support.code;

import aima.extra.instrument.search.TreeSearchInstrumented;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;


/**
 * @author Ciaran O'Reilly
 */
public class CodeReader {
    public static final String CODE_TYPE_PREFIX   = "@CODE:";
    public static final String CODE_MARKER_PREFIX = "@";

    // Test Main
    public static void main(String[] args) {
        read("tree-search.code", TreeSearchInstrumented.CMDS);
    }

    public static List<CodeRepresentation> read(String codeFileName, Set<String> expectedCmds) {
        List<CodeRepresentation> result = new ArrayList<>();

        StringBuilder codeTypeName = new StringBuilder();
        StringBuilder source       = new StringBuilder();
        Map<String, List<SourceIndex>> codeCommands = new LinkedHashMap<>();

        try {
            URI uri = CodeReader.class.getResource(codeFileName).toURI();
            // Need to do this if reading form jar file.
            if (uri.toString().contains("!")) {
                final Map<String, String> env = new HashMap<>();
                final String[] array = uri.toString().split("!");
                try (FileSystem fs  = FileSystems.newFileSystem(URI.create(array[0]), env)) {
                    Path path = fs.getPath(array[1]);
                    try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
                        getContent(lines, codeTypeName, source, codeCommands, result);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            else {
                try (Stream<String> lines = Files.lines(Paths.get(uri), StandardCharsets.UTF_8)) {
                    getContent(lines, codeTypeName, source, codeCommands, result);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // Ensure the last code representation is included as well
        if (codeTypeName.length() > 0) {
            result.add(new CodeRepresentation(codeTypeName.toString(), source.toString(), convert(codeCommands)));
        }

        result.forEach(cr -> {
            if (!cr.commandIdToCommand.keySet().equals(expectedCmds)) {
                throw new IllegalArgumentException("Code Representation is missing command ids :\n" + cr.commandIdToCommand.keySet() + "\n" + expectedCmds);
            }
        });

        return result;
    }

    private static void getContent(Stream<String> lines,
                                   StringBuilder codeTypeName,
                                   StringBuilder source,
                                   Map<String, List<SourceIndex>> codeCommands,
                                   List<CodeRepresentation> result) {
        lines.forEach(line -> {
            if (line.startsWith(CODE_TYPE_PREFIX)) {
                // If we have already processed a code representation
                if (codeTypeName.length() > 0) {
                    result.add(new CodeRepresentation(codeTypeName.toString(), source.toString(), convert(codeCommands)));
                }

                codeTypeName.setLength(0);
                source.setLength(0);
                codeTypeName.append(line.substring(CODE_TYPE_PREFIX.length()));
            }
            else {
                process(line, source, codeCommands);
            }
        });
    }

    private static void process(String line, StringBuilder source, Map<String, List<SourceIndex>> codeCommands) {
        int s = 0;
        while (s < line.length()) {
            int m = line.indexOf(CODE_MARKER_PREFIX, s);
            if (m == -1) {
                source.append(line.substring(s, line.length()));
                s = line.length();
            }
            else {
                if (s < m) {
                    source.append(line.substring(s, m));
                }
                s = processMarker(m, line, source, codeCommands);
            }
        }
        source.append("\n");
    }

    private static int processMarker(int s1, String line, StringBuilder source, Map<String, List<SourceIndex>> codeCommands) {
        int e1 = line.indexOf(CODE_MARKER_PREFIX, s1+1);
        int s2 = line.indexOf(CODE_MARKER_PREFIX, e1+1);
        int e2 = line.indexOf(CODE_MARKER_PREFIX, s2+1);

        if (e1 == -1 || s2 == -1 || e2 == -1) {
            throw new IllegalArgumentException("Code line has invalid code command marker starting at "+s1+" :\n"+line);
        }

        String c1 = line.substring(s1+1, e1);
        String c2 = line.substring(s2+1, e2);

        if (!c1.equals(c2)) {
            throw new IllegalArgumentException("Code marker start and begin tags do not match up ("+c1+", "+c2+") :\n"+line);
        }

        List<SourceIndex> sis = codeCommands.get(c1);
        if (sis == null) {
            sis = new ArrayList<>();
            codeCommands.put(c1, sis);
        }

        String sourceFragment = line.substring(e1+1, s2);
        sis.add(new SourceIndex(source.length(), source.length() + sourceFragment.length()));
        source.append(sourceFragment);

        return e2+1;
    }

    private static List<CodeCommand> convert(Map<String, List<SourceIndex>> commandIdsToSourceIndexes) {
        final List<CodeCommand> result = new ArrayList<>();

        commandIdsToSourceIndexes.forEach((k, v) -> result.add(new CodeCommand(k, v)));

        commandIdsToSourceIndexes.clear();

        return result;
    }
}
