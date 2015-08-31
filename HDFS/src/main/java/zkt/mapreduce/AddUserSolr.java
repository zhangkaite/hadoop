package zkt.mapreduce;

import java.io.IOException;

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

import zkt.entity.UserEntity;
import zkt.util.JsonUtil;
import zkt.util.SolrUtil;

public class AddUserSolr extends Configured {
	public static String currentDate = "";
	public static final String SOLR_URL = "http://192.168.13.30:8983/solr/zkt_test";
	public static SolrUtil solrUtil;

	// mapper
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
		@Override
		protected void setup(org.apache.hadoop.mapreduce.Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			solrUtil = new SolrUtil(SOLR_URL);
		};

		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			//将获取的字符串转换成对象
			try {
				JSONObject jsonObject = new JSONObject(line);
                String  data=jsonObject.getString("data");
                UserEntity u=(UserEntity) JsonUtil.getObjectFromJson(data, UserEntity.class);
                if (null!=u) {
					solrUtil.createIndex(u);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch blockd
				e.printStackTrace();
			}
			
			

		}

		@Override
		protected void cleanup(org.apache.hadoop.mapreduce.Mapper<Object, Text, Text, IntWritable>.Context context)
				throws IOException, InterruptedException {
			 try {
				solrUtil.dealException("/home/software/mr");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
	}

	// reduce
	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

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
		job.setJarByClass(AddUserSolr.class);
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
