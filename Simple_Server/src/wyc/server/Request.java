package wyc.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class Request {
	// 请求信息
	private String requestInfo;
	// 请求方式信息(GET/POST)
	private String method;
	// 请求地址
	private String url;
	// 查询信息
	private String queryStr;
	// 由查询信息得到的属性列表
	private Map<String, List<String>> parameterMap;

	// 常量换行符
	private static final String CRLF = "\r\n";

	/**
	 * 由客户端得到构造方法，然后重载
	 * 
	 * @param client
	 * @throws IOException
	 */
	public Request(Socket client) throws IOException {
		this(client.getInputStream());
	}

	/**
	 * 由输入流得到Request构造方法
	 * 
	 * @param client
	 * @throws IOException
	 */
	public Request(InputStream is) {
		parameterMap = new HashMap<String, List<String>>();
		byte[] datas = new byte[1024 * 1024 * 10];
		int len;
		try {
			System.out.println(is);
			len = is.read(datas);
			System.out.println(len);
			this.requestInfo = new String(datas, 0, len);
			System.out.println("客户端的is读取完毕");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		System.out.println(requestInfo);
		// 分解字符串
		parseRequestInfo();
	}

	/**
	 * 分解字符串的方法
	 */
	private void parseRequestInfo() {
		System.out.println("开始分解Request字符串");
		// 得到请求方法（GET/POST）
		this.method = this.requestInfo.substring(0, this.requestInfo.indexOf("/")).trim();
//		System.out.println(this.method);
		// 得到URL和查询参数
		int urlStartIndex = this.requestInfo.indexOf('/') + 1; // URL+Query开始下标
		int urlEndIndex = this.requestInfo.indexOf("HTTP/"); // URL+Query结束下标
		this.url = this.requestInfo.substring(urlStartIndex, urlEndIndex).trim();
		int queryIndex = this.url.indexOf('?');
		if (queryIndex >= 0) { // 表示存在请求参数
			String[] urlArray = this.url.split("\\?");
			this.url = urlArray[0]; // URL字符串
			this.queryStr = urlArray[1]; // Query字符串
		}
//		System.out.println("url = " + this.url);
//		System.out.println("Query = " + this.queryStr);
		// 此处需要获取POST请求中正文中包含的查询参数
		if (method.equalsIgnoreCase("post")) {
			String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
//			System.out.println(qStr);
			if (null == queryStr) {
				queryStr = qStr;
			} else {
				queryStr += "&" + qStr;
			}
		}
		queryStr = null == queryStr ? "" : queryStr;
		System.out.println("method: " + method);
		System.out.println("url: " + url);
		System.out.println("queryStr: " + queryStr);
		// 把QueryString转换成Map<String, String>
		convertMap();
	}

	private void convertMap() {
		// 1. 分割"&"字符串
		String[] keyValues = this.queryStr.split("&");
		for (String queryStr : keyValues) {
			// 2. 分割"="字符串
			String[] kv = queryStr.split("=");
			kv = Arrays.copyOf(kv, 2); // 如果有的参数没有等号后面值，就用null填充
			String key = kv[0];
			String value = kv[1];
			if (!parameterMap.containsKey(key)) { // 若parameterMap没有key值，则需要创建一个值的ArrayList
				parameterMap.put(key, new ArrayList<String>());
			}
			parameterMap.get(key).add(value);
		}
		System.out.println(parameterMap);
	}

	/**
	 * 处理中文
	 * 
	 * @return
	 */
	private String decode(String value, String enc) {
		try {
			return java.net.URLDecoder.decode(value, enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 通过name获取对应的多个值
	 * 
	 * @param key
	 * @return
	 */
	public String[] getParameterValues(String key) {
		List<String> values = this.parameterMap.get(key);
		if (null == values || values.size() < 1) {
			return null;
		}
		return values.toArray(new String[0]); // 此处的new String[0]是指List转成String[]类型，而0无意义，随便取一个数字都行。
	}

	/**
	 * 获取name属性的第一个值
	 * 
	 * @param key
	 * @return
	 */
	public String getParameter(String key) {
		String[] values = getParameterValues(key);
		return values == null ? null : values[0];
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getQueryStr() {
		return queryStr;
	}

	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}

}
