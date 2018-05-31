package aima.extra.environment.model.map.object;

/**
 * Created by @AdrianBZG on 2/04/17.
 */

import aima.extra.environment.view.object.BlockView;
import aima.extra.environment.view.object.ObjectView;

/**
 * Represents a no passing tile (a wall or obstacle)
 */
public class Block extends MapObject {

    @Override
    public TypeObject getType() {
        return TypeObject.Obstacle;
    }

    @Override
    public ObjectView getVisualObject() {
        return new BlockView();
    }

    @Override
    public String getName() {
        return "Block";
    }

    @Override
    public boolean hasOptions() {
        return false;
    }
}
