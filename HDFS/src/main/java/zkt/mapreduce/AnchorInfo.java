package zkt.mapreduce;

/**
 * nohup hadoop jar /data/tmp/NumCount.jar cn.centaur.top5.GoodsCount /wfpuser_a0408 /result/top5/output-goods &
 * 
 */
import java.io.IOException;
import java.math.BigDecimal;
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
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/***
 * 
 * @author Administrator
 * 1、读取hdfs上的文件，对读取的文件进行map操作
 * 2、对map操作的数据进行reduce操作
 *
 */

public class AnchorInfo extends Configured implements Tool {
	// mapper
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
		// 获取读取的数据
		public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
			// 读取一行的数据
			String line = value.toString();
			// 对数据进行分割
			String[] columns = line.split("\\s+");
			// 获取每列的数据，主播ID用户id(1),统计主播获得的鲜花数（3）、主播获得的T豆数（4）、贡献给公会T豆数（5）、爵位佣金（8）、兑换T券（9）
			String user_id = columns[1];
			String flower = columns[3];
			String anchorTbean = columns[4];
			String unionTbean = columns[5];
			String money = columns[8];
			String changeTq = columns[9];
			// 对数据进行处理
			Text key_ = new Text(user_id);
			String result = flower + " " + anchorTbean + " " + unionTbean + " " + money + " " + changeTq;
			Text bw = new Text(result);
			output.collect(key_, bw);
		}
	}

	public static class Reduce extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
		public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
		//	int flower = 0;
			BigDecimal f=new BigDecimal(0);
			float anchorTbean = 0.0f;
			float unionTbean = 0.0f;
			int money = 0;
			int changeTq = 0;
			while (values.hasNext()) {
				String result = values.next().toString();
				//if (result.contains(";")) {
					String[] columns = result.split("\\s+");
					//flower += Integer.parseInt(columns[0]);
					f.add(new BigDecimal(columns[0]));
					anchorTbean += Float.parseFloat(columns[1].equals("0")?"0.00":columns[1]);
					unionTbean += Float.parseFloat(columns[2].equals("0")?"0.00":columns[1]);
					money += Integer.parseInt(columns[3]);
					changeTq += Integer.parseInt(columns[4]);
				//}
				
			}
			Text bw = new Text(f.toString() + "\t" + anchorTbean + "\t" + unionTbean + "\t" + money + "\t" + changeTq);
			output.collect(key, bw);
		}
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new AnchorInfo(), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {
		JobConf conf = new JobConf(getConf(), AnchorInfo.class);
		conf.setJobName("AnchorInfo");
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		// 读取hadoop的otherArgs[0]目录，取出所有以edb_开头的目录作为本mapreduce程序的输入目录
		String inputPath = "";
		FileSystem fs = FileSystem.get(conf);
		Path path = new Path(args[0]);
		inputPath = path.toString();
		fs.close();
		FileInputFormat.addInputPaths(conf, inputPath);
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		JobClient.runJob(conf);
		return 0;
	}
}
