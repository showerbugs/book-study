package com.paulbutcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class WikipediaContributors extends Configured implements Tool {

// START:mapper
  public static class Map extends Mapper<Object, Text, IntWritable, LongWritable> {

    public void map(Object key, Text value, Context context)
      throws IOException, InterruptedException {

      Contribution contribution = new Contribution(value.toString());
      context.write(new IntWritable(contribution.contributorId), 
        new LongWritable(contribution.timestamp));
    }
  }
// END:mapper

// START:reducer
  public static class Reduce 
    extends Reducer<IntWritable, LongWritable, IntWritable, Text> {
    static DateTimeFormatter dayFormat = ISODateTimeFormat.yearMonthDay();
    static DateTimeFormatter monthFormat = ISODateTimeFormat.yearMonth();

    public void reduce(IntWritable key, Iterable<LongWritable> values,
                       Context context) throws IOException, InterruptedException {
      HashMap<DateTime, Integer> days = new HashMap<DateTime, Integer>(); // <label id="code.days"/> 
      HashMap<DateTime, Integer> months = new HashMap<DateTime, Integer>(); // <label id="code.months"/> 
      for (LongWritable value: values) {
        DateTime timestamp = new DateTime(value.get());
        DateTime day = timestamp.withTimeAtStartOfDay(); // <label id="code.day"/> 
        DateTime month = day.withDayOfMonth(1); // <label id="code.month"/> 
        incrementCount(days, day);
        incrementCount(months, month);
      }
      for (Entry<DateTime, Integer> entry: days.entrySet()) // <label id="code.outputcountsstart"/> 
        context.write(key, formatEntry(entry, dayFormat));
      for (Entry<DateTime, Integer> entry: months.entrySet())
        context.write(key, formatEntry(entry, monthFormat)); // <label id="code.outputcountsend"/> 
    }
// END:reducer

// START:incrementcount
    private void incrementCount(HashMap<DateTime, Integer> counts, DateTime key) {
      Integer currentCount = counts.get(key);
      if (currentCount == null)
        counts.put(key, 1);
      else
        counts.put(key, currentCount + 1);
    }
// END:incrementcount

// START:formatentry
    private Text formatEntry(Entry<DateTime, Integer> entry, 
                             DateTimeFormatter formatter) {
      return new Text(formatter.print(entry.getKey()) + "\t" + entry.getValue())
    }
// END:formatentry
// START:reducer
  }
// END:reducer

  public int run(String[] args) throws Exception {
    Configuration conf = getConf();

    Job job = Job.getInstance(conf, "wikicontributors");
    job.setJarByClass(WikipediaContributors.class);
    job.setMapperClass(Map.class);
    job.setReducerClass(Reduce.class);
    job.setMapOutputValueClass(LongWritable.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    boolean success = job.waitForCompletion(true);
    return success ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(), new WikipediaContributors(), args);
    System.exit(res);
  }
}
