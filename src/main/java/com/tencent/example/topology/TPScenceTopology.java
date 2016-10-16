package com.tencent.example.topology;

import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.BoltDeclarer;
import org.apache.storm.topology.SpoutDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * Created by elroy on 16-9-20.
 *
 * @author <a href="mailto:elroyzhang@gmail.com">elroyzhang</a>
 * @usage test data is changed? when data is transferred
 */
public class TPScenceTopology {
  /**
   * @throws Exception
   * @param(1),param(2),param(3)
   */
  @Option(name = "--help", aliases = { "-h" }, usage = "print help message")
  private boolean _help = false;

  @Option(name = "--limit", aliases = {
      "--limit" }, metaVar = "NAME", usage = "default limit is Long.MAX_VALUE")
  private Long _limit = Long.MAX_VALUE;

  @Option(name = "--name", aliases = {
      "--topologyName" }, metaVar = "NAME", usage = "name of the topology")
  private String _name = "test";

  @Option(name = "--interval1", aliases = {
      "--i1" }, usage = "interval for word default is 0 ns")
  private int _interval1 = 0;

  @Option(name = "--has-ack", aliases = {
      "--ack" }, usage = "has ack default is 1 means true, 0 is false")
  private int _hasAck = 1;

  @Option(name = "--para1", aliases = {
      "--p1" }, usage = "paramllism for word default is 1")
  private int _para1 = 1;

  @Option(name = "--para2", aliases = {
      "--p2" }, usage = "paramllism for exclaim default is 1")
  private int _para2 = 1;

  @Option(name = "--task1", aliases = {
      "--t1" }, usage = "tasknum for word default is 1")
  private int _task1 = -1;

  @Option(name = "--task2", aliases = {
      "--t2" }, usage = "tasknum for exclaim default is 1")
  private int _task2 = -1;

  @Option(name = "--interval2", aliases = {
      "--i2" }, usage = "interval for exclaim default is 0 ns")
  private int _interval2 = 0;

  @Option(name = "--update", aliases = {
      "--u" }, usage = "update default is 0 is false, 1 means true")
  private int _update = 0;

  @Option(name = "--interval3", aliases = {
      "--i3" }, usage = "interval for update default is 0 ns")
  private int _interval3 = 0;

  @Option(name = "--para3", aliases = {
      "--p3" }, usage = "paramllism for update default is 1")
  private int _para3 = 1;

  public static void main(String[] args) throws Exception {
    new TPScenceTopology().realMain(args);
  }

  public void realMain(String[] args)
      throws InvalidTopologyException, AuthorizationException,
      AlreadyAliveException {
    CmdLineParser parser = new CmdLineParser(this);
    parser.setUsageWidth(80);
    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      System.err.println(e.getMessage());
      _help = true;
    }
    if (_help) {
      parser.printUsage(System.err);
      System.err.println();
      return;
    }
    if (_name == null || _name.isEmpty()) {
      throw new IllegalArgumentException("privide topology name please");
    }

    TopologyBuilder builder = new TopologyBuilder();
    Config conf = new Config();

    SpoutDeclarer sd1 = builder.setSpout("number",
        new TPTestWordSpout(_limit, _interval1, _hasAck == 1), _para1);
    if (_task1 != -1) {
      sd1.setNumTasks(_task1);
    }
    BoltDeclarer bd1 = builder.setBolt("field",
        new TPFieldBolt(_interval2, _hasAck == 1), _para2)
        .shuffleGrouping("number", "STRAEM_ID_NUMBER");
    if (_task2 != -1) {
      bd1.setNumTasks(_task2);
    }
    BoltDeclarer bd2 = builder.setBolt("combine",
        new TPCombinerBolt(_interval3, _hasAck == 1, _update == 1), _para3)
        .shuffleGrouping("field", "STRAEM_ID_NUMBER");
    if (_task2 != -1) {
      bd2.setNumTasks(_task2);
    }
    StormSubmitter.submitTopology(_name, conf, builder.createTopology());
  }
}
