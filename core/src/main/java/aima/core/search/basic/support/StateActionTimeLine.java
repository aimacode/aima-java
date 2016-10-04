package aima.core.search.basic.support;

import java.time.Instant;
import java.util.AbstractMap.SimpleEntry;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * A list of events for creating a timeline of which actions what state took when. Used for
 * debugging purposes and could later maybe help visualizing stuff.
 *
 * @param <S> the states
 * @param <A> action attempted from this state
 * using a {@link SimpleEntry} as key to the executed actions, can be ordered later
 * @author wormi
 */

public class StateActionTimeLine<S, A> extends ConcurrentLinkedQueue<StateActionTimeLine.MyEntry> {

  /**
   * add to timeLine
   * @param state     from which state is this action attempted
   * @param action    action attempted on the state
   */
  public boolean add(S state, A action) {
    return add(new MyEntry(state, action, Instant.now().toEpochMilli(), Thread.currentThread().getId()));
  }

  public class MyEntry {
    public final S state;
    public final A action;
    public final long time;
    public final long processId;

    MyEntry(S state, A action, long time, long processId) {
      this.state = state;
      this.action = action;
      this.time = time;
      this.processId = processId;
    }
  }
}
