package zkt.mapreduce;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.codehaus.jettison.json.JSONObject;

import zkt.entity.TBRecharge;
import zkt.util.DBUtil;
import zkt.util.JsonUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class TbClientTypeRechargeCount extends Configured {
	public static String currentDate = "";

	// mapper
	public static class TokenizerMapper extends Mapper<Object, Text, Text, Text> {
		
		
	   public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String line = value.toString();
			try {
				// 将字符串转换成json对象
				JSONObject jo = new JSONObject(line);
				// 通过json对象获取里面的数据
				String ja = jo.getString("data");
				if (null != ja || !"".equals(ja)) {
					TBRecharge tbRechargeEntity = (TBRecharge) JsonUtil.getObjectFromJson(ja, TBRecharge.class);
					// 获取终端类型
					String clientType = tbRechargeEntity.getClientType();
					String time = tbRechargeEntity.getTime();
					// 将时间戳转换成成具体的日期
					Long timeStamp = new Long(time);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String sd = sdf.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
					// 添加时间过滤条件和ordeid过滤条件
					String orderid = tbRechargeEntity.getOrderId();
					// 按照时间和orderid过滤数据
					if (sd.equals(currentDate) && !orderid.contains("admin")) {
						// 获取交易金额数据
						BigDecimal num = tbRechargeEntity.getNumber();
						Text bw = new Text(num.toString());
						// 按天统计交易量
						Text key_ = new Text(sd+"\t"+clientType);
						context.write(key_, bw);
					}

					// 统计交易详情

					// 按终端统计交易量

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// reduce
	public static class IntSumReducer extends Reducer<Text, Text, Text, Text> {
		MultipleOutputs<Text, Text> mos;

		public void setup(Context context) {
			mos = new MultipleOutputs(context);
		}

		protected void cleanup(Context context) throws IOException, InterruptedException {
			mos.close();
		}

		private BigDecimal result = new BigDecimal(0);

		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			BigDecimal f = new BigDecimal(0);
			for (Text val : values) {
				// 将Text转换成BigDecimal
				String num = val.toString();
				f.add(new BigDecimal(num));
			}
			result = f;
			
			
			// context.write(key, result);
			mos.write("tbClientTypeDayCount", key, result);
		}
		
		
		
		
		
		
		
	}

	public static void main(String[] args) throws Exception {
		currentDate = args[2];
		Configuration conf = new Configuration();
		FileSystem fs = FileSystem.get(conf);
		Path outPath = new Path(args[1]);
		// 如果输出路径已存在，则先删除
		if (fs.exists(outPath)) {
			fs.delete(outPath, true);
			System.out.println(outPath + "输出路径存在，已删除！");
			fs.close();
		}
		Job job = Job.getInstance(conf, "TbClientTypeRechargeCount");
		job.setJarByClass(TbClientTypeRechargeCount.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		MultipleOutputs.addNamedOutput(job, "tbClientTypeDayCount", TextOutputFormat.class, Text.class, Text.class);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
