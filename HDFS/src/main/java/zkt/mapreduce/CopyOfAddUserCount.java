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
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.codehaus.jettison.json.JSONObject;

import zkt.entity.UserEntity;
import zkt.util.JsonUtil;

public class CopyOfAddUserCount extends Configured {
	public static String currentDate = "";
	public static final String SOLR_URL = "http://192.168.13.30:8983/solr/zkttest";
	public static HttpSolrServer server = new HttpSolrServer(SOLR_URL);
	// mapper
	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {

		public void indexEntity(Object object){
			System.out.println("进入连接solr阶段##############################################");
			try {
				server.addBean((UserEntity)object);
				server.optimize();
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			try {
				// 将字符串转换成json对象
				JSONObject jo = new JSONObject(line);
				// 通过json对象获取里面的数据
				String ja = jo.getString("data");
				if (null != ja || !"".equals(ja)) {
					UserEntity userEntity = (UserEntity) JsonUtil.getObjectFromJson(ja, UserEntity.class);
					String type = userEntity.getType();
					String time = userEntity.getTime();
					// 将时间戳转换成成具体的日期
					Long timeStamp = new Long(time);
					SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					//String sd = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
		            userEntity.setTime(sdfs.format(new Date(Long.parseLong(String.valueOf(timeStamp)))));
		            System.out.println("进入创建索引阶段########################################");
		            indexEntity(userEntity);
					//if (sd.equals(currentDate)) {
						// 按终端统计，如果需要过滤时间可以根据数据的时间戳来过滤数据
						IntWritable one = new IntWritable(1);
						Text key_ = new Text(type);
						context.write(key_, one);
					//}
					

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// reduce
	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		MultipleOutputs<Text, Text> mos;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		public void setup(Context context) {
			mos = new MultipleOutputs(context);
		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			mos.close();
		}

		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			// context.write(key, result);
			System.out.println("进入操作数据库阶段");
		/*	DBUtil dbUtil = new DBUtil();
			String sql = "insert into t_user_register(time,count,clientType) values ('" + currentDate + "','" + sum + "','"
					+ key.toString() + "')";
			try {
				dbUtil.inSertData(sql);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			mos.write("addUser", key, result);
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
		//currentDate =args[2];
		Job job = Job.getInstance(conf, "AddUserCount");
		job.setJarByClass(CopyOfAddUserCount.class);
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
