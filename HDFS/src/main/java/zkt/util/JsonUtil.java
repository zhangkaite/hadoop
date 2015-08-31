package zkt.util;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

	/**
	 * 将Object对象转换成Json格式的数据
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String getObjectToJson(Object obj) throws Exception {
		String resultJson = null;
		ObjectMapper jsonMapper = new ObjectMapper();
		resultJson = jsonMapper.writeValueAsString(obj);
		if (resultJson != null) {
			return resultJson;
		}
		return null;
	}

	/**
	 * 将Json字符串转换成相应的
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object getObjectFromJson(String json, Class objClass) throws Exception {
		ObjectMapper jsonMapper = new ObjectMapper();
		Object obj = jsonMapper.readValue(json, objClass);
		if (obj != null) {
			return obj;
		}
		return null;
	}
}