package zkt.hdfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Hello world!
 * 现有的思路：通过flume采集日志存放在hadoop hdfs上。
 * hadoop处理流程：
 * 1、hdfs操作
 * 2、在yarn上创建mapreduce 处理程序
 * 3、将处理的结果写入hbase
 *
 *
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	readFileByLines("F:\\test.txt");
        
    }
    
    
    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	String[] columns = tempString.split("\\s+");
            	System.out.println(columns[2]);
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

}
