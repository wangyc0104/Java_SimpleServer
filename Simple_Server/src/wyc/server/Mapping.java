package wyc.server;

import java.util.HashSet;
import java.util.Set;

/**
 * <servlet-mapping>
 *   <servlet-name>login</servlet-name>
 *   <url-pattern>/login</url-pattern>
 * </servlet-mapping>
 * 
 * Mapping是一个简单的JavaBean，其中name存储Servlet的类名，Set存储所有的url地址
 * 
 * @author Yicheng Wang
 */
public class Mapping {
	private String name;
	private Set<String> patterns;

	public Mapping() {
		patterns = new HashSet<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getPatterns() {
		return patterns;
	}

	public void setPatterns(Set<String> patterns) {
		this.patterns = patterns;
	}
	
	public void addPattern(String pattern) {
		this.patterns.add(pattern);
	}
}
