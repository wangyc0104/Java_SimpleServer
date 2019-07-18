package wyc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器应用
 * @author Yicheng Wang
 */
public class Server {
	// 服务器Socket
	private ServerSocket serverSocket = null;
	// 服务器运行标记
	private boolean isRunning;

	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}

	// 启动服务
	public void start() {
		try {
			System.out.println("正在启动服务器……");
			serverSocket = new ServerSocket(8888);
			isRunning = true;
			receive();
		} catch (IOException e) {
			System.err.println("服务器启动失败！");
			stop();
		}
	}

	public void receive() {
		while (isRunning) {
			try {
				Socket client = serverSocket.accept();
				System.out.println("一个客户端建立了连接！");
				new Thread(new Dispatcher(client)).start();
			} catch (IOException e) {
				System.err.println("客户端错误！");
			}
		}
	}

	public void stop() {
		isRunning = false;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			System.err.println("服务端关闭错误！");
		}
	}
}
