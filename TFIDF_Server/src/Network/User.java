package Network;
import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Random;

import dto.KnnResult;
import TF.KnnExecutor;
import TF.TF;

public class User {
	static final char DATA_VERCTOR_AND_ID = 'V';
	static final char DATA_LABELLIST_AND_ID = 'L';
	static final char DATA_QUIT = 'Q';
	
	String myIP;
	Socket socket;
	Connection connection;
	DataInputStream in;
	DataOutputStream out;
	
	LinkedList<User> userList;
	
	User(Socket s, LinkedList<User> ul){
		socket=s;
		connection= new Connection();
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		userList = ul;
	}
	
	void closeAll(){
		userList.remove(this);
		
		Server.log(myIP + " ����");	
		Server.log("���� ���� ���� ��:" + userList.size());
		
		try {
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//inner class
	class Connection extends Thread{
		
		boolean terminated;
		
		Connection(){
			terminated=false;
		}
		
		public void terminate(){
			terminated=true;
		}
		
		public void run(){
			String received;

			myIP=socket.getInetAddress().toString();// �����̵� ������ ����� (/192.168.0.9)
			myIP=myIP.substring(1);// �����̵� ����
			
			try{
				while(!terminated){
					if((received = in.readUTF()) != null){
						
						System.out.println(received);
						if (received.charAt(0) == DATA_VERCTOR_AND_ID){
							//������ ID ����
							String ID = received.substring(1, received.indexOf(';'));
							
							//vector ����
							received = received.substring(received.indexOf(';')+1);
							String[] TFVector = received.split(";");
							
							System.out.println("received.length: " + received.length());
							System.out.println("TFVector.length: " + TFVector.length);
							
							
							TF[] TFList = new TF[TFVector.length];
							int i=0;
							for(String tf : TFVector){
								String[] TF = tf.split(",");
								System.out.println(TF[0] + " " + TF[1]);
								TFList[i++] = new TF(TF[0], Integer.parseInt(TF[1]));
								
								//System.out.println(TF[0] + " " + TF[1]);
							}
							///////////////////////////////////////////
							KnnExecutor knn = new KnnExecutor();
							//labellist ����
							String labelList = knn.getRealOutput(TFList);
							///////////////////////////////////////////
							/*
							Random rn = new Random();
							rn.setSeed(System.currentTimeMillis());
							int b = rn.nextInt(3);
							switch(b){
								case 0:
									labelList = "OS/40;DB/30;AI/30";
									break;
									
								case 1:
									labelList = "AI/60;graphic/30;network/10";
									break;
									
								case 2:
									labelList = "DB/100";
									break;
							}*/

							//����
							if(labelList != null){
								out.writeUTF(DATA_LABELLIST_AND_ID + ID + ";" + labelList);
								out.flush();
							}
							
						}
						else if(received.charAt(0) == DATA_QUIT){//�������ݿ� ���� �ٲ�
							closeAll();
							terminate();
						}	
					}
					else{//������ �Է��� ������
						yield();//�̰� ��ȿ�ϳ�? read�� ����ε�? �۵����ϴµ�
						Server.log("yield() �۵�");
					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}