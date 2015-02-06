package dto;

/**
 * @author HamJunhyuk
 *파일명, 단어, 그룹, tf, df, idf, tf_idf로 입력
 */
public class TrainedVector {
	private String file_name;
	private String keyword;
	private String label;
	private Double tf;
	private Double df;
	private Double idf;
	private Double tf_idf;
	private Integer seq;
	
	public void Set( String file_name, String keyword, String label,
			Double tf, Double df, Double idf, Double tf_idf, Integer seq	){
		this.file_name = file_name;
		this.keyword = keyword;
		this.label = label;
		this.tf = tf;
		this.df = df;
		this.idf = idf;
		this.tf_idf = tf_idf;
		this.seq = seq;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Double getTf() {
		return tf;
	}
	public void setTf(Double tf) {
		this.tf = tf;
	}
	public Double getDf() {
		return df;
	}
	public void setDf(Double df) {
		this.df = df;
	}
	public Double getIdf() {
		return idf;
	}
	public void setIdf(Double idf) {
		this.idf = idf;
	}
	public Double getTf_idf() {
		return tf_idf;
	}
	public void setTf_idf(Double tf_idf) {
		this.tf_idf = tf_idf;
	}
	public Integer getSeq() {
		return seq;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		System.out.println("file_name = " + this.file_name);
		System.out.println("keyword = " + this.keyword);
		System.out.println("label = " + this.label);
		System.out.println("tf = " + this.tf);
		System.out.println("df = " + this.df);
		System.out.println("idf = " + this.idf);
		System.out.println("tf_idf = " + this.tf_idf);
		System.out.println("---------------------------");
		return super.toString();
	}
	
}
