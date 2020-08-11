package mapred.wordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapreduce.Counter;

//Sysout location: http://localhost:9870/logs/userlogs/application_1597149227428_0004/container_1597149227428_0004_01_000003/stdout
public class TokenizerMapper extends MapReduceBase
		implements Mapper<Object, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
	
	@Override
	public void configure(JobConf job) {
		super.configure(job);
		
		System.out.println(job.get("CustomConfig"));
	}

	public void map(Object key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		StringTokenizer itr = new StringTokenizer(value.toString());
		
		Counter counter = reporter.getCounter(this.getClass().getName(), "TOTAL DE PALAVRAS_" + key);
		counter.setValue(0);

		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			output.collect(word, one);
			counter.increment(1);
		}
	}
}