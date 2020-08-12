
package trabalhopraticomodulo3;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

//hadoop jar $mapreducejarjob trabalhopraticomodulo3.ExemploIGTI
public class ExemploIGTI extends Configured implements Tool {
	public static void main(final String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new ExemploIGTI(), args);
		System.exit(res);
	}

	public int run(final String[] args) throws Exception {
		try {
			JobConf conf = new JobConf(getConf(), ExemploIGTI.class);
			conf.setJobName("rabalho prático IGTI Módulo 3 - Cálculo Covid19");

			final FileSystem fs = FileSystem.get(conf);
			Path diretorioEntrada = new Path("/entrada"), diretorioSaida = new Path("/saida");

			/* Criar um diretorio de entrada no HDFS */
			if (!fs.exists(diretorioEntrada))
				fs.mkdirs(diretorioEntrada);

			/* Adicionar um arquivo para ser processado */
			fs.copyFromLocalFile(new Path("/home/prbpedro/Downloads/Dados/covidData.txt"), diretorioEntrada);

			/* Atribuindo os diretorios de Entrada e Saida para o Job */
			FileInputFormat.setInputPaths(conf, diretorioEntrada);
			FileOutputFormat.setOutputPath(conf, diretorioSaida);

			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueClass(Text.class);
			conf.setMapperClass(MapIGTI.class);
			conf.setReducerClass(ReduceIGTI.class);
			JobClient.runJob(conf);

		} catch (Exception e) {
			throw e;
		}
		return 0;
	}

	public static class MapIGTI extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

			Text txtChave = new Text();
			Text txtValor = new Text();

			String[] dadosCovid = value.toString().split(",");

			String dataEvento = dadosCovid[0];
			String paisEvento = dadosCovid[2];
			int novosCasos = Integer.parseInt(dadosCovid[4]);
			int novosObitos = Integer.parseInt(dadosCovid[6]);

			String strChave = dataEvento;
			String strValor = paisEvento + "|" + String.valueOf(novosCasos) + "|" + String.valueOf(novosObitos);

			txtChave.set(strChave);
			txtValor.set(strValor);

			output.collect(txtChave, txtValor);

		}
	}

	public static class ReduceIGTI extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

			int maiorCasos = 0, maiorObitos = 0;
			String paisCasos = "", paisObitos = "", strSaida = "";
			Text value = new Text();
			String[] campos = new String[3];

			while (values.hasNext()) {
				value = values.next();
				campos = value.toString().split("\\|");

				if (Integer.parseInt(campos[1]) > maiorCasos) {
					maiorCasos = Integer.parseInt(campos[1]);
					paisCasos = campos[0];
				}

				if (Integer.parseInt(campos[2]) > maiorObitos) {
					maiorObitos = Integer.parseInt(campos[2]);
					paisObitos = campos[0];
				}
			}

			strSaida = "Casos: " + String.valueOf(maiorCasos) + " em " + paisCasos + ".";
			strSaida += "Obitos: " + String.valueOf(maiorObitos) + " em " + paisObitos + ".";

			value.set(strSaida);
			output.collect(key, value);
		}

	}
}
