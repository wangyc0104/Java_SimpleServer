package wyc.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WEB上下文(其实就是一堆的容器)
 * @author Yicheng Wang
 */
public class WebContext {
	private List<Entity> entities = null; // entity的列表
	private List<Mapping> mappings = null; // mapping的列表

	// 存储servlet-name对应servlet-class的map结构，key对应servlet-name，value对应servlet-class
	private Map<String, String> entityMap = new HashMap<String, String>();

	// 存储url-pattern对应servlet-name的map结构，key对应url-pattern，value对应servlet-name
	private Map<String, String> mappingMap = new HashMap<String, String>();

	public WebContext(List<Entity> entities, List<Mapping> mappings) {
		this.entities = entities; // 将解析好的entities作为参数传入
		this.mappings = mappings; // 将解析好的mappings作为参数传入
		// 将entity的List转换成对应的map
		for (Entity entity : entities) {
			entityMap.put(entity.getName(), entity.getClz());
		}
		// 将map的List转换成对应的map
		for (Mapping mapping : mappings) {
			for (String pattern : mapping.getPatterns()) {
				mappingMap.put(pattern, mapping.getName());
			}
		}
	}

	/**
	 * 通过URL的路径找到相对应的Class
	 * @param pattern 找到的Class限定名
	 * @return
	 */
	public String getClz(String pattern) {
		String name = mappingMap.get(pattern);
		return entityMap.get(name);
	}
	
}
