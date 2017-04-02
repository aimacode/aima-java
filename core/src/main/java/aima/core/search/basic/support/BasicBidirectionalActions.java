package aima.core.search.basic.support;

import aima.core.search.api.BidirectionalActions;
import aima.core.search.api.SearchController;

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
    private List<A> fromMeetingStateToGoalStateList = new ArrayList<>();
    private List<A> fromMeetingStateToInitialStateList = new ArrayList<>();

    public BasicBidirectionalActions(List<A> fromInitialStatePartList, List<A> fromGoalStatePartList,List<A> fromMeetingStateToInitialStateList,List<A> fromMeetingStateToGoalStateList) {
        this.fromInitialStatePartList = fromInitialStatePartList;
        this.fromGoalStatePartList = fromGoalStatePartList;
        this.fromMeetingStateToGoalStateList = fromMeetingStateToGoalStateList;
        this.fromMeetingStateToInitialStateList= fromMeetingStateToInitialStateList;
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
        init();
        fromInitialStateToGoalStateList.addAll(fromInitialStatePart());
        fromInitialStateToGoalStateList.addAll(fromMeetingStateToGoalStateList);
        return fromInitialStateToGoalStateList;
    }

    @Override
    public List<A> fromGoalStateToInitialState() {
        init();
        fromGoalStateToInitialStateList.addAll(fromGoalStatePart());
        fromGoalStateToInitialStateList.addAll(fromMeetingStateToInitialStateList);
        return fromGoalStateToInitialStateList;
    }

}
