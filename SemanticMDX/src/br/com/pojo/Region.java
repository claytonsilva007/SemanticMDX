package br.com.pojo;

public class Region {
	private int regionId;
	private String city;
	private String province;
	
	public Region(int regionId, String city, String province) {
		super();
		this.regionId = regionId;
		this.city = city;
		this.province = province;
	}
	public int getRegionId() {
		return regionId;
	}
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	
}
