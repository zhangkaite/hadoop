package zkt.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

@SuppressWarnings("unused")
public class SolrUtil {

	/**
	 * 初始化solr连接
	 */
	private String solrUrl;
	private HttpSolrServer solrServer;
	public List<Object> ls = new ArrayList<Object>();
	public static int num=0;
	private final int DELAY_TIME = 2000;

	public SolrUtil(String solrUrl) {
		this.solrUrl = solrUrl;
		this.solrServer = new HttpSolrServer(solrUrl);
	}

	/**
	 * 将数据录入solr
	 */
	public void createIndex(Object obj) {
		try {
			solrServer.addBean(obj,DELAY_TIME);
		} catch (Exception e) {
			ls.add(obj);
		} 
	}
	
	
	public  void detIndex(){
		
		 try {
            //删除所有的索引
			 solrServer.deleteByQuery("*:*");
			 solrServer.commit();
     } catch (Exception e) {
            e.printStackTrace();
     }

	}
	
	
	/**
	 * 追加文件：使用FileWriter
	 * 
	 * @param fileName
	 * @param content
	 */
	public  void writeData2File(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.write("\n");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	/***
	 * 处理list,将失败的数据写入本地磁盘
	 * @throws Exception 
	 */

	@SuppressWarnings("rawtypes")
	public void dealException(String filename) throws Exception {
		if (!new File(filename).exists()) {
			new File(filename).mkdirs();
		}
		if (ls.size() > 0) {
			for (Iterator iterator = ls.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				String data=JsonUtil.getObjectToJson(object);
				writeData2File(filename,data);
			}
		}
	}
}
