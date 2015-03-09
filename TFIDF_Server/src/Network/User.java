package Network;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import TF.KnnExecutor;
import TF.TF;
import TF.TFVector;

public class User {
	static final char DATA_VECTOR_AND_ID = 'V';
	static final char DATA_VECTOR_AND_ID_start = 'S';
	static final char DATA_VECTOR_AND_ID_end = 'E';
	static final char DATA_LABELLIST_AND_ID = 'L';
	static final char DATA_FEEDBACK_AND_ID = 'F';
	static final char DATA_QUIT = 'Q';
	
	String myIP;
	Socket socket;
	Connection connection;
	DataInputStream in;
	DataOutputStream out;
	
	LinkedList<User> userList;
	
	ArrayList<TFVector> TFVectorList;
	Path tmpDirPath;
	
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
		
		TFVectorList = new ArrayList<TFVector>();
		
		if(tmpDirPath == null){
			try {
				tmpDirPath = Files.createTempDirectory("TFIDF");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("임시 폴더 생성 실패");
			}
			
			tmpDirPath.toFile().deleteOnExit();// 왜 살아있지? 안에 내용물이 있어서 그런듯
			System.out.println(tmpDirPath.toString());
		}
	}
	
	void closeAll(){
		userList.remove(this);
		TFVectorList.clear();
		tmpDirPath.toFile().delete();
		
		Server.log(myIP + " 퇴장");	
		Server.log("접속 중인 유저 수:" + userList.size());
		
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
		
		TFVector makeTFVector(TF[] TFList, int id){
			TFVector TFV = new TFVector();
			
			for(TF tf : TFList){
				TFV.add(new TF(tf.keyword, tf.count));
			}
			
			TFV.ID = id;
			TFV.fileName = myIP + new Date().getTime() + ".txt";
			
			return TFV;
		}
		
		void makeFile(TFVector TFV){
			FileWriter fileWriter = null;
			BufferedWriter bufferedWriter = null;
				
			try {
				fileWriter = new FileWriter(new File(tmpDirPath.toString() +"\\"+ TFV.fileName));
				System.out.println(tmpDirPath.toString() +"\\"+ TFV.fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			bufferedWriter = new BufferedWriter(fileWriter);
			
			try {
				bufferedWriter.write(TFV.size()+"");
				bufferedWriter.newLine();
				for(TF tf : TFV){
					bufferedWriter.write(tf.keyword + " " + tf.count);
					bufferedWriter.newLine();
					bufferedWriter.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//닫기
			try {
				fileWriter.close();
			} catch (IOException e) {}	
			try {
				bufferedWriter.close();
			} catch (IOException e) {}
			
			//return tmpDirPath;
		}
		
		void moveFile(TFVector TFV){
			System.out.println(TFV.filePath);
			System.out.println(Server.TRAINNING_FILE_PATH+"\\"+TFV.fileName);
			
			File dest = new File(Server.TRAINNING_FILE_PATH+"\\"+TFV.label);
			dest.mkdir();
			
			try {
				Files.move(Paths.get(TFV.filePath), 
						   Paths.get(dest + "\\" + TFV.fileName), 
						   StandardCopyOption.ATOMIC_MOVE
				);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		void sendLabelList(String received){
			//문서의 ID 추출
			if(received.indexOf(';') != -1){
				String ID = received.substring(1, received.indexOf(';'));
			
				//vector 추출
				received = received.substring(received.indexOf(';')+1);
				String[] TFVector = received.split(";");
				
				System.out.println("received.length: " + received.length());
				System.out.println("TFVector.length: " + TFVector.length);
				
				TF[] TFList = new TF[TFVector.length];
				int i=0;
				for(String tf : TFVector){
					String[] TF = tf.split(",");
					if(TF.length == 2){
						TFList[i++] = new TF(TF[0], Integer.parseInt(TF[1]));
					}
					else{
						System.out.println("TF split 오류: TF 하나 버림");
						for(String s : TF){
							System.out.println(s);
						}
					}
				}
				
				//labelList 생성
				KnnExecutor knn;
				String labelList = null;
				try {
					knn = new KnnExecutor();
					labelList = knn.getRealOutput(TFList);
				} catch (IOException e) {
					e.printStackTrace();
				}

				//전송
				if(labelList != null){
					try {
						out.writeUTF(DATA_LABELLIST_AND_ID + ID + ";" + labelList);
						out.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				//TFList를 TFVector로
				TFVector TFV = makeTFVector(TFList, Integer.parseInt(ID));
				
				//TFVector를 파일화
				makeFile(TFV);
				TFV.filePath = tmpDirPath.toString() +"\\"+ TFV.fileName;//makeFile(TFV).toString();
				
				//TFVectorList에 추가
				if(TFV.filePath != null){
					TFVectorList.add(TFV);
				}

			}
			else{//벡터가 전송되지 않았을 경우
				//labelList 대신 etc 전송
				String ID = received.substring(1);
				try {
					out.writeUTF(DATA_LABELLIST_AND_ID + ID + ";etc/100");
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}	
		}
		
		public void run(){
			String received;

			myIP=socket.getInetAddress().toString();// 슬라이드 포함해 저장됨 (/192.168.0.9)
			myIP=myIP.substring(1);// 슬라이드 제거
			
			try{
				while(!terminated){
					if((received = in.readUTF()) != null){
						
						System.out.println("received: "+received);
						
						if (received.charAt(0) == DATA_VECTOR_AND_ID){
							sendLabelList(received);						
						}
						else if(received.charAt(0) == DATA_VECTOR_AND_ID_start){
							String received2 = null;
							if((received2 = in.readUTF()) != null){
								if(received2.charAt(0) == DATA_VECTOR_AND_ID_end){
									
									System.out.println("received2: "+received2);
									
									received += received2.substring(1);
									sendLabelList(received);
								}
							}
						}
						else if(received.charAt(0) == DATA_FEEDBACK_AND_ID){
							//TFVector 파일을 학습자료 폴더로 이동
							String ID = received.substring(1, received.indexOf(';'));
							String label = received.substring(received.indexOf(';')+1, received.length());
							for(TFVector TFV : TFVectorList){
								if(TFV.ID == Integer.parseInt(ID)){
									TFV.label = label;
									moveFile(TFV);
									break;
								}
							}
							
						}
						else if(received.charAt(0) == DATA_QUIT){
							closeAll();
							terminate();
						}	
					}
				}
			}
			catch(Exception e){
				closeAll();
				terminate();
				e.printStackTrace();
			}
		}
	}
}