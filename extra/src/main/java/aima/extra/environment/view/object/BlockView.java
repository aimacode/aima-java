package aima.extra.environment.view.object;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by @AdrianBZG on 2/04/17.
 */

public class BlockView extends ObjectView {

    public BlockView() {
        Rectangle rectangle = new Rectangle(20,20);
        rectangle.setTranslateX(-20);
        rectangle.setTranslateY(-20);
        rectangle.setFill(Color.BLACK);
        getChildren().addAll(rectangle);
    }
}
