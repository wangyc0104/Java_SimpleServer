package wyc.servlet;

import wyc.server.Request;
import wyc.server.Response;
import wyc.server.Servlet;

public class LoginServlet implements Servlet {

	@Override
	public void service(Request request, Response response) {
		response.print("<html>");
		response.print("<head>");
		response.print("<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">");
		response.print("<title>");
		response.print("登录Servlet");
		response.print("</title>");
		response.print("</head>");
		response.print("<body>");
		response.print("欢迎回来" + request.getParameter("uname"));
		response.print("</body>");
		response.print("</html>");
	}

}
