package com.tencent.example.topology;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.utils.TupleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by elroy on 16-9-21.
 */
public class TPCombinerBolt extends BaseRichBolt{
  private static final long serialVersionUID = 1L;
  private static final Logger LOG =
      LoggerFactory.getLogger(TPCombinerBolt.class);
  OutputCollector _collector;
  private final static int tickFreqSecs = 20;
  private Long combineResult = 0l;
  private int interval = 0;
  private boolean hasAck = true;
  private boolean update = false;
  Timer timer = null;

  protected class TimerSchedule extends TimerTask
  {
    public void run()
    {
      //todo:input file

      File resultWrite = new File(System.getenv("STORM_HOME")+File.separator+"combineResult");
      try {
        resultWrite.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(resultWrite));
        out.write(combineResult.toString());
        out.flush();
        out.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public TPCombinerBolt(int _interval, boolean _hasAck, boolean _update)
  {
    interval = _interval;
    hasAck = _hasAck;
    update = _update;
  }

  @Override public void prepare(Map stormConf, TopologyContext context,
      OutputCollector collector) {
    _collector = collector;
    timer = new Timer();
    timer.schedule(new TimerSchedule(), 60 * 1000);
  }

  @Override public void execute(Tuple input) {
    if (TupleUtils.isTick(input))
    {
      LOG.info("tick tuple every" + tickFreqSecs + " secs ...");
    }else {
      LOG.info("combineResult="+combineResult+",integer="+input.getIntegerByField("field"));
      combineResult += Long.valueOf(input.getIntegerByField("field"));
    }
  }

  @Override public void declareOutputFields(OutputFieldsDeclarer declarer) {
    declarer.declareStream("STRAEM_ID_NUMBER", new Fields("number"));
  }
}