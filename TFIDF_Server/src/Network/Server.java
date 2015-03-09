package Network;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class Server extends Thread{
	public final static int PORT = 8080;
	public final static int MAX_USER = 100;
	public final static String TRAINNING_FILE_PATH = "C:\\TFIDF_trainning";
	
	ServerSocket serverSocket;
	LinkedList<User> userList;
	long lastValidUserCheckTime;
	
	Path trainningFilePath;
	
	public static SimpleDateFormat dateFormat;

	static {
		dateFormat = new SimpleDateFormat("[hh:mm:ss]");
	}
	
	public Server(){
		try {
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			InetAddress ip= InetAddress.getLocalHost();
			log("���� ���� IP: "+ip.getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		userList = new LinkedList<User>();
		lastValidUserCheckTime=System.currentTimeMillis();
		
		trainningFilePath = new File(TRAINNING_FILE_PATH).toPath();
		trainningFilePath.toFile().mkdir();
		
		log("���� ����");
	}
	
	public static void log(Object logMessage){
		System.out.println(timeStamp() + " " + logMessage);
	}
	
	public static String timeStamp(){
		return dateFormat.format(new Date());
	}
	
	public void run(){
		while (!serverSocket.isClosed() && serverSocket != null) {
			Socket socket = null;
			
			try {
				// �� ���� ��û�� ���� ������ ���
				socket = serverSocket.accept();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(userList.size() < MAX_USER){
					
				log("�� ���� ����"+socket.getInetAddress().toString());
				
				User user = new User(socket, userList);
				userList.add(user);
					
				user.connection.setDaemon(true);
				user.connection.start();
				
				log("���� ���� ���� ��:" + userList.size());
					
			}
			else{
				log("���� �ο� �ʰ��� �� �̻��� ������ ���� �� �����ϴ�.");
					
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}