package dto;

/**
 * @author HamJunhyuk
 * id, 파일명, 단어, tf로 입력
 */
public class ClientVector {
	private Integer id;
	private String file_name;
	private String keyword;
	private Double tf;
	private Integer seq;
	
	public void Set(Integer id, String file_name, String keyword, Double tf, Integer seq) {
		this.id = id;
		this.file_name = file_name;
		this.keyword = keyword;
		this.tf = tf;
		this.seq = seq;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Double getTf() {
		return tf;
	}
	public void setTf(Double tf) {
		this.tf = tf;
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
		System.out.println("id = " + this.id);
		System.out.println("file_name = " + this.file_name);
		System.out.println("keyword = " + this.keyword);
		System.out.println("tf = " + this.tf);
		System.out.println("---------------------------");
		return super.toString();
	}
}
