package aima.extra.environment.model.map.object;

/**
 * Created by @AdrianBZG on 2/04/17.
 */

import aima.core.util.datastructure.Tuple;
import aima.extra.environment.view.object.ObjectView;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class MapObject implements Cloneable {

    /**
     * Internal use to have a list of all object available
     */
    private static ArrayList<MapObject> mapObjects = new ArrayList<>();

    private Tuple<Integer, Integer> objectPosition = new Tuple<>(0, 0);


    static {
        // No objects at the moment so the Array is empty
        mapObjects = new ArrayList<>(Arrays.asList());
    }

    /**
     * Basic representation of what is this object
     * @return enum
     */
    public abstract TypeObject getType();

    /**
     * Representation of this object in 2D dimension
     * @return
     */
    public abstract ObjectView getVisualObject();

    /**
     * Name of this object
     * @return
     */
    public abstract String getName();

    /**
     * Is it possible to configure this object?
     * @return boolean to question
     */
    public abstract boolean hasOptions();

    /**
     * Each object define how is itself configured or not
     */
    public void showOptions() {};

    /**
     * Retrieve list of objects available to use
     */
    public static ArrayList<MapObject> getMapObjects() {
        return mapObjects;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Tuple<Integer, Integer> getPosition() {
        return objectPosition;
    }

    public void setPosition(Tuple<Integer, Integer> objectPosition) {

        this.objectPosition = objectPosition;
    }

    public void setPosition (int x, int y) {

        objectPosition = new Tuple<>(x, y);
    }
}
