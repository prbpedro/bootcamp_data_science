package mapred.wordcount;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

//hadoop jar $mapreducejarjob mapred.wordcount.WordCountJob /input/wordcount /output/wordcount
public class WordCountJob extends Configured implements Tool{

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new WordCountJob(), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		JobConf jobConf = new JobConf(getConf(), WordCountJob.class);
		jobConf.setJobName("WordCountJob");
		
		Configuration conf = new Configuration();
	    Job job = Job.getInstance(conf, "word count");
	    job.setJarByClass(WordCountJob.class);
	    jobConf.setMapperClass(TokenizerMapper.class);
	    jobConf.setCombinerClass(IntSumReducer.class);
	    jobConf.setReducerClass(IntSumReducer.class);
	    jobConf.setOutputKeyClass(Text.class);
	    jobConf.setOutputValueClass(IntWritable.class);
	    FileInputFormat.setInputPaths(jobConf, args[0]);
	    FileOutputFormat.setOutputPath(jobConf, new Path(args[1]));
	    RunningJob rjob = JobClient.runJob(jobConf);
	    rjob.waitForCompletion();
	    return rjob.getJobState() == 2 ? 0 : 1 ;
	}
}
