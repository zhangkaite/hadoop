package zkt.util;

public interface SolrConstant {

	/**
	 * SolrUserInfo查询字段
	 */
	String SOLRUSERINFO_KEY_TTNUM = "TTnum";
	String SOLRUSERINFO_KEY_USERNAME ="userName";
	String SOLRUSERINFO_KEY_PASSWORD ="password";
	String SOLRUSERINFO_KEY_TIME = "time";
	String SOLRUSERINFO_LOGINGOODTTNUM ="loginGoodTTnum";
	String SOLRUSERINFO_LOGINGOODTTNUMTYPE ="loginGoodTTnumType";

	/**
	 * SolrUserCrossRelation查询字段
	 */
	String SOLRUSERCROSSRELATION_USERIDA = "userIdA";
	String SOLRUSERCROSSRELATION_TYPE = "type";
	
	/**
	 * 公共结果限定
	 */
	String SOLR_KEY_ID = "id";
	String SOLR_KEY_DATASOURCEKEY = "dataSourceKey";
	String SOLR_KEY_TTNUM = "TTnum";
	/**
	 * Solr查询默认起始位置和 偏移量
	 */
	int SOLR_START = 0;
	int SOLR_UNIQUE = 1;
	int SOLR_COUNT = 9999;
	
	/**
	 * SolrGroup 默认分组的类型
	 */
	int DEFAULT_VALUE_FRIEND = 0;
	int DEFAULT_VALUE_BLACKLIST = 1;
	int DEFAULT_VALUE_NORMAL = 2;
}
