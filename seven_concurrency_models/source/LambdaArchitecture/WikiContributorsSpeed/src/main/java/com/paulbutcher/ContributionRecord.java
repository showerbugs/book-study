package com.paulbutcher;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.HashMap;
import java.util.HashSet;

// START:bolt
class ContributionRecord extends BaseBasicBolt {
  private static final HashMap<Integer, HashSet<Long>> timestamps = // <label id="code.recordertimestamps"/>
    new HashMap<Integer, HashSet<Long>>();

  public void declareOutputFields(OutputFieldsDeclarer declarer) { // <label id="code.recorderdeclare"/>
  }
  public void execute(Tuple tuple, BasicOutputCollector collector) { // <label id="code.recorderexecute"/>
    addTimestamp(tuple.getInteger(2), tuple.getLong(0));
  }

  private void addTimestamp(int contributorId, long timestamp) { // <label id="code.addtimestamp"/>
    HashSet<Long> contributorTimestamps = timestamps.get(contributorId);
    if (contributorTimestamps == null) {
      contributorTimestamps = new HashSet<Long>();
      timestamps.put(contributorId, contributorTimestamps);
    }
    contributorTimestamps.add(timestamp);
  }
}
// END:bolt
