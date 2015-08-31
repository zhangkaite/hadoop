package zkt.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import zkt.base.BaseSolrCloud;

public class SolrCloudUtil {

	public BaseSolrCloud solrCloud;
	public String zkHost;
	public String collection;
	private final int DELAY_TIME = 2000;
	public List<Object> ls = new ArrayList<Object>();

	SolrCloudUtil(String zkHost, String collection) {
		this.zkHost = zkHost;
		this.collection = collection;
	}

	/**
	 * 将数据录入solr
	 */
	public void createIndex(Object obj) {
		try {
			solrCloud.addBean(obj, DELAY_TIME);
		} catch (Exception e) {
			ls.add(obj);
		}
	}

	/**
	 * 删除索引
	 */
	public void detIndex() {

		try {
			// 删除所有的索引
			solrCloud.deleteByQuery("*:*");
			solrCloud.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 创建索引失败的数据写入本地文件
	 */
	/***
	 * 处理list,将失败的数据写入本地磁盘
	 * 
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
				String data = JsonUtil.getObjectToJson(object);
				FileOperation.writeData2File(filename, data);
			}
		}
	}

}
