package at.tugraz.ist.ase.smarthome.classes;

import java.util.ArrayList;

public class Room {
	private String						type;					// Kitchen, Bathroom, Livingroom, Bedroom, Other
	private ArrayList<HomeAppliance>	homeAppliances = null;
	
	public Room(String roomType) {
		this.type = roomType;
	}
	
	public void addHomeAppliance(String homeApplianceType) {
		if (homeAppliances == null) {
			homeAppliances = new ArrayList<HomeAppliance>();
		}
		homeAppliances.add(new HomeAppliance(homeApplianceType));
	}
	public void setHomeApplianceAlwaysOn(String homeApplianceAlwaysOn) {
		homeAppliances.get(homeAppliances.size() - 1).setAlwaysOn(homeApplianceAlwaysOn);
	}
	public void setHomeApplianceDangerous(String homeApplianceDangerous) {
		homeAppliances.get(homeAppliances.size() - 1).setDangerous(homeApplianceDangerous);
	}
}
