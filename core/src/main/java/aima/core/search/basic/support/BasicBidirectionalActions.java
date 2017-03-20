package aima.core.search.basic.support;

import aima.core.search.api.BidirectionalActions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author manthan.
 */
public class BasicBidirectionalActions<A> implements BidirectionalActions<A> {
    private List<A> fromInitialStatePartList = new ArrayList<>();
    private List<A> fromGoalStatePartList = new ArrayList<>();
    private List<A> fromInitialStateToGoalStateList = new ArrayList<>();
    private List<A> fromGoalStateToInitialStateList = new ArrayList<>();

    public BasicBidirectionalActions(List<A> fromInitialStatePartList, List<A> fromGoalStatePartList) {
        this.fromInitialStatePartList = fromInitialStatePartList;
        this.fromGoalStatePartList = fromGoalStatePartList;
    }

    private void init() {
        this.fromInitialStateToGoalStateList = new ArrayList<>();
        this.fromGoalStateToInitialStateList = new ArrayList<>();
    }

    @Override
    public List<A> fromInitialStatePart() {
        return fromInitialStatePartList;
    }

    @Override
    public List<A> fromGoalStatePart() {
        return fromGoalStatePartList;
    }

    @Override
    public List<A> fromInitialStateToGoalState() {
        List<A> reversedFromGoalStatePart = new ArrayList<>(fromGoalStatePart());
        Collections.reverse(reversedFromGoalStatePart);
        init();
        fromInitialStateToGoalStateList.addAll(fromInitialStatePart());
        fromInitialStateToGoalStateList.addAll(reversedFromGoalStatePart);
        return fromInitialStateToGoalStateList;
    }

    @Override
    public List<A> fromGoalStateToInitialState() {
        List<A> reversedFromInitialStatePart = new ArrayList<>(fromInitialStatePart());
        Collections.reverse(reversedFromInitialStatePart);
        init();
        fromGoalStateToInitialStateList.addAll(fromGoalStatePart());
        fromGoalStateToInitialStateList.addAll(reversedFromInitialStatePart);
        return fromGoalStateToInitialStateList;
    }

}
