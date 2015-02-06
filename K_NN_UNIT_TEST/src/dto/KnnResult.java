package dto;

public class KnnResult {

	String file_name;
	String label;
	Double average;
	Double distance;
	
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}
	@Override
	public String toString() {
		System.out.println("label = "+this.label + " | "+"average = "+this.average + " | "+"distance = "+this.distance);
		return super.toString();
	}
	
	
}
