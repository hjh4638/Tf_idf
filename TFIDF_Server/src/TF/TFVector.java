package TF;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class TFVector extends ArrayList<TF>{ //Ŭ���̾�Ʈ�� �Ͱ� �ٸ�
	public int ID;
	public String fileName;
	public String filePath;
	public String label;//�̰� �ʿ������?

	
	public TFVector(){
		super();
	}
	
	public TFVector(int id, String fn){
		this();
		
		ID = id;
		fileName = fn;
	}
}
