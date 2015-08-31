package zkt.entity;

import org.apache.solr.client.solrj.beans.Field;

public class MrEntity {

	@Field
	private String key;
	@Field
	private String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
