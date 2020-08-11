package mapreduce.wordcount;
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;

//Sysout location: http://localhost:9870/logs/userlogs/application_1597149227428_0004/container_1597149227428_0004_01_000003/stdout
public class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private Text word;
	
	@Override
	protected void setup(Mapper<Object, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		super.setup(context);
		 word = new Text();
	}
	
	@Override
	protected void cleanup(Mapper<Object, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
	}

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		StringTokenizer itr = new StringTokenizer(value.toString());
		
		Counter counter = context.getCounter(this.getClass().getName(), "TOTAL DE PALAVRAS_" + key);
		counter.setValue(0);
		
		while (itr.hasMoreTokens()) {
			word.set(itr.nextToken());
			context.write(word, one);
			counter.increment(1);
		}
		
		System.out.println(key + ": " + value);
	}
}