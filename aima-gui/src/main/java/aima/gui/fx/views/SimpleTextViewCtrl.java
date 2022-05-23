package aima.gui.fx.views;

import aima.core.agent.Notifier;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;


public class SimpleTextViewCtrl implements Notifier {

    private final TextArea textArea;

    public SimpleTextViewCtrl(Pane pane) {
        textArea = new TextArea();
        //textArea.setFont(Font.font("monospaced", 16));
        textArea.setFont(Font.font(16));
        pane.getChildren().add(textArea);
    }

    @Override
    public void notify(String msg) {
        if (Platform.isFxApplicationThread())
            textArea.appendText("\n" + msg);
        else
            Platform.runLater(() -> textArea.appendText("\n" + msg));
    }

    public void clear() {
        textArea.clear();
    }
}
