package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Ignore;
import org.junit.Test;

public class TestT2 {

	@Test
	@Ignore
	public void makeInput() throws IOException{
		int num = 0;
		File f1 = new File("tf_idf_group/vector.txt");
		FileReader r = new FileReader(f1);
		BufferedReader br = new BufferedReader(r);
		
		String line =null;
		String ex_name = null;
		int count = 0;
		while((line = br.readLine()) != null){
			String name[] = line.split("/ /");
			if(ex_name == null)
				ex_name = name[0];
			else if(!ex_name.equals(name[0])){
				num++;
				ex_name = name[0];
			}
			FileWriter fw = new FileWriter("input/input"+num+".txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(line);
			bw.newLine();
			bw.flush();
			
			count++;
		}
		System.out.println("count = "+ count);
		
		
	}
	@Test
	public void makeRealInput() throws IOException{
		String dir = "RealData";
		File f1 = new File(dir);
		String a[] = f1.list();
		String path[] = new String[a.length];
		int f_num = 0;
		
		for(int i=0;i<a.length;i++){
			path[i] = dir + "/" + a[i];
		}
		for(int i=0;i<path.length;i++){
			String[] flist = new File(path[i]).list();
			
			for(int j=0;j<flist.length;j++){
				File ff = new File(path[i]+"/"+flist[j]);
				FileReader r = new FileReader(ff);
				BufferedReader br = new BufferedReader(r);
				
				FileWriter fw = new FileWriter("input/input"+f_num+".txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				String line = null;
				bw.write(a[i]);
				bw.newLine();
				while((line = br.readLine())!=null){
					bw.write(line);
					bw.newLine();
				}
				bw.flush();
				f_num++;
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
