package com.paulbutcher;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

// START:bolt
class ContributionParser extends BaseBasicBolt { // <label id="code.bolt"/>
  public void declareOutputFields(OutputFieldsDeclarer declarer) { // <label id="code.parserdeclare"/>
    declarer.declare(new Fields("timestamp", "id", "contributorId", "username"));
  }
  public void execute(Tuple tuple, BasicOutputCollector collector) { // <label id="code.parserexecute"/>
    Contribution contribution = new Contribution(tuple.getString(0));
    collector.emit(new Values(contribution.timestamp, contribution.id, 
      contribution.contributorId, contribution.username));
  }
}
// END:bolt
