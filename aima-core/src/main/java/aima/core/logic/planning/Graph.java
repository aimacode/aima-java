package aima.core.logic.planning;

import java.util.ArrayList;

public class Graph {
    ArrayList<Level> levels;
    Problem problem;

    public Graph(Problem problem, Level initialLevel) {
        this.problem = problem;
        levels = new ArrayList<>();
        levels.add(initialLevel);
    }

    public int numLevels() {
        return levels.size();
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public Problem getProblem() {
        return problem;
    }

    public Graph addLevel() {
        Level lastLevel = levels.get(levels.size() - 1);
        Level level = new Level(lastLevel, this.problem);
        this.levels.add(level);
        return this;
    }
}
