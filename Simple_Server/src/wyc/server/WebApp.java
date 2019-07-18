package wyc.server;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 服务器应用内核
 * @author Yicheng Wang
 */
public class WebApp {
	
	// WebApp类初始化时就开始进行SAX解析
	private static WebContext webContext;
	static {
		try {
			// SAX解析
			// 1.获取解析工厂
			SAXParserFactory factory = SAXParserFactory.newInstance();
			// 2.从解析工厂获取解析器
			SAXParser parser = factory.newSAXParser();
			// 3.编写处理器
			// 4.加载文档Document，注册处理器
			WebHandler handler = new WebHandler();
			// 5.解析
			parser.parse(Thread.currentThread().getContextClassLoader().getResourceAsStream("web.xml"),handler);
			// 6.获取数据
			webContext = new WebContext(handler.getEntities(), handler.getMappings());
			System.out.println("解析配置文件成功");
		} catch (Exception e) {
			System.err.println("解析配置文件错误");
		}
	}
	
	/**
	 * WebApp通过url来获取配置文件相对应的servlet
	 * @param url 用户在浏览器中输入的地址
	 * @return 相应的Servlet处理器
	 */
	public static Servlet getServletFromUrl(String url) {
		// 假设输入了localhost:8888/login
		String className = webContext.getClz("/" + url);
		Class clz;
		try {
			System.out.println("url-->"+url+"className-->"+className);
			clz = Class.forName(className);
			Servlet servlet = (Servlet) clz.getConstructor().newInstance();
			return servlet;
		} catch (Exception e) {
			System.err.println("WebApp的getServletFromUrl方法出现错误");
		}
		return null;
	}
	
}
