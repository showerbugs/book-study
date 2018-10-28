package com.paulbutcher;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

// START:main
public class WikiContributorsTopology {

  public static void main(String[] args) throws Exception {

    TopologyBuilder builder = new TopologyBuilder(); // <label id="code.topologybuilder"/>

    builder.setSpout("contribution_spout", new RandomContributorSpout(), 4); // <label id="code.setspout"/>

    builder.setBolt("contribution_parser", new ContributionParser(), 4). // <label id="code.shufflegrouping"/>
      shuffleGrouping("contribution_spout");

    builder.setBolt("contribution_recorder", new ContributionRecord(), 4). // <label id="code.fieldsgrouping"/>
      fieldsGrouping("contribution_parser", new Fields("contributorId"));

    LocalCluster cluster = new LocalCluster();
    Config conf = new Config();
    cluster.submitTopology("wiki-contributors", conf, builder.createTopology()); // <label id="code.submittopology"/>

    Thread.sleep(10000);

    cluster.shutdown(); // <label id="code.clustershutdown"/>
  }
}
// END:main
