package wyc.server;

/**
 * <servlet>
 *   <servlet-name>login</servlet-name>
 *   <servlet-class>wyc.user.LoginServlet</servlet-class>
 * </servlet>
 * 
 * Entity是一个简单的JavaBean，其中存储了Servlet的名字和相应的类限定名
 * 
 * @author Yicheng Wang
 */
public class Entity {
	private String name;
	private String clz;

	public Entity() { }

	public String getName() {
		return this.name;
	}

	public String getClz() {
		return clz;
	}

	public void setClz(String clz) {
		this.clz = clz;
	}

	public void setName(String name) {
		this.name = name;
	}
}