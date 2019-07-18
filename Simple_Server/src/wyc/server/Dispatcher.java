package wyc.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Dispatcher的作用：作为多线程载体创建request和response的交互动作
 * 
 * @author Yicheng Wang
 *
 */
public class Dispatcher implements Runnable {
	private Socket client = null;
	private Request request;
	private Response response;

	public Dispatcher(Socket client) {
		try {
			this.client = client;
			request = new Request(client); // 获取请求协议
			response = new Response(client); // 获取响应协议
		} catch (IOException e) {
			System.err.println("Dispatcher初始化错误！");
			this.release();
		}
	}

	@Override
	public void run() {
		System.out.println("Dispatcher的run方法中request：" + request);
		try {
			if (null == request.getUrl() || request.getUrl().equals("")) {
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("index.html");
				StringBuilder sb = new StringBuilder("");
				byte[] bytes = new byte[1024];
				int len = -1;
				while ((len = is.read(bytes)) != -1) {
					sb.append(new String(bytes, 0, len));
				}
				response.print(sb.toString());
				response.pushToBrowser(200);
				is.close();
				return;
			}
			Servlet servlet = WebApp.getServletFromUrl(request.getUrl());
			if (null != servlet) { // 正常访问读取到的网页
				servlet.service(request, response);
				// 状态码输出
				response.pushToBrowser(200);
			} else { // 访问异常：未找到页面
				InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("error.html");
				StringBuilder sb = new StringBuilder("");
				byte[] bytes = new byte[1024];
				int len = -1;
				while ((len = is.read(bytes)) != -1) {
					sb.append(new String(bytes, 0, len));
				}
				response.print(sb.toString());
				response.pushToBrowser(404);
				is.close();
			}
		} catch (Exception e) {
			try {
				System.err.println("Dispatcher的run方法错误！");
				response.println("服务器内部错误");
				response.pushToBrowser(505);
			} catch (IOException e1) {
				System.err.println("Dispatcher的run方法中的错误处理出现异常！");
			}
		}
		release();
	}

	// 释放资源
	private void release() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
