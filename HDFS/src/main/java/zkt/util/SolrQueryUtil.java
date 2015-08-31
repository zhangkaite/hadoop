package zkt.util;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

public class SolrQueryUtil {

	/**
	 * 通过条件精确查询 还没添加：时间段 的查询 nickName字段是模糊查询
	 * 
	 * @return
	 * @throws SolrServerException
	 */
	public static QueryResponse assemblyConditionsOfSelect(SolrServer solrServer, Map<String, Object> fieldValues,
			Map<String, List<String>> timeMap, Integer start, Integer count, Map<String, Boolean> sortFields,
			List<String> showFields) throws SolrServerException {

		SolrQuery query = null;

		/* 查询字段输入是否合法 */
		if (fieldValues == null || fieldValues.size() == 0) {
			return null;
		}
		/* 初始化查询对象 */
		query = new SolrQuery("*:*");
		if (fieldValues.containsKey("sorfFields")) {
			fieldValues.remove("sorfFields");
		}
		Set<String> setFieldValue = fieldValues.keySet();
		Iterator<String> iterators = setFieldValue.iterator();
		StringBuffer queryStr = new StringBuffer();
		int i = 0;// 计数
		while (iterators.hasNext()) {
			String field = iterators.next();
			if (i != setFieldValue.size() - 1) {
				if (field.equals("nickName")) {
					queryStr.append(field + ":*" + fieldValues.get(field) + "* AND ");
				} else {
					queryStr.append(field + ":\"" + fieldValues.get(field) + "\" AND ");
				}
			} else if (i == setFieldValue.size() - 1) {
				if (field.equals("nickName")) {
					queryStr.append(field + ":*" + fieldValues.get(field) + "*");
				} else {
					queryStr.append(field + ":\"" + fieldValues.get(field) + "\"");
				}
			}
			i++;
		}
		query.addFilterQuery(queryStr.toString());
		/* 添加时间范围 */
		if (timeMap != null && timeMap.size() > 0) {
			Set<String> times = timeMap.keySet();
			Iterator<String> iteratorTime = times.iterator();
			StringBuffer queryTime = new StringBuffer();
			int y = 0;
			while (iteratorTime.hasNext()) {
				String field = iteratorTime.next();
				y++;
				if (y != timeMap.size()) {
					queryTime.append(field + ":[" + timeMap.get(field).get(0) + " TO " + timeMap.get(field).get(0) + "] AND ");
				} else if (y == timeMap.size()) {
					queryTime.append(field + ":[" + timeMap.get(field).get(0) + " TO " + timeMap.get(field).get(0) + "]");
				}
			}
			query.addFilterQuery(queryTime.toString());
		}
		/* 设置起始位置与返回结果数 */
		query.setStart(start);
		query.setRows(count);
		/* 设置限定的结果集 */
		query.addField(SolrConstant.SOLR_KEY_ID);
		query.addField(SolrConstant.SOLR_KEY_DATASOURCEKEY);
		/* 设置排序 */
		if (sortFields != null && sortFields.size() > 0) {
			Set<String> setSortFields = sortFields.keySet();
			Iterator<String> iters = setSortFields.iterator();
			while (iters.hasNext()) {
				String sortField = iters.next();
				if (sortFields.get(sortField)) {
					query.addSort(sortField, SolrQuery.ORDER.asc);
				} else {
					query.addSort(sortField, SolrQuery.ORDER.desc);
				}
			}
		}
		return solrServer.query(query);
	}

	/**
	 * 通过唯一关键字查询信息： 只返回userid 和dataSourceKey
	 * 
	 * @return
	 * @throws SolrServerException
	 */
	public static QueryResponse selectByKeyWordReturnUserIdAndDataSourcekey(SolrServer solrServer, String field, String key,
			int start, int count) throws SolrServerException {
		SolrQuery query = null;
		/* 检查参数是否合法 */
		if (null == field || null == key) {
			return null;
		}
		if (count < 1) {
			return null;
		}
		/* 初始化查询对象 */
		query = new SolrQuery("*:*");
		query.addFilterQuery(field + ":" + key);

		/* 查询的字段只有 id 和datasourcekey */
		query.addField(SolrConstant.SOLR_KEY_DATASOURCEKEY);
		query.addField(SolrConstant.SOLR_KEY_ID);

		/* 设置起始位置和返回结果数 */
		query.setStart(start);
		query.setRows(count);
		return solrServer.query(query);
	}

	/**
	 * 通过唯一关键字查询信息： 返回id的全部的数据
	 * 
	 * @return
	 * @throws SolrServerException
	 */
	public static QueryResponse selectByKeyWord(SolrServer solrServer, String field, String key, int start, int count)
			throws SolrServerException {
		SolrQuery query = null;
		/* 检查参数是否合法 */
		if (null == field || null == key) {
			return null;
		}
		if (count < 1) {
			return null;
		}
		/* 初始化查询对象 */
		query = new SolrQuery("*:*");
		query.addFilterQuery(field + ":" + key);

		/* 设置起始位置和返回结果数 */
		query.setStart(start);
		query.setRows(count);
		return solrServer.query(query);
	}

	/**
	 * 通过ids 查询Item的列表， 查询字段是：id，返回结果字段是 id 和 dataSourceKey
	 * 
	 * @param solrServer
	 * @param ids
	 * @return
	 */
	public static QueryResponse selectByIds(SolrServer solrServer, List<BigInteger> ids) throws Exception {
		SolrQuery query = null;
		/* 检查参数是否合法 */
		if (ids == null || ids.size() == 0) {
			return null;
		}
		/* 初始化查询对象 */
		query = new SolrQuery("*:*");
		StringBuffer queryStr = new StringBuffer();
		for (int i = 0; i < ids.size(); i++) {
			if (i != ids.size() - 1) {
				queryStr.append(SolrConstant.SOLR_KEY_ID + ":" + ids.get(i) + " OR ");
			} else if (i == ids.size() - 1) {
				queryStr.append(SolrConstant.SOLR_KEY_ID + ":" + ids.get(i));
			}
		}
		query.addFilterQuery(queryStr.toString());

		/* 查询的字段只有 id 和datasourcekey */
		query.addField(SolrConstant.SOLR_KEY_DATASOURCEKEY);
		query.addField(SolrConstant.SOLR_KEY_ID);

		/* 设置起始位置和返回结果数 */
		query.setStart(0);
		query.setRows(ids.size());
		return solrServer.query(query);
	}

	/**
	 * 字符串的第一个字母大写
	 * 
	 * @param fildeName
	 * @return
	 */
	public static String getLowerName(String fildeName) {
		byte[] items = fildeName.getBytes();
		items[0] = (byte) ((char) items[0] - 'A' + 'a');
		return new String(items);
	}
}
