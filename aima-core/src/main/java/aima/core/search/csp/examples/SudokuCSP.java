package aima.core.search.csp.examples;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Domain;
import aima.core.search.csp.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    /**
     * Constructs a Sudoku CSP. Sets variables, their domains (possible values) and the constraints.
     */
    public SudokuCSP() {
        // Set Variables
        for (int y = 1; y <= 9; y++) {
            for (int x = 1; x <= 9; x++) {
                Variable variable = new Variable(String.valueOf(y) + String.valueOf(x));
                addVariable(variable);
            }
        }
        List<Variable> variables = getVariables();

        // Set Values
        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

        // Set Domains for all variables
        setDomains(values);

        // Set Constraints
        setAllDiffConstraints(variables);
    }

    /**
     * Setting the domain for all variables in the Sudoku game.
     * At the beginning all variables (fields in the game) could potentially be one number out of the domain {1, 2, 3, 4, 5, 6, 7, 8, 9}.
     *
     * @param values = domain = {1, 2, 3, 4, 5, 6, 7, 8, 9}.
     */
    private void setDomains(List<Integer> values) {
        Domain<Integer> domain = new Domain<>(values);
        for (Variable variable : getVariables())
            setDomain(variable, domain);
    }

    /**
     * Adding alldiff(...) constraints for the Sudoku grid.
     *
     * @param variables = fields in the Sudoku grid.
     */
    private void setAllDiffConstraints(List<Variable> variables) {
        // Rows
        for (int i = 0; i <= variables.size() - 9; i += 9) {
            addConstraint(new AllDiffConstraint<>(variables.subList(i, i + 9)));
            //System.out.println("allDiff(" + variables.subList(i, i + 9) + ")");
        }

        // Columns
        for (int i = 0; i < 9; i++) {
            List<Variable> constraintVars = new ArrayList<>();
            for (int j = i; j < variables.size(); j += 9) {
                constraintVars.add(variables.get(j));
            }
            addConstraint(new AllDiffConstraint<>(constraintVars));
            //System.out.println("allDiff(" + constraintVars + ")");
        }

        // Squares
        //TODO: this could be improved
        addConstraint((new AllDiffConstraint<>(Arrays.asList(
                variables.get(0), variables.get(1), variables.get(2),
                variables.get(9), variables.get(10), variables.get(11),
                variables.get(18), variables.get(19), variables.get(20)
        ))));

        addConstraint((new AllDiffConstraint<>(Arrays.asList(
                variables.get(3), variables.get(4), variables.get(5),
                variables.get(12), variables.get(13), variables.get(14),
                variables.get(21), variables.get(22), variables.get(23)
        ))));

        addConstraint((new AllDiffConstraint<>(Arrays.asList(
                variables.get(6), variables.get(7), variables.get(8),
                variables.get(13), variables.get(14), variables.get(15),
                variables.get(24), variables.get(25), variables.get(26)
        ))));

        //--------------------------------------------------------------

        addConstraint((new AllDiffConstraint<>(Arrays.asList(
                variables.get(27), variables.get(28), variables.get(29),
                variables.get(36), variables.get(37), variables.get(38),
                variables.get(45), variables.get(46), variables.get(47)
        ))));

        addConstraint((new AllDiffConstraint<>(Arrays.asList(
                variables.get(30), variables.get(31), variables.get(32),
                variables.get(39), variables.get(40), variables.get(41),
                variables.get(48), variables.get(49), variables.get(50)
        ))));

        addConstraint((new AllDiffConstraint<>(Arrays.asList(
                variables.get(33), variables.get(34), variables.get(35),
                variables.get(42), variables.get(43), variables.get(44),
                variables.get(51), variables.get(52), variables.get(53)
        ))));

        //--------------------------------------------------------------

        addConstraint((new AllDiffConstraint<>(Arrays.asList(
                variables.get(54), variables.get(55), variables.get(56),
                variables.get(63), variables.get(64), variables.get(65),
                variables.get(72), variables.get(73), variables.get(74)
        ))));

        addConstraint((new AllDiffConstraint<>(Arrays.asList(
                variables.get(57), variables.get(58), variables.get(59),
                variables.get(66), variables.get(67), variables.get(68),
                variables.get(75), variables.get(76), variables.get(77)
        ))));

        addConstraint((new AllDiffConstraint<>(Arrays.asList(
                variables.get(60), variables.get(61), variables.get(62),
                variables.get(69), variables.get(70), variables.get(71),
                variables.get(78), variables.get(79), variables.get(80)
        ))));
    }

    /**
     * Adding binary constraints for the Sudoku grid to be able to use e.g. the AC-3 algorithm.
     *
     * @param variables = fields in the Sudoku grid.
     */
    private void setBinaryConstraints(List<Variable> variables) {

    }

    /**
     * Printing the Sudoku board with a given assignment.
     *
     * @param assignments = the numbers that are already set in the Sudoku game a.k.a. the previous variable-value-assignments.
     */
    public static void printCSP(Map<String, Integer> assignments) {
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
        result.append("\n-------------------------\n");
        System.out.println(result);
    }

    public Assignment<Variable, Integer> getStartingAssignment() {

        /* Sudoku Board:
            1 2 3   4 5 6   7 8 9
          -------------------------
        1 | - - 1 | - - 9 | - - - |
        2 | 4 - 9 | 1 7 - | - - 2 |
        3 | - 3 5 | - 4 8 | 1 - - |
          | ------+-------+------ |
        4 | 9 - 6 | - - - | 3 7 - |
        5 | 1 - - | 7 3 6 | 4 - 9 |
        6 | 3 - - | 9 8 2 | - - - |
          | ------+-------+------ |
        7 | - - - | - 2 7 | - - 4 |
        8 | 6 9 4 | - 1 3 | - 5 7 |
        9 | - 7 2 | - - - | - - - |
          -------------------------
        */

        Assignment<Variable, Integer> assignment = new Assignment<>();
        for (Variable variable : getVariables()) {
            switch (variable.getName()) {
                case "13" -> assignment.add(variable, 1);
                case "16" -> assignment.add(variable, 9);

                case "21" -> assignment.add(variable, 4);
                case "23" -> assignment.add(variable, 9);
                case "24" -> assignment.add(variable, 1);
                case "25" -> assignment.add(variable, 7);
                case "29" -> assignment.add(variable, 2);

                case "32" -> assignment.add(variable, 3);
                case "33" -> assignment.add(variable, 5);
                case "35" -> assignment.add(variable, 4);
                case "36" -> assignment.add(variable, 8);
                case "37" -> assignment.add(variable, 1);

                case "41" -> assignment.add(variable, 9);
                case "43" -> assignment.add(variable, 6);
                case "47" -> assignment.add(variable, 3);
                case "48" -> assignment.add(variable, 7);

                case "51" -> assignment.add(variable, 1);
                case "54" -> assignment.add(variable, 7);
                case "55" -> assignment.add(variable, 3);
                case "56" -> assignment.add(variable, 6);
                case "57" -> assignment.add(variable, 4);
                case "59" -> assignment.add(variable, 9);

                case "61" -> assignment.add(variable, 3);
                case "64" -> assignment.add(variable, 9);
                case "65" -> assignment.add(variable, 8);
                case "66" -> assignment.add(variable, 2);

                case "75" -> assignment.add(variable, 2);
                case "76" -> assignment.add(variable, 7);
                case "79" -> assignment.add(variable, 4);

                case "81" -> assignment.add(variable, 6);
                case "82" -> assignment.add(variable, 9);
                case "83" -> assignment.add(variable, 4);
                case "85" -> assignment.add(variable, 1);
                case "86" -> assignment.add(variable, 3);
                case "88" -> assignment.add(variable, 5);
                case "89" -> assignment.add(variable, 7);

                case "92" -> assignment.add(variable, 7);
                case "93" -> assignment.add(variable, 2);
            }
        }
        return assignment;
    }

}
