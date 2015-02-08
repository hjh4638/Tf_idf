package preprocessing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.PatternSyntaxException;

	public class TFVectorizer {

	public static ArrayList<TF> vectorize(ArrayList<String>strList){
		ArrayList<TF> TFList = new ArrayList<TF>();
		
		//º¤ÅÍÈ­
		for(String str : strList){
			boolean flag = false;
			
			for(TF tf : TFList){
				try{
					if(tf.keyword.equals(str)){
						tf.count++;
						flag=true;
						break;
					}
				}catch(PatternSyntaxException e){
					break;
				}
			}
			
			if(flag==false){
				TFList.add(new TF(str, 1));			
			}
		}
		
		//Á¤·Ä
		Collections.sort(TFList);
		
		return TFList;
	}
}
