package aima.core.search.basic.support;

import java.time.Instant;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Some logging, used for debugging purposes. Todo: one could define some general
 * search actions (like adding and removing to frontiers) and states (explored states and next in
 * frontier), for now a message seems enough
 *
 * @author wormi
 */

public class StateActionTimeLine extends ConcurrentLinkedQueue<StateActionTimeLine.MyEntry> {

  public boolean add(String message) {
    return add(new MyEntry(message, Instant.now().toEpochMilli(), Thread.currentThread().getId()));
  }

  public class MyEntry {
    public final String message;
    public final long time;
    public final long processId;

    MyEntry(String message, long time, long processId) {
      this.message = message;
      this.time = time;
      this.processId = processId;
    }
  }
}
