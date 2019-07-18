package wyc.server;

/**
 * Servlet即Server和applet的合成词<br>
 * 是一个用来进行request和response处理的网络小组件<br>
 * @author Yicheng Wang
 */
public interface Servlet {
	public void service(Request request, Response response);
}
