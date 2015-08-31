package zkt.mapreduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
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
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;

import zkt.entity.UserEntity;
import zkt.util.JsonUtil;

/**
 * 获取每天的注册数据，思路通过调度在每天的23：59：59执行RegisterCount。 因为注册数据很少，所以所有的注册数据都存放在指定的目录下/hadoop/mq/addUser.txt中，
 * 每次都去指定的位置获取当天注册的数据
 * 
 * @author zkt
 *
 */
public class RegisterCount extends Configured implements Tool {
	// 获取应用程序执行输入的参数
	static String param = "";
	private static Logger logger = LogManager.getLogger(RegisterCount.class);

	// mapper
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {
		public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			// 读取一行的数据
			String line = value.toString();
			try {
				// 将字符串转换成json对象
				JSONObject jo = new JSONObject(line);
				// 通过json对象获取里面的数据
				String ja = jo.getString("data");
				logger.info("读取每行，获取的data数据:" + ja);
				if (null != ja || !"".equals(ja)) {
					UserEntity userEntity = (UserEntity) JsonUtil.getObjectFromJson(ja, UserEntity.class);
					Long timeStamp = new Long(userEntity.getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String sd = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
					// if (sd.equals(sdf.format(param))) {
					String type = userEntity.getType();
					IntWritable one = new IntWritable(1);
					Text key_ = new Text(type);
					output.collect(key_, one);
					// }
				}

			} catch (Exception e) {
				logger.error("json字符串转换成json对象失败，失败的原因是:" + e.getMessage());
				e.printStackTrace();
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

	public int run(String[] args) throws Exception {
		Path outPath = new Path(args[1]);
		JobConf conf = new JobConf(getConf(), RegisterCount.class);
		conf.setJobName("RegisterCount");
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(IntWritable.class);
		conf.setMapperClass(Map.class);
		conf.setCombinerClass(Reduce.class);
		conf.setReducerClass(Reduce.class);
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);
		// 读取args[0]数据，作为本mapreduce程序的输入目录
		String inputPath = "";
		FileSystem fs = FileSystem.get(conf);
		// 如果输出路径已存在，则先删除
		if (fs.exists(outPath)) {
			fs.delete(outPath, true);
			System.out.println(outPath + "输出路径存在，已删除！");
		}
		// 指定要读取的文件路径
		Path path = new Path(args[0]);
		inputPath = path.toString();
		fs.close();
		FileInputFormat.addInputPaths(conf, inputPath);
		// 指定计算完成结果输出路径

		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		// param=args[2];
		JobClient.runJob(conf);
		return 0;
	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new RegisterCount(), args);
		System.exit(res);
	}
}
