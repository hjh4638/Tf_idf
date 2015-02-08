package TF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dto.ClientVector;
import dto.KnnResult;
import dto.TrainedVector;

public class KnnExecutor {
	SqlSessionFactory sqlSessionFactory;
	SqlSession session;
	String words[];
	int input_dimension;
	int vector_dimension;
	
	public KnnExecutor() throws IOException {
		String resource = "sqlMap/mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		session = sqlSessionFactory.openSession();
		
		words= new String[1001];
		
		input_dimension = 1000;
		vector_dimension = 360;
	}
	
	public void MechineLearning() throws IOException{
		File f1 = new File("tf_idf_group/vector.txt");
		
		FileReader r = new FileReader(f1);
		BufferedReader br = new BufferedReader(r);
		
		int p_size = 6;
		String line =null;
		String str[] = new String[10];
		TrainedVector vec = new TrainedVector();
		while((line = br.readLine()) !=null){
			str = line.split("/ /");
			vec.Set(str[0], str[2], str[1], Double.parseDouble(str[3]), Double.parseDouble(str[4]), Double.parseDouble(str[5]), Double.parseDouble(str[6]), null);
			session.insert("test.insert1", vec);
		}
		session.commit();
		br.close();
	}
	
	public String getRealOutput(TF[] TFList) throws IOException{
			session.delete("test.reset2");
			session.commit();
			
			Integer id = session.selectOne("test.getClientSeq");
					
			ClientVector in2 = new ClientVector();
			TrainedVector vec = new TrainedVector();
			
			for(int i=0;i<TFList.length;i++){
				if(TFList[i].keyword.length()<50){
					try{
						in2.Set(id, null, TFList[i].keyword, (double)TFList[i].count, null);
					}
					catch(NumberFormatException e){
						System.out.println(TFList[i].keyword);
					}
					session.insert("test.insert2", in2);
				}
			}
			session.commit();
			
			List<KnnResult> dis = session.selectList("test.getKnnDistance", id);
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for(int i=0;i<dis.size();i++){
				String label = session.selectOne("test.getLabel",dis.get(i).getFile_name());
				dis.get(i).setLabel(label);
				String key = dis.get(i).getLabel();
				if(map.containsKey(key)){
					Integer count = map.get(key);
					map.put(key, count+1);
				}
				else{
					map.put(key, 1);
				}
				
			}
			Iterator it = sortByValue(map).iterator();
			String result = new String();
			while(it.hasNext()){
	            String temp = (String) it.next();
	            result = result + temp+"/"+map.get(temp)+";";
	        }
			return result;
	}
	 public static List sortByValue(final Map map){
	        List<String> list = new ArrayList();
	        list.addAll(map.keySet());
	         
	        Collections.sort(list,new Comparator(){
	             
	            public int compare(Object o1,Object o2){
	                Object v1 = map.get(o1);
	                Object v2 = map.get(o2);
	                 
	                return ((Comparable) v1).compareTo(v2);
	            }
	             
	        });
	        Collections.reverse(list); // 주석시 오름차순
	        return list;
	    }
}
