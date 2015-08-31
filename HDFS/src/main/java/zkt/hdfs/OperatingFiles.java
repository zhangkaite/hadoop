package zkt.hdfs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class OperatingFiles {

	private static final Logger LOG = LogManager.getLogger(OperatingFiles.class);
	/**
	 * 在HDFS上创建一个文件夹
	 * 
	 * **/
	static Configuration conf = new Configuration();

	public static void createDirectoryOnHDFS() throws Exception {
		conf.set("fs.defaultF", "hdfs://192.168.12.102:8020");
		FileSystem fs = FileSystem.get(conf);
		Path p = new Path("/zkt");
		fs.mkdirs(p);
		fs.close();// 释放资源
		System.out.println("创建文件夹成功.....");

	}
	public static BufferedWriter createFile(String path) throws IOException{
		conf.set("fs.defaultF", "hdfs://192.168.12.102:8020");
		FileSystem fs = FileSystem.get(conf);
		FSDataOutputStream dataOutputStream = FileSystem.create(fs, new Path(path),FsPermission.valueOf("-rw-rw-rw-"));
		LOG.info("create file: "+path+" successfully.");
		return new BufferedWriter(
				new OutputStreamWriter(dataOutputStream,"UTF-8"));
	}

	// initialization
	public static void main(String[] args) throws IOException {
		/*try {
			createDirectoryOnHDFS();
			//createFile("/dfs/dn/test");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		 String uri = args[0];  
	     //   Configuration conf = new Configuration();  
	        FileSystem fs = FileSystem.get(URI.create(uri), conf);  
	  
	       /* Path[] paths = new Path[args.length];  
	        for (int i = 0; i < paths.length; i++) {  
	            paths[i] = new Path(args[i]);  
	        }  */
	        Path paths = new Path("/");
	  
	        FileStatus[] status = fs.listStatus(paths);  
	        Path[] listedPaths = FileUtil.stat2Paths(status);  
	        for (Path p : listedPaths) {  
	            System.out.println(p);  
	        }  
		/*try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(new URI("hdfs://192.168.12.102:8020"), conf);
            Path file = new Path("/test.txt");
            FSDataInputStream getIt = fs.open(file);
            BufferedReader d = new BufferedReader(new InputStreamReader(getIt));
            String s = "";
            while ((s = d.readLine()) != null) {
                System.out.println(s);
            }
            d.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
	}
}