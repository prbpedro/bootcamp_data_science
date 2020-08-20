package hive.hcatalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hive.common.type.HiveVarchar;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hive.hcatalog.data.DefaultHCatRecord;
import org.apache.hive.hcatalog.data.HCatRecord;
import org.apache.hive.hcatalog.data.schema.HCatFieldSchema;
import org.apache.hive.hcatalog.data.schema.HCatSchema;
import org.apache.hive.hcatalog.mapreduce.HCatBaseInputFormat;
import org.apache.hive.hcatalog.mapreduce.HCatInputFormat;
import org.apache.hive.hcatalog.mapreduce.HCatOutputFormat;
import org.apache.hive.hcatalog.mapreduce.OutputJobInfo;

//hadoop jar $mapreducejarjob hive.hcatalog.HCatalogMapReduceHiveExemple
public class HCatalogMapReduceHiveExemple extends Configured implements Tool {
	public static void main(final String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new HCatalogMapReduceHiveExemple(), args);
		System.exit(res);
	}

	public int run(final String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Map Reduce HCatalog Exemple");
		job.setJarByClass(HCatalogMapReduceHiveExemple.class);
		job.setMapperClass(MapHCatalog.class);
		job.setReducerClass(ReduceHCatalog.class);

		HCatInputFormat.setInput(job, "dbdesafio", "dadoscovid");
		job.setInputFormatClass(HCatInputFormat.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		HCatOutputFormat.setOutput(job, OutputJobInfo.create("dbdesafio", "paises", null));
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(DefaultHCatRecord.class);
		job.setOutputFormatClass(HCatOutputFormat.class);

		HCatSchema s = HCatOutputFormat.getTableSchema(job.getConfiguration());
		HCatOutputFormat.setSchema(job, s);

		return (job.waitForCompletion(true) ? 0 : 1);
	}

	public static class MapHCatalog extends Mapper<WritableComparable, HCatRecord, Text, Text> {

		public static int c = 0;

		@Override
		protected void map(WritableComparable key, HCatRecord value,
				Mapper<WritableComparable, HCatRecord, Text, Text>.Context context)
				throws IOException, InterruptedException {
			HCatSchema schema = HCatBaseInputFormat.getTableSchema(context.getConfiguration());

			Text txtChave = new Text();
			Text txtValor = new Text();
			txtChave.set(key.toString());
			txtValor.set(value.getString("siglapais", schema));

			if (c == 0) {
				System.out.println(value.getString("siglapais", schema));
				System.out.println(value.getString("siglapais", schema));
			}

			c++;

			context.write(txtChave, txtValor);
		}
	}

	public static class ReduceHCatalog extends Reducer<Text, Iterable<Text>, Object, HCatRecord> {

		@Override
		protected void reduce(Text key, Iterable<Iterable<Text>> value,
				Reducer<Text, Iterable<Text>, Object, HCatRecord>.Context context)
				throws IOException, InterruptedException {

			List<HCatFieldSchema> columns = new ArrayList<HCatFieldSchema>(3);
			columns.add(new HCatFieldSchema("cd", HCatFieldSchema.Type.VARCHAR, ""));
			HCatSchema schema = new HCatSchema(columns);
			HCatRecord record = new DefaultHCatRecord(1);

			record.setVarchar("cd", schema, new HiveVarchar(key.toString(), key.toString().length()));
			context.write(null, record);
		}
	}
}
