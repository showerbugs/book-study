package com.paulbutcher;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

// START:runner
public class WordCount extends Configured implements Tool {

// END:runner
// START:mapper
  public static class Map extends Mapper<Object, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
	
    public void map(Object key, Text value, Context context)
      throws IOException, InterruptedException {

      String line = value.toString(); // <label id="code.map.tostring"/>
      Iterable<String> words = new Words(line); // <label id="code.map.words"/>
      for (String word: words)
        context.write(new Text(word), one); // <label id="code.map.write"/>
    }
  }
// END:mapper

// START:reducer
  public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
      throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val: values)
        sum += val.get();
      context.write(key, new IntWritable(sum));
    }
  }
// END:reducer

// START:runner
  public int run(String[] args) throws Exception {
    Configuration conf = getConf();
    Job job = Job.getInstance(conf, "wordcount");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(Map.class); // <label id="code.mapperclass"/>
    job.setReducerClass(Reduce.class); // <label id="code.reducerclass"/>
    job.setOutputKeyClass(Text.class); // <label id="code.keyclass"/>
    job.setOutputValueClass(IntWritable.class); // <label id="code.valueclass"/>
    FileInputFormat.addInputPath(job, new Path(args[0])); // <label id="code.inputpath"/>
    FileOutputFormat.setOutputPath(job, new Path(args[1])); // <label id="code.outputpath"/>
    boolean success = job.waitForCompletion(true); // <label id="code.waitforcompletion"/>
    return success ? 0 : 1;
  }

  public static void main(String[] args) throws Exception {
    int res = ToolRunner.run(new Configuration(), new WordCount(), args);
    System.exit(res);
  }
// START:runner
}
// END:runner
