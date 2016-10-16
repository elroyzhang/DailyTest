package com.tencent.example.topology;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.TupleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by elroy on 16-9-21.
 */
public class TPFieldBolt extends BaseRichBolt {
  private static final long serialVersionUID = 1L;
  private static final Logger LOG =
      LoggerFactory.getLogger(TPFieldBolt.class);
  OutputCollector _collector;
  private final static int tickFreqSecs = 40;
  private int interval = 0;
  private boolean hasAck = true;


  public TPFieldBolt(int _interval3, boolean _hasAck) {
    this.interval = _interval3;
    this.hasAck = _hasAck;
  }

  @Override public void prepare(Map stormConf, TopologyContext context,
      OutputCollector collector) {
    _collector = collector;
  }

  public static void sleepNs(int ns) {
    try {
      Thread.sleep(0, ns);
    } catch (InterruptedException e) {

    }
  }

  @Override public void execute(Tuple input) {
    if(TupleUtils.isTick(input))
    {
      LOG.info("tick tuple every " + tickFreqSecs + " secs ...");
    }else {
      if(this.hasAck)
      {
        _collector.ack(input);
      }else {
        LOG.info("testTPField="+input.getInteger(0)+",name="+input.getMessageId()+",sourceComponent="+input.getSourceComponent()
        +",sourceStreamid="+input.getSourceStreamId()+",field="+input.getIntegerByField("number"));
        _collector.emit("STRAEM_ID_NUMBER", input, new Values(input.getInteger(0) + "!!!"));
        if(this.hasAck) {
          _collector.ack(input);
        }
        sleepNs(interval);
      }

      sleepNs(interval);
      LOG.debug("updater-bolt:" + input.getInteger(0));
    }
  }

  @Override public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declareStream("STRAEM_ID_NUMBER", new Fields("field"));
  }
}
