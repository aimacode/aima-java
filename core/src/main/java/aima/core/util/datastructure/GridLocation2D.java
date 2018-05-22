package aima.core.util.datastructure;

/**
 * Note: If looking at a rectangle - the coordinate (x=0, y=0) will be the top
 * left hand corner. This corresponds with Java's AWT coordinate system.
 *
 * @author samagra
 */

public class GridLocation2D {

    private int x;
    private int y;

    public GridLocation2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public GridLocation2D up(){
        return new GridLocation2D(x,y-1);
    }

    public GridLocation2D down(){
        return new GridLocation2D(x,y+1);
    }

    public GridLocation2D left(){
        return new GridLocation2D(x-1,y);
    }

    public GridLocation2D right(){
        return new GridLocation2D(x+1,y);
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
