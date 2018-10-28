package com.paulbutcher;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import java.util.Map;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

// START:spout
public class RandomContributorSpout extends BaseRichSpout { // <label id="code.spout"/>

  private static final Random rand = new Random();
  private static final DateTimeFormatter isoFormat = 
    ISODateTimeFormat.dateTimeNoMillis();

  private SpoutOutputCollector collector;
  private int contributionId = 10000;

  public void open(Map conf, TopologyContext context, // <label id="code.spoutopen"/>
    SpoutOutputCollector collector) {

    this.collector = collector;
  }

  public void declareOutputFields(OutputFieldsDeclarer declarer) { // <label id="code.spoutdeclare"/>
    declarer.declare(new Fields("line"));
  }

  public void nextTuple() { // <label id="code.spoutnexttuple"/>
    Utils.sleep(rand.nextInt(100));
    ++contributionId;
    String line = isoFormat.print(DateTime.now()) + " " + contributionId + " " + 
      rand.nextInt(10000) + " " + "dummyusername";
    collector.emit(new Values(line));
  }
}
// END:spout
