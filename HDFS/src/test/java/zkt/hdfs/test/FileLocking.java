package zkt.hdfs.test;

import java.io.FileOutputStream;
import java.nio.channels.FileLock;

public class FileLocking {

	 public static void main(String[] args) throws Exception {  
	        FileOutputStream fos = new FileOutputStream("F:\\addUser.txt");  
	        //获取文件锁 FileLock 对象  
	        FileLock fl = fos.getChannel().tryLock();  
	        //tryLock是尝试获取锁，有可能为空，所以要判断  
	        if (fl != null) {  
	            System.out.println("Locked File");  
	            Thread.sleep(100);  
	            fl.release();//释放锁  
	            System.out.println("Released Lock");  
	        }  
	        fos.close();  
	    }  
}
