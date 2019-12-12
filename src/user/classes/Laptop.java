package user.classes;

public class Laptop {
	Integer laptopId, laptopCategoryId;
	String model;
	public Integer getLaptopId() {
		return laptopId;
	}
	public void setLaptopId(Integer laptopId) {
		this.laptopId = laptopId;
	}
	
	public Integer getLaptopCategoryId() {
		return laptopCategoryId;
	}
	public void setLaptopCategoryId(Integer laptopCategoryId) {
		this.laptopCategoryId = laptopCategoryId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	@Override
	public String toString() {
		return "Laptop [model=" + model + "]";
	}
	
}
