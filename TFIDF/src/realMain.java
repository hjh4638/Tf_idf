
public class realMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		tfidf vector = new tfidf();
		vector.getTFIDF("C:/tf_samples/", "C:/tf_samples_output/");
		System.out.println("Finished");
	}
}