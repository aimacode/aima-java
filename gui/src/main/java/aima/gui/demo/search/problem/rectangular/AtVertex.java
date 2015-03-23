package aima.gui.demo.search.problem.rectangular;

import java.util.StringJoiner;

/**
 * @author Ciaran O'Reilly
 */
public class AtVertex {
    public final int x;
    public final int y;

    public AtVertex(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return new StringJoiner(",", "At(", ")").add(""+x).add(""+y).toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || ! (obj instanceof AtVertex)) {
           return false;
        }
        AtVertex other = (AtVertex) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 37 * result + x;
        result = 43 * result + y;
        return result;
    }
}
