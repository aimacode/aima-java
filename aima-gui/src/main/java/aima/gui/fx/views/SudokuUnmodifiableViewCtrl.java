package aima.gui.fx.views;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Variable;
import aima.core.search.csp.examples.SudokuCSP;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.util.*;

public class SudokuUnmodifiableViewCtrl {

    private SudokuCSP csp;
    private Assignment<Variable, Integer> assignment;

    private final GridPane gridPane = new GridPane();
    private List<Integer> boldValues;

    public SudokuUnmodifiableViewCtrl(StackPane viewRoot) {
        viewRoot.getChildren().add(gridPane);
        viewRoot.setAlignment(Pos.BOTTOM_CENTER);
        gridPane.maxWidthProperty().bind(Bindings.min(viewRoot.widthProperty(), viewRoot.heightProperty()).subtract(20));
        gridPane.maxHeightProperty().bind(Bindings.min(viewRoot.widthProperty(), viewRoot.heightProperty()).subtract(10));
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setStyle("-fx-background-color: lightgray;");

        RowConstraints c1 = new RowConstraints();
        c1.setPercentHeight(100.0 / 9);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setPercentWidth(100.0 / 9);
        for (int i = 0; i < 9; i++) {
            gridPane.getRowConstraints().add(c1);
            gridPane.getColumnConstraints().add(c2);
        }
    }

    public void initialize(SudokuCSP csp, Assignment<Variable, Integer> startAssignment) {
        this.csp = csp;
        this.boldValues = new ArrayList<>();
        for (Variable variable : startAssignment.getVariables()) {
            int row = Integer.parseInt(String.valueOf(variable.getName().charAt(0))) - 1;
            int col = Integer.parseInt(String.valueOf(variable.getName().charAt(1))) - 1;
            boldValues.add(row * 9 + col);
        }
        update(csp, startAssignment);
    }

    public void update(SudokuCSP csp, Assignment<Variable, Integer> assignment) {
        if (assignment != null)
            this.assignment = assignment;
        if (csp != null)
            this.csp = csp;

        gridPane.getChildren().clear();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String value = "";
                String domain = "{1, 2, 3, 4, 5, 6, 7, 8, 9}";

                int currentRow = row;
                int currentCol = col;
                Variable currentVariable = this.csp.getVariables().stream()
                        .filter(var -> var.getName().equals(String.valueOf(currentRow + 1) + String.valueOf(currentCol + 1)))
                        .findFirst()
                        .orElse(null);

                boolean assignmentFromStart = false;

                if (currentVariable != null) {
                    value = (this.assignment != null) ? String.valueOf(this.assignment.getValue(currentVariable)) : "";
                    value = (!value.equals("null")) ? value : "";
                    domain = this.csp.getDomain(currentVariable).toString();

                    if (boldValues.contains(row * 9 + col))
                        assignmentFromStart = true;
                } else {
                    value = "test";
                    domain = "test";
                }

                TextFlow textArea = createTextFlow(domain, value, assignmentFromStart);
                gridPane.add(textArea, col, row);
            }
        }
    }

    public TextFlow createTextFlow(String domain, String value, boolean bold) {
        TextFlow textFlow = new TextFlow();

        Text domainText = new Text(domain + "\n");
        domainText.setFont(Font.font(10));
        domainText.setFill(Color.DARKSLATEGREY);

        Text valueText = new Text(value);
        if (bold) {
            valueText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        } else {
            valueText.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
            valueText.setFill(Color.DARKSLATEGREY);
        }

        textFlow.getChildren().addAll(domainText, valueText);
        textFlow.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        textFlow.setPadding(new Insets(10));
        textFlow.setTextAlignment(TextAlignment.CENTER);
        textFlow.setStyle("-fx-background-color: white;");
        return textFlow;
    }
}
