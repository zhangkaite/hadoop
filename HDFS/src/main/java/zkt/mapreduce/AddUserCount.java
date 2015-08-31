package zkt.mapreduce;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.codehaus.jettison.json.JSONObject;

import zkt.entity.MrEntity;
import zkt.entity.UserEntity;
import zkt.util.JsonUtil;
import zkt.util.SolrUtil;

public class AddUserCount extends Configured {
	public static String currentDate = "";
	public static final String SOLR_URL = "http://192.168.13.30:8983/solr/zkttest";
	public static SolrUtil solrUtil;
	public static final String PATH_STRING="/home/software/exception/addUser.txt";

	// mapper
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			try {
				// 将字符串转换成json对象
				JSONObject jo = new JSONObject(line);
				// 通过json对象获取里面的数据
				String ja = jo.getString("data");
					UserEntity userEntity = (UserEntity) JsonUtil.getObjectFromJson(ja, UserEntity.class);
					if (null!=userEntity) {
						String type = userEntity.getType();
						String time = userEntity.getTime();
						// 将时间戳转换成成具体的日期
						Long timeStamp = new Long(time);
						SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						userEntity.setTime(sdfs.format(new Date(Long.parseLong(String.valueOf(timeStamp)))));
						IntWritable one = new IntWritable(1);
						Text key_ = new Text(type);
						context.write(key_, one);
					}
			
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	// reduce
	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		MultipleOutputs<Text, Text> mos;

		public void setup(Context context) {
			solrUtil = new SolrUtil(SOLR_URL);
		}

		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			MrEntity mrEntity=new MrEntity();
			mrEntity.setKey(key.toString());
			mrEntity.setValue(String.valueOf(sum));
			solrUtil.createIndex(mrEntity);
		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			try {
				solrUtil.dealException(PATH_STRING);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		Path outPath = new Path(args[1]);
		// 如果输出路径已存在，则先删除
		if (fs.exists(outPath)) {
			fs.delete(outPath, true);
			System.out.println(outPath + "输出路径存在，已删除！");
			fs.close();
		}
		Job job = Job.getInstance(conf, "AddUserCount");
		job.setJarByClass(AddUserCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		MultipleOutputs.addNamedOutput(job, "addUser", TextOutputFormat.class, Text.class, IntWritable.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
