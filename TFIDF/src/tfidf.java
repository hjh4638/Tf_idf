
public class tfidf {
	public native void getTFIDF(String filePath, String savedFilePath);
	
	static{
//		System.load("C:/Users/HamJunhyuk/workspace/TFIDF/dll/TFIDFVector.dll");
//	      System.setProperty("java.library.path", "C:/Users/HamJunhyuk/workspace/TFIDF/dll");
//			String libpath = System.getProperty("java.library.path");
//	      System.out.println(libpath);
		System.loadLibrary("TFIDFVector");
		
//		 try {
//		        System.loadLibrary("TFIDFVector");
//		    } catch (UnsatisfiedLinkError e) {
//		      System.err.println("Native code library failed to load.\n" + e);
//		    }
//			
//		    try {
//		        System.load("C:/Users/HamJunhyuk/workspace/TFIDF/dll/TFIDFVector.dll");
//		    } catch (UnsatisfiedLinkError e) {
//		      System.err.println("Native code library failed to load.\n" + e);
//		    }
	}
}
