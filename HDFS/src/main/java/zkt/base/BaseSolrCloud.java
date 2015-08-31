package zkt.base;

import org.apache.solr.client.solrj.impl.CloudSolrServer;

@SuppressWarnings("serial")
public class BaseSolrCloud extends CloudSolrServer{

	public BaseSolrCloud(String zkHost,String collection) {
		super(zkHost);
		super.setDefaultCollection(collection);
	}
}

