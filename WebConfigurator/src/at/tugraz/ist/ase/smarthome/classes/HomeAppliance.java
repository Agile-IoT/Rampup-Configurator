package at.tugraz.ist.ase.smarthome.classes;

public class HomeAppliance {
	private String		type;			// Fridge, TV, Stove
	private boolean		isAlwaysOn,
						isDangerous;
	
	public HomeAppliance(String homeApplianceType) {
		this.type = homeApplianceType;
	}
	public void setAlwaysOn(String homeApplianceAlwaysOn) {
		if (homeApplianceAlwaysOn.equals("true")) {
			this.isAlwaysOn = true;
		} else {
			this.isAlwaysOn = false;
		}
	}
	public void setDangerous(String homeApplianceDangerous) {
		if (homeApplianceDangerous.equals("true")) {
			this.isDangerous = true;
		} else {
			this.isDangerous = false;
		}
	}
}
