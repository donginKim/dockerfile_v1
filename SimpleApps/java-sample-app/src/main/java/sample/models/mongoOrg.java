package sample.models;

public class mongoOrg {

	private String id;
	
	private String label;
	
	private String desc;



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getLabel() {
		return label;
	}



	public void setLabel(String label) {
		this.label = label;
	}



	public String getDesc() {
		return desc;
	}



	public void setDesc(String desc) {
		this.desc = desc;
	}



	@Override
	public String toString() {
		return "State [id=" + id + ", label=" + label + ", desc="
				+ desc + "]";
	}

}
