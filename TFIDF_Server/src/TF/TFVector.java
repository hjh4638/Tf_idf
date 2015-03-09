package TF;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class TFVector extends ArrayList<TF>{ //클라이언트의 것과 다름
	public int ID;
	public String fileName;
	public String filePath;
	public String label;//이게 필요없을걸?

	
	public TFVector(){
		super();
	}
	
	public TFVector(int id, String fn){
		this();
		
		ID = id;
		fileName = fn;
	}
}
