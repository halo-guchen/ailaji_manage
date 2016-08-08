package com.ailaji.manage.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonUtil {

	private static final ObjectMapper objectMapper;

	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	private static final com.fasterxml.jackson.databind.ObjectMapper obm = new com.fasterxml.jackson.databind.ObjectMapper();

	static {
		obm.configure(com.fasterxml.jackson.core.JsonParser.Feature.IGNORE_UNDEFINED, true);
		obm.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * @param json
	 * @param clsT
	 * @param clsP
	 * @param <T>
	 * @param <P>
	 * @return T<P> e.g:clsT:PayResponse clsP:CallAlipayResult return PayResponse<CallAlipayResult>
	 */
	public static <T, P> T getParameteriedObjectFromJson(String json, Class<T> clsT, Class<P> clsP) {
		JavaType javaType = obm.getTypeFactory().constructParametrizedType(clsT, clsT, clsP);
		try {
			T t = obm.readValue(json, javaType);
			return t;
		} catch (IOException e) {
			throw new RuntimeException(String.format("jsonUtil Failed.json:%s,clsT:%s,clsP:%s", json, clsT, clsP), e);
		}
	}


	public static <T> List<T> getObjectsFromJson(InputStream instream, Class<T> clsT) throws Exception {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(instream);

			JsonNode nodes = parser.readValueAsTree();
			List<T> list = new LinkedList<T>();

			for (JsonNode node : nodes) {
				list.add(objectMapper.readValue(node, clsT));
			}

			return list;

		} catch (JsonParseException e) {
			throw new Exception("parse json error", e);

		} finally {
			try {
				instream.close();

			} catch (Exception ignore) {
			}
		}
	}

	public static <T> List<T> getObjectsFromJson(String str, Class<T> clsT) throws Exception {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(str);

			JsonNode nodes = parser.readValueAsTree();
			List<T> list = new LinkedList<T>();

			for (JsonNode node : nodes) {
				list.add(objectMapper.readValue(node, clsT));
			}

			return list;

		} catch (JsonParseException e) {
			throw new Exception("parse json error, json=" + str +
					", class=" + clsT.getName(), e);

		} catch (IOException e) {
			throw new Exception("parse json error, json=" + str +
					", class=" + clsT.getName(), e);
		}
	}

	public static <T> T getObjectFromJson(String str, TypeReference<T> typeReference) throws Exception {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(str);
			return objectMapper.readValue(parser, typeReference);
		} catch (JsonParseException e) {
			throw new Exception("parse json error, json=" + str +
					", type=" + typeReference.getType(), e);

		} catch (IOException e) {
			throw new Exception("parse json error, json=" + str +
					", type=" + typeReference.getType(), e);
		}
	}

	public static <T> T getObjectFromJson(InputStream instream, Class<T> cls) throws Exception {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(instream);
			T t = objectMapper.readValue(parser, cls);
			return t;

		} catch (JsonParseException e) {
			throw new Exception("parse json error", e);

		} catch (IOException e) {
			throw new Exception("parse json error", e);

		} finally {
			try {
				instream.close();

			} catch (Exception ignore) {

			}
		}
	}

	public static <T> T getObjectFromJson(String str, Class<T> cls) throws Exception {
		try {
			JsonParser parser = objectMapper.getJsonFactory().createJsonParser(str);
			T t = objectMapper.readValue(parser, cls);
			return t;

		} catch (JsonParseException e) {
			throw new Exception("parse json error, json=" + str +
					", class=" + cls.getName(), e);

		} catch (IOException e) {
			throw new Exception("parse json error, json=" + str +
					", class=" + cls.getName(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public static String getValueByFieldFromJson(String json, String field) throws Exception {
		Map<String, String> mapValue = getObjectFromJson(json, HashMap.class);
		return String.valueOf(mapValue.get(field));
	}

	public static String getJsonFromObject(Object object) throws Exception {
		try {
			return objectMapper.writeValueAsString(object);

		} catch (JsonGenerationException e) {
			throw new Exception("get json error", e);

		} catch (JsonMappingException e) {
			throw new Exception("get json error", e);

		} catch (IOException e) {
			throw new Exception("get json error", e);
		}
	}

	public static String toJson(Object o) {
		try {
			return obm.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("转换对象为json失败", e);
		}
	}

	public static <T> T toBean(String json, Class<T> clz) {
		try {
			T t = obm.readValue(json, clz);
			return t;
		} catch (IOException e) {
			throw new RuntimeException("转换json为对象Failed", e);
		}
	}

	public static String getJsonWithoutNullFromObject(Object object) throws Exception {
		try {
			ObjectMapper om = new ObjectMapper();
			om.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
			om.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			om.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
			return om.writeValueAsString(object);

		} catch (JsonGenerationException e) {
			throw new Exception("get json error", e);

		} catch (JsonMappingException e) {
			throw new Exception("get json error", e);

		} catch (IOException e) {
			throw new Exception("get json error", e);
		}
	}
}
