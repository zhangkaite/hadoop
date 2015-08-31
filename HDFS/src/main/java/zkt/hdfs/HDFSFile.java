package zkt.hdfs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class HDFSFile {
	private static final Logger LOG = LogManager.getLogger(HDFSFile.class);
	// initialization
	static Configuration conf = new Configuration();
	static FileSystem hdfs;
	static {
		String path = "/HDFS/resource/";
		conf.addResource(new Path(path + "core-site.xml"));
		conf.addResource(new Path(path + "hdfs-site.xml"));
		/*conf.addResource(new Path(path + "mapred-site.xml"));
		path = "/usr/java/hbase-0.90.3/conf/";
		conf.addResource(new Path(path + "hbase-site.xml"));*/
		try {
			hdfs = FileSystem.get(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			makeDir("/test");
			LOG.info("创建文件夹成功");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建一个目录
	 * @param path
	 * @throws IOException
	 */
	public static boolean makeDir(String path) throws IOException{
		FileSystem  fs = FileSystem.get(conf);
		return fs.mkdirs(new Path(path));
	}
	
	/**
	 * create file and return its BufferedWriter object
	 * @param path
	 * @return 该文件的写流
	 * @throws IOException
	 */
	public static BufferedWriter createFile(String path) throws IOException{
		FileSystem fs = FileSystem.get(conf);
		FSDataOutputStream dataOutputStream = FileSystem.create(fs, new Path(path),FsPermission.valueOf("-rw-rw-rw-"));
		LOG.info("create file: "+path+" successfully.");
		return new BufferedWriter(
				new OutputStreamWriter(dataOutputStream,"UTF-8"));
	}
	
	 //create a new file  
    public void createFile(String fileName, String fileContent) throws IOException {  
        Path dst = new Path(fileName);  
        byte[] bytes = fileContent.getBytes();  
        FSDataOutputStream output = hdfs.create(dst);  
        output.write(bytes);  
        System.out.println("new file \t" + conf.get("fs.defaultFS") + fileName);  
    }  

	  //create a direction  
    public void createDir(String dir) throws IOException {  
        Path path = new Path(dir);  
        hdfs.mkdirs(path);  
        System.out.println("new dir \t" + conf.get("fs.defaultFS") + dir);  
    }     
	/**
	 * 在HDFS上创建一个文件夹
	 * 
	 * **/
	public static void createDirectoryOnHDFS() throws Exception {
		 conf.set("fs.defaultFS", "hdfs://n2:8020");
		conf.addResource(new Path("/resource/core-site.xml"));
		FileSystem fs = FileSystem.get(conf);
		Path p = new Path("zkttest.txt");
		fs.mkdirs(p);
		fs.close();// 释放资源
		System.out.println("创建文件夹成功.....");

	}
}
