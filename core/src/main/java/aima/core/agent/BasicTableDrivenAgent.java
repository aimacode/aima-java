package aima.core.agent;

import aima.core.api.agent.Action;
import aima.core.api.agent.Percept;
import aima.core.api.agent.TableDrivenAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ciaran O'Reilly
 */
public class BasicTableDrivenAgent<P extends Percept> implements TableDrivenAgent<P> {
    private List<Percept>              percepts = new ArrayList<>();
    private Map<List<Percept>, Action> table    = new HashMap<>();

    public BasicTableDrivenAgent(Map<List<Percept>, Action> table) {
        this.table.putAll(table);
    }

    @Override
    public List<Percept> percepts() {
        return percepts;
    }

    @Override
    public Map<List<Percept>, Action> table() {
        return table;
    }
}
