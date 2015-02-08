//package test;
//
//import static org.junit.Assert.assertNotEquals;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Random;
//
//import org.apache.ibatis.exceptions.PersistenceException;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import com.mysql.jdbc.Connection;
//
//import dto.ClientVector;
//import dto.KnnResult;
//import dto.TrainedVector;
//
//public class Test_tresh {
//	@Test
//	@Ignore
//	public void mybatisInitTest() throws IOException{
//		assertNotEquals(sqlSessionFactory, null);
//	}
//	@Test
//	@Ignore
//	public void jdbcConnectTest() throws SQLException{
//		Connection con = null;
//		con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/knn",
//					"root", "tkatjdthapa123");
//	}
//	@Test
//	@Ignore
//	public void selectTest(){
//		Integer in = session.selectOne("test.sel1");
//		List<ClientVector> li1 = session.selectList("test.sel2");
//		List<TrainedVector> li2 = session.selectList("test.sel3");
//		assertNotEquals(li1.size(), 0);
//		assertNotEquals(li2.size(), 0);
//	}
//	@Test(expected=PersistenceException.class)
//	@Ignore
//	public void insertNullTest(){
//		TrainedVector in1 = new TrainedVector();
//		ClientVector in2 = new ClientVector();
//		in1.Set("test1.txt", "apple" , "과일", null, null, null, null, null);
//		in2.Set(1, "asdfasf", null, null, null);
//		in1.Set(null, null, null, null, null, null, null, null);
//		in2.Set(null, null, null, null, null);
//		int size1 = session.insert("test.insert1", in1);
//		int size2 = session.insert("test.insert2", in2);
//		session.commit();
//	}
//	@Test(expected=PersistenceException.class)
//	@Ignore
//	public void insertDuplicatePk(){
//		TrainedVector in1 = new TrainedVector();
//		ClientVector in2 = new ClientVector();
//		in1.Set("test1.txt", "apple" , "과일", null, null, null, null, 4);
//		in2.Set(1, "asdfasf", null, null, 4);
//		int size1 = session.insert("test.insert1", in1);
//		int size2 = session.insert("test.insert2", in2);
//		session.commit();
//	}
//	@Test
//	@Ignore
//	public void insertRandomTf() throws IOException{
//		ClientVector in2 = new ClientVector();
//		String str[] = getRandomWord();
//		Random random = new Random();
//		for(int i=0;i<input_dimension;i++){
//			double ran_tf =  random.nextInt(1000);
//			in2.Set(10, "testTF1.txt", str[i], ran_tf, null);
//			session.insert("test.insert2", in2);
//		}
//		session.commit();
//	}
//	@Test
//	@Ignore
//	public void insertRandomTrained() throws IOException{
//		TrainedVector in1 = new TrainedVector();
//		String str[] = getRandomWord();
//		Random random = new Random();
//		int flag = 1;
//		for(int j = 0; j <300;j++){
//			for(int i=0;i<vector_dimension;i++){
//				double ran_tf =  random.nextInt(1000);
//				double ran_idf = random.nextDouble();
//				String name = "Trained"+flag+".txt";
//				in1.Set(name, str[i], null,ran_tf, null,ran_idf , ran_tf*ran_idf, null);
//				session.insert("test.insert1", in1);
//			}
//			flag++;
//		}
//		session.commit();
//	}
//	public void setWord() throws IOException{
//		File word_list = new File("words.txt");
//		FileReader file_reader = new FileReader(word_list);
//		BufferedReader reader = new BufferedReader(file_reader);
//		String line = null;
//		
//		int count =0;
//		while((line=reader.readLine())!=null){
//			words[count]=line;
//			count++;
//			if(count>1000){
//				break;
//			}
//		}
//		reader.close();
//	}
//	public String[] getRandomWord() throws IOException{
//		setWord();
//		String str[] = new String[input_dimension];
//		Random random = new Random();
//		for(int i = 0; i<input_dimension;i++){
//			int ran = random.nextInt(1000);
//			str[i] = words[ran];
//		}
//		return str;
//	}
//	@Test
//	@Ignore
//	public void getID(){
//		Integer id = session.selectOne("test.getClientSeq");
//		Integer in = session.selectOne("test.sel1");
//		List<TrainedVector> a = session.selectList("test.sel3");
//		a.get(0).toString();
//		System.out.println(id);
//		System.out.println(in);
//	}
//	@Test
//	@Ignore
//	public void getLineFileAndInsertInput() throws IOException{
//		session.delete("test.reset2");
//		session.commit();
//		
//		Integer id = session.selectOne("test.getClientSeq");
//		
//		
//		File f1 = new File("input/input0.txt");
//	
//		FileReader r = new FileReader(f1);
//		BufferedReader br = new BufferedReader(r);
//		ClientVector in2 = new ClientVector();
//		
//		String label = new String();
//		int p_size = 6;
//		String line =null;
//		String str[] = new String[10];
//		TrainedVector vec = new TrainedVector();
//		
//		
//		while((line = br.readLine()) !=null){
//			str = line.split("/ /");
//			label = str[1];
//			try{
//				in2.Set(id, str[0], str[2], Double.parseDouble(str[3]), null);
//			}
//			catch(NumberFormatException e){
//				System.out.println(str);
//			}
//			session.insert("test.insert2", in2);
//		}
//		session.commit();
//		
//		int num = 0;
//		FileWriter fw = new FileWriter("output/output"+num+".txt", true);
//		BufferedWriter bw = new BufferedWriter(fw);
//		System.out.println("input의 원래 그룹 = " +label);
//		System.out.println("------------------------------------------------------------------------");
//		System.out.println("avg list");
//		List<KnnResult> avg = session.selectList("test.getKnnAvg", id);
//		String test =avg.toString();
//		System.out.println("------------------------------------------------------------------------");
//		System.out.println("dis list");
//		List<KnnResult> dis = session.selectList("test.getKnnDistance", id);
//		dis.toString();
//		System.out.println("------------------------------------------------------------------------");
//		System.out.println();
//	
//		br.close();
//	}
//	@Test
//	@Ignore
//	public void getOutput() throws IOException{
//		int num = 0;
//		FileWriter fw = new FileWriter("output/output.txt", true);
//		BufferedWriter bw = new BufferedWriter(fw);
//		
//		for(int ou =0 ;ou<1;ou++){
//			session.delete("test.reset2");
//			session.commit();
//			
//			Integer id = session.selectOne("test.getClientSeq");
//			
//			
//			File f1 = new File("input/input"+num+".txt");
//		
//			FileReader r = new FileReader(f1);
//			BufferedReader br = new BufferedReader(r);
//			ClientVector in2 = new ClientVector();
//			
//			String label = new String();
//			String file_name = new String();
//			int p_size = 6;
//			String line =null;
//			String str[] = new String[10];
//			TrainedVector vec = new TrainedVector();
//			
//			
//			while((line = br.readLine()) !=null){
//				str = line.split("/ /");
//				label = str[1];
//				file_name = str[0];
//				try{
//					in2.Set(id, str[0], str[2], Double.parseDouble(str[3]), null);
//				}
//				catch(NumberFormatException e){
//					System.out.println(str);
//				}
//				session.insert("test.insert2", in2);
//			}
//			session.commit();
//			
//			bw.write("filename = " +file_name);
//			bw.write(" , 원래 그룹 = " +label); bw.newLine();
//			bw.write("Distance List");
//			bw.newLine();
//			List<KnnResult> dis = session.selectList("test.getKnnDistance", id);
//			HashMap<String, Integer> map = new HashMap<String, Integer>();
//			for(int i=0;i<dis.size();i++){
//				dis.get(i).setLabel(session.selectOne("test.getLabel",dis.get(i).getFile_name()));
//				String key = dis.get(i).getLabel();
//				if(map.containsKey(key)){
//					Integer count = map.get(key);
//					map.put(key, count+1);
//				}
//				else{
//					map.put(key, 1);
//				}
//				
//				bw.write("label = "+dis.get(i).getLabel() + " | "+"average = "+dis.get(i).getAverage() + " | "+"distance = "+dis.get(i).getDistance());
//				bw.newLine();
//			}
//			bw.write("------------------------------------------------------------------------"); 
//			bw.newLine();
//	       
//			Iterator it = TestT3.sortByValue(map).iterator();
//			String high_label = new String();
//			if(it.hasNext()){
//				high_label = (String) it.next();
//				bw.write("Label = " + high_label + " | count = " + map.get(high_label));
//		        bw.newLine();
//			}
//			while(it.hasNext()){
//	            String temp = (String) it.next();
//	            bw.write("Label = " + temp + " | count = " + map.get(temp));
//	            bw.newLine();
//	        }	
//			if(label.equals(high_label)){
//				bw.write("분류가 성공하였습니다");
//	            bw.newLine();
//			}
//			else{
//				bw.write("분류가 실패하였습니다");
//	            bw.newLine();
//			}
//			bw.write("------------------------------------------------------------------------"); 
//			bw.newLine();
//			bw.newLine();
//			bw.flush();
//			br.close();
//			
//			num++;
//		}
//		
//	}
//}
