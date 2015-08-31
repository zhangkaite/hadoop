package zkt.hdfs.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jettison.json.JSONObject;

import zkt.util.JsonUtil;
import zkt.util.SolrUtil;

public class Test {
	public static void main(String[] args) {
        String SOLR_URL = "http://192.168.13.30:8983/solr/zkt_test";
		SolrUtil solrUtil = new SolrUtil(SOLR_URL); 
		solrUtil.detIndex();
		try {
			//readFileByLines("F:\\addUser.txt",solrUtil);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

	public static void readFileByLines(String fileName, SolrUtil solrUtil) throws Exception {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			while ((tempString = reader.readLine()) != null) {
				long start_time = System.currentTimeMillis();
				JSONObject jo = new JSONObject(tempString);
				String ja = jo.getString("data");
				System.out.println(ja);
				if (null != ja || !"".equals(ja)) {
					UserEntity userEntity = (UserEntity) JsonUtil.getObjectFromJson(ja, UserEntity.class);
					String time = userEntity.getTime();
					Long timeStamp = new Long(time);
					SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					userEntity.setTime(sdfs.format(new Date(Long.parseLong(String.valueOf(timeStamp)))));
					solrUtil.createIndex(userEntity);
					long end_time = System.currentTimeMillis();
					System.out.println("处理一条数据花费的时间:" + (end_time - start_time));
				}
				
			}
			//reader.close();
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
