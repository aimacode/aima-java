package aima.extra.environment.model.map;

/**
 * Created by @AdrianBZG on 2/04/17.
 *
 * Auxiliary class used to access to different sectors into a EnvironmentMap
 *
 */
public class MapSector {
    // X coordinate
    private int x;

    // Y Coordinate
    private int y;

    private MapSector(int x, int y) {
        this.x = x; this.y = y;
    }

    /**
     * Make a sector position to find into map.
     * @param x
     * @param y
     * @return
     */
    public static MapSector pos(int x, int y) {
        return new MapSector(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapSector mapSector = (MapSector) o;

        if (x != mapSector.x) return false;
        return y == mapSector.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}