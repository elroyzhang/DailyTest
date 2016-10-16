package com.tencent.example.topology;

import com.tencent.example.bp.BPTestWordSpout;
import com.tencent.storm.topology.UpdateRichSpout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by elroy on 16-9-21.
 */
public class TPTestWordSpout extends UpdateRichSpout {

  private static final long serialVersionUID = 1;
  private static final Logger LOG =
      LoggerFactory.getLogger(BPTestWordSpout.class);

  boolean _isDistributed;
  SpoutOutputCollector _collector;
  long limit = 50000;
  int interval = 0;
  private boolean hasAck = true;
  private AtomicLong cnt = new AtomicLong(0);

  public TPTestWordSpout(long limit, int interval, boolean _hasAck) {
    this(true);

    this.limit = limit;
    this.interval = interval;
    this.hasAck = _hasAck;
    LOG.info("Limit is " + limit + ", interval is " + interval);
  }

  public TPTestWordSpout() {
    this(true);
  }

  public TPTestWordSpout(boolean isDistributed) {
    _isDistributed = isDistributed;
  }

  @Override public void open(Map conf, TopologyContext context,
      SpoutOutputCollector collector) {
    _collector = collector;
  }

  @Override public void nextTuple() {
    final Integer number = getDbInteger();
    if(number != null) {
      if(this.hasAck)
      {
        _collector.emit("STRAEM_ID_NUMBER", new Values(number), number);
      }else {
        _collector.emit("STRAEM_ID_NUMBER", new Values(number));
        LOG.info("number="+number);
      }
      sleepNs(interval);
    }else
    {
      sleepMics(30 * 1000);
    }
  }

  public static void sleepMics(int micSecs) {
    try {
      TimeUnit.MICROSECONDS.sleep(micSecs);
    } catch (InterruptedException e) {
    }
  }


  @Override public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declareStream("STRAEM_ID_NUMBER", new Fields("number"));
  }

  public static void sleepNs(int ns) {
    try {
      Thread.sleep(0, ns);
    } catch (InterruptedException e) {

    }
  }

  public Integer getDbInteger()
  {
    long row_num = cnt.incrementAndGet();
    if(row_num < limit)
    {
      final Integer[] integers = new Integer[] {
          1, -1, 2, -2, 3, -3, 4, -4, 5, -5
      };
      final Integer index = Math.toIntExact(row_num % 10);
      return integers[index];
    }
    return null;

  }
}
