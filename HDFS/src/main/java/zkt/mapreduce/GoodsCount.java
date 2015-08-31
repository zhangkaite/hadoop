package zkt.mapreduce;

/**
 * nohup hadoop jar /data/tmp/NumCount.jar cn.centaur.top5.GoodsCount /wfpuser_a0408 /result/top5/output-goods &
 * 
 */
import java.io.IOException;
import java.util.Iterator;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class GoodsCount extends Configured implements Tool {
	//mapper
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

		static enum Counters {
			INPUT_WORDS
		}

		//
		private String inputFile;
		public void configure(JobConf job) {
			inputFile = job.get("map.input.file");
		}

		//获取读取的数据
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			//读取一行的数据
			String line = value.toString();
			//对数据进行分割
			String[] columns = line.split("\\|");
			if (columns.length > 40) {
				try {
					Integer.parseInt(columns[0]);
					String ac0011 = columns[9];
					String AC2037 = columns[39];
					int edbStart = inputFile.indexOf("edb_");
					String edbName = inputFile.substring(edbStart, inputFile.indexOf("/", edbStart));
					Text key_ = new Text(edbName + "," + ac0011 + "," + AC2037);
					IntWritable one = new IntWritable(1);
					output.collect(key_, one);

				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> {
		public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			int sum = 0;
			while (values.hasNext()) {
				int temp = values.next().get();
				sum += temp;
			}
			output.collect(key, new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new GoodsCount(), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		JobConf conf = new JobConf(getConf(), GoodsCount.class);
		conf.setJobName("GoodsCount");
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		// 读取hadoop的otherArgs[0]目录，取出所有以edb_开头的目录作为本mapreduce程序的输入目录
		String inputPath = "";
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(args[0]);
		FileStatus[] fileStatus = fs.listStatus(path);
		for (FileStatus status : fileStatus) {
			String hadoopPathStr = status.getPath().toString();
			System.out.println(hadoopPathStr);
			if (hadoopPathStr != null && hadoopPathStr.contains("edb_")) {
				inputPath += hadoopPathStr.replace("hdfs://db2:9000", "") + ",";
			}
		}
		fs.close();
		inputPath = inputPath.substring(0, inputPath.length() - 1);
		FileInputFormat.addInputPaths(conf, inputPath);
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		JobClient.runJob(conf);
		return 0;
	}
}
