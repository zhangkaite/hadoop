package zkt.hdfs;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsRead {

	static Configuration conf = new Configuration();

	public static void main(String[] args) {
		try {
			readHDFSListAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/***
	 * 
	 * 读取HDFS某个文件夹的所有 文件，并打印
	 * 
	 * **/
	public static void readHDFSListAll() throws Exception {

		// 流读入和写入
		InputStream in = null;
		// 获取HDFS的conf
		// 读取HDFS上的文件系统
		FileSystem hdfs = FileSystem.get(conf);
		// 使用缓冲流，进行按行读取的功能
		BufferedReader buff = null;
		// 获取日志文件的根目录
		Path listf = new Path("/hadoop/mq/");
		// 获取根目录下的所有2级子文件目录
		FileStatus stats[] = hdfs.listStatus(listf);
		// 自定义j，方便查看插入信息
		for (int i = 0; i < stats.length; i++) {
			// 获取子目录下的文件路径
			FileStatus temp[] = hdfs.listStatus(new Path(stats[i].getPath().toString()));
			for (int k = 0; k < temp.length; k++) {
				System.out.println("文件路径名:" + temp[k].getPath().toString());
				// 获取Path
				Path p = new Path(temp[k].getPath().toString());
				// 打开文件流
				in = hdfs.open(p);
				// BufferedReader包装一个流
				buff = new BufferedReader(new InputStreamReader(in));
				String str = null;
				while ((str = buff.readLine()) != null) {
					System.out.println(str);
				}
				buff.close();
				in.close();

			}

		}

		hdfs.close();

	}
}
