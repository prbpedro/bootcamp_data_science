package hdfs;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class HdfsManager  extends Configured implements Tool{

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new HdfsManager(), args);
		System.exit(res);
	}
	
	//hadoop jar $mapreducejarjob hdfs.HdfsManager read /output/hdsfmanager/demo.txt ~/Desktop
	private void read(String pathInput, String pathOutput) throws IOException {
		Path inputPath = new Path(pathInput);
        Configuration conf = getConf();
        FileSystem fs = FileSystem.get(conf);
        InputStream is = fs.open(inputPath);
        OutputStream os = new BufferedOutputStream(new FileOutputStream(pathOutput + "/outputfile.txt"));
        IOUtils.copyBytes(is, os, conf);
	}
	
	//hadoop jar $mapreducejarjob hdfs.HdfsManager createDemoFile /output/hdsfmanager
	private void createDemoFile(String pathHdsf) throws IOException {
        Configuration conf = getConf();
        FileSystem fs = FileSystem.get(conf);
        Path writePath = new Path(pathHdsf);
        if (fs.exists(writePath)) {
        	fs.delete(writePath, true);
        }

        Path filePath = new Path(pathHdsf + "/demo.txt");
        FSDataOutputStream stream = fs.create(filePath);
        
        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        final Calendar cal = Calendar.getInstance();
        
        stream.write((df.format(cal.getTime()) + "\n").getBytes());
        stream.flush();
        stream.close();
        fs.close();
	}

	public int run(String[] args) throws Exception {
		
		if(args[0].equals("read")) {
			read(args[1], args[2]);
		}else if (args[0].equals("createDemoFile")){
			createDemoFile(args[1]);
		}
		
		return 0;
	}
}
