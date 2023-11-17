package aima.core.search.csp.examples;

import aima.core.environment.sudoku.SudokuDifficulty;
import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Domain;
import aima.core.search.csp.Variable;

import java.util.*;

/**
 * Sudoku
 * This is another typical example for a constraint satisfaction problem (CSP).
 * It consists of a board with 9x9 fields (variables) and for each a value from 1 to 9 can
 * be assigned.
 * In all rows, every value is only allowed to be assigned once. In all columns, every value
 * is only allowed to be assigned once. If the 9x9 Sudoku is divided into nine non-overlapping
 * 3x3 squares, every value can only be assigned once in a 3x3 square.
 * <br>
 * A description of the Sudoku constraint propagation problem can be found in
 * Artificial Intelligence A Modern Approach (4th Ed.): Page 173 - 175.
 * <br>
 * Sudoku Board:
 * variable name: [row|col]
 * <br>
 *      1  2  3    4  5  6    7  8  9
 *   ----------------------------------
 * 1 | 11 12 13 | 14 15 16 | 17 18 18 |
 * 2 | 21 22 23 | 24 25 26 | 27 28 28 |
 * 3 | 31 32 33 | 34 35 36 | 37 38 38 |
 *   | ---------+----------+--------- |
 * 4 | 41 42 43 | 44 45 46 | 47 48 48 |
 * 5 | 51 52 53 | 54 55 56 | 57 58 58 |
 * 6 | 61 62 63 | 64 65 66 | 67 68 68 |
 *   | ---------+----------+--------- |
 * 7 | 71 72 73 | 74 75 76 | 77 78 78 |
 * 8 | 81 82 83 | 84 85 86 | 87 88 88 |
 * 9 | 91 92 93 | 94 95 96 | 97 98 98 |
 *   ----------------------------------
 */
public class SudokuCSP extends CSP<Variable, Integer> {

    List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

    /**
     * Constructs a Sudoku CSP. Sets variables, their domains (possible values) and the constraints.
     */
    public SudokuCSP() {
        setVariables();
        setDomains();
        setConstraints();
    }

    /**
     * Setting the variables for the Sudoku game. Variables are named like in the example above.
     */
    private void setVariables() {
        for (int y = 1; y <= 9; y++) {
            for (int x = 1; x <= 9; x++) {
                Variable variable = new Variable(String.valueOf(y) + String.valueOf(x));
                addVariable(variable);
            }
        }
    }

    /**
     * Setting the domain for all variables in the Sudoku game.
     * At the beginning all variables (fields in the game) could potentially be one number out of the domain {1, 2, 3, 4, 5, 6, 7, 8, 9}.
     */
    private void setDomains() {
        Domain<Integer> domain = new Domain<>(this.values);
        for (Variable variable : getVariables())
            setDomain(variable, domain);
    }

    /**
     * Adding alldiff() constraints for the Sudoku grid.
     */
    private void setConstraints() {
        // alldiff() constraints for a csp
        List<AllDiffConstraint<Variable, Integer>> allDiffConstraints = getAllDiffConstraints(getVariables());
        List<NotEqualConstraint<Variable, Integer>> notEqualConstraints = new ArrayList<>();

        // Transforming alldiff() constraints to not-equal constraints
        for (AllDiffConstraint<Variable, Integer> allDiffConstraint : allDiffConstraints) {
            notEqualConstraints.addAll(allDiffConstraint.getNotEqualConstraints());
        }

        // Adding not-equal constraints
        for (NotEqualConstraint<Variable, Integer> notEqualConstraint : notEqualConstraints) {
            addConstraint(notEqualConstraint);
        }
    }

    /**
     * Getting the alldiff() constraints for a Sukoku csp.
     *
     * @param variables all variables of the csp.
     * @return a list of all necessary alldiff() constraints
     */
    private List<AllDiffConstraint<Variable, Integer>> getAllDiffConstraints(List<Variable> variables) {
        List<AllDiffConstraint<Variable, Integer>> allDiffConstraints = new ArrayList<>();

        // Rows
        for (int i = 0; i <= variables.size() - 9; i += 9) {
            allDiffConstraints.add(new AllDiffConstraint<>(variables.subList(i, i + 9)));
            //System.out.println("allDiff(" + variables.subList(i, i + 9) + ")");
        }

        // Columns
        for (int i = 0; i < 9; i++) {
            List<Variable> constraintVars = new ArrayList<>();
            for (int j = i; j < variables.size(); j += 9) {
                constraintVars.add(variables.get(j));
            }
            allDiffConstraints.add(new AllDiffConstraint<>(constraintVars));
        }

        // Squares
        //TODO: this could be improved
        allDiffConstraints.add((new AllDiffConstraint<>(Arrays.asList(
                variables.get(0), variables.get(1), variables.get(2),
                variables.get(9), variables.get(10), variables.get(11),
                variables.get(18), variables.get(19), variables.get(20)
        ))));

        allDiffConstraints.add((new AllDiffConstraint<>(Arrays.asList(
                variables.get(3), variables.get(4), variables.get(5),
                variables.get(12), variables.get(13), variables.get(14),
                variables.get(21), variables.get(22), variables.get(23)
        ))));

        allDiffConstraints.add((new AllDiffConstraint<>(Arrays.asList(
                variables.get(6), variables.get(7), variables.get(8),
                variables.get(15), variables.get(16), variables.get(17),
                variables.get(24), variables.get(25), variables.get(26)
        ))));

        //--------------------------------------------------------------

        allDiffConstraints.add((new AllDiffConstraint<>(Arrays.asList(
                variables.get(27), variables.get(28), variables.get(29),
                variables.get(36), variables.get(37), variables.get(38),
                variables.get(45), variables.get(46), variables.get(47)
        ))));

        allDiffConstraints.add((new AllDiffConstraint<>(Arrays.asList(
                variables.get(30), variables.get(31), variables.get(32),
                variables.get(39), variables.get(40), variables.get(41),
                variables.get(48), variables.get(49), variables.get(50)
        ))));

        allDiffConstraints.add((new AllDiffConstraint<>(Arrays.asList(
                variables.get(33), variables.get(34), variables.get(35),
                variables.get(42), variables.get(43), variables.get(44),
                variables.get(51), variables.get(52), variables.get(53)
        ))));

        //--------------------------------------------------------------

        allDiffConstraints.add((new AllDiffConstraint<>(Arrays.asList(
                variables.get(54), variables.get(55), variables.get(56),
                variables.get(63), variables.get(64), variables.get(65),
                variables.get(72), variables.get(73), variables.get(74)
        ))));

        allDiffConstraints.add((new AllDiffConstraint<>(Arrays.asList(
                variables.get(57), variables.get(58), variables.get(59),
                variables.get(66), variables.get(67), variables.get(68),
                variables.get(75), variables.get(76), variables.get(77)
        ))));

        allDiffConstraints.add((new AllDiffConstraint<>(Arrays.asList(
                variables.get(60), variables.get(61), variables.get(62),
                variables.get(69), variables.get(70), variables.get(71),
                variables.get(78), variables.get(79), variables.get(80)
        ))));

        return allDiffConstraints;
    }

    /**
     * Printing the Sudoku board with a given assignment.
     *
     * @param assignment = the numbers that are already set in the Sudoku game a.k.a. the previous variable-value-assignments.
     */
    public static void printCSP(Assignment<Variable, Integer> assignment) {
        Map<String, Integer> assignments = new HashMap<>();

        for (Variable variable : assignment.getVariables()) {
            assignments.put(variable.getName(), assignment.getValue(variable));
        }

        StringBuilder result = new StringBuilder("-------------------------");

        for (int y = 1; y <= 9; y++) { //for (char y = 'A'; y <= 'I'; y++) {
            result.append("\n| ");
            for (int x = 1; x <= 9; x++) {
                String value = "-";
                if (assignments.containsKey(String.valueOf(y) + String.valueOf(x)))
                    value = String.valueOf(assignments.get(String.valueOf(y) + String.valueOf(x)));
                result.append((x % 3 == 0) ? value + " | " : value + " ");
            }
            result.append((y % 3 == 0 && y % 9 != 0) ? "\n| ------+-------+------ |" : "");
        }
        result.append("\n-------------------------");
        System.out.println(result);
    }

    /**
     * Getting a possible start assignment for a Sudoku to solve.
     *
     * @return the assignment.
     */
    public Assignment<Variable, Integer> getStartingAssignment(SudokuDifficulty difficulty) {
        Assignment<Variable, Integer> assignment = new Assignment<>();
        if (difficulty == SudokuDifficulty.EASY) {
            for (Variable variable : getVariables()) {
                switch (variable.getName()) {
                    case "13", "24", "37", "51", "85" -> assignment.add(variable, 1);
                    case "29", "66", "75", "93" -> assignment.add(variable, 2);
                    case "32", "47", "55", "61", "86" -> assignment.add(variable, 3);
                    case "21", "35", "57", "79", "83" -> assignment.add(variable, 4);
                    case "33", "88" -> assignment.add(variable, 5);
                    case "43", "56", "81" -> assignment.add(variable, 6);
                    case "25", "48", "54", "76", "89", "92" -> assignment.add(variable, 7);
                    case "36", "65" -> assignment.add(variable, 8);
                    case "16", "23", "41", "59", "64", "82" -> assignment.add(variable, 9);
                }
            }
        } else if (difficulty == SudokuDifficulty.MEDIUM) {
            for (Variable variable : getVariables()) {
                switch (variable.getName()) {
                    case "27", "31", "69", "78", "86", "93" -> assignment.add(variable, 1);
                    case "11", "72" -> assignment.add(variable, 2);
                    case "32", "68", "95" -> assignment.add(variable, 3);
                    case "16", "22", "38", "45", "59", "61" -> assignment.add(variable, 4);
                    case "23" -> assignment.add(variable, 5);
                    case "29", "71", "87", "96" -> assignment.add(variable, 6);
                    case "67", "79", "91" -> assignment.add(variable, 7);
                    case "26", "44", "62" -> assignment.add(variable, 8);
                    case "35", "49", "63", "88" -> assignment.add(variable, 9);
                }
            }
        } else {
            for (Variable variable : getVariables()) {
                switch (variable.getName()) {
                    case "15", "58", "87" -> assignment.add(variable, 1);
                    case "13", "29", "55" -> assignment.add(variable, 2);
                    case "35", "94" -> assignment.add(variable, 3);
                    case "18", "56" -> assignment.add(variable, 4);
                    case "19", "44", "78", "86" -> assignment.add(variable, 5);
                    case "22", "53", "74" -> assignment.add(variable, 6);
                    case "16", "85", "93" -> assignment.add(variable, 7);
                    case "61" -> assignment.add(variable, 8);
                    case "67", "82" -> assignment.add(variable, 9);
                }
            }
        }
        return assignment;
    }

    /**
     * Setting the right domain for the variables contained in the starting assignment.
     */
    public void setDomainsForStartingAssignment(Assignment<Variable, Integer> assignment) {
        for (Variable var : assignment.getVariables()) {
            if (assignment.getValue(var) != null) {
                setDomain(var, new Domain<>(assignment.getValue(var)));
            }
        }
    }
}