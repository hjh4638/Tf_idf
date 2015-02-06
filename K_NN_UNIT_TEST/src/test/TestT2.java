package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import dto.TrainedVector;

public class TestT2 {

	@Test
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
	
}
