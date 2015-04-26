package aima.gui.support.fx;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.GlyphsDude;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * @author Ciaran O'Reilly
 */
public class FXUtil {
    public static final int _buttonDefaultIconSize = 16;
    public static final int _labelDefaultIconSize  = 12;

    public static void anchor(Node node) {
        AnchorPane.setTopAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setBottomAnchor(node, 0.0);
    }

    public static void setDefaultButtonIcon(Button button, GlyphIcons icon) {
        GlyphsDude.setIcon(button, icon, iconSize(_buttonDefaultIconSize), ContentDisplay.GRAPHIC_ONLY);
        fixButtonSize(button, _buttonDefaultIconSize);
    }

    public static void setDefaultLabelIcon(Label label, GlyphIcons icon) {
        GlyphsDude.setIcon(label, icon, iconSize(_labelDefaultIconSize), ContentDisplay.GRAPHIC_ONLY);
    }

    //
    // PRIVATE
    //
    private static String iconSize(int size) {
        return ""+size+"px";
    }

    private static void fixButtonSize(Button button, int size) {
        button.setMaxSize(size + (8.7*2), size+(4.3*2));
        button.setPrefWidth(size + (8.7*2));
        button.setPrefHeight(size + (4.3 * 2));
        button.setPadding(Insets.EMPTY);
    }
}
