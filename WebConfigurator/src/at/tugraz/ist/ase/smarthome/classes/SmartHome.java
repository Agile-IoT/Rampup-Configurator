package at.tugraz.ist.ase.smarthome.classes;

import java.util.ArrayList;

public class SmartHome {
	private boolean				areTubesInstalled;
	private String				builtWith,				// Steel, Other
								communication;			// Wired, Wireless
	private int					age;
	private ArrayList<Room>		rooms = null;
	
	private static ArrayList<String> builtWithList = new ArrayList<String>() {
		private static final long serialVersionUID = 4143431222478394084L;
		{
			add("Steel");
			add("Other");
		}
	};
	private static ArrayList<String> communicationList = new ArrayList<String>() {
		private static final long serialVersionUID = 1139587724416181305L;
		{
			add("Wired");
			add("Wireless");
		}
	};
	
	public void setBuiltWith(String builtWith) {
		this.builtWith = builtWith;
	}
	public void setCommunication(String communication) {
		this.communication = communication;
	}
	public void setAreTubesInstalled(String areTubesInstalled) {
		if (areTubesInstalled.equals("true")) {
			this.areTubesInstalled = true;
		} else {
			this.areTubesInstalled = false;
		}
	}
	public void setAge(String age) {
		this.age = Integer.parseInt(age);
	}
	public void addRoom(String roomType) {
		if (rooms == null) {
			rooms = new ArrayList<Room>();
		}
		rooms.add(new Room(roomType));
	}
	public void addHomeAppliance(String homeApplianceType) {
		rooms.get(rooms.size() - 1).addHomeAppliance(homeApplianceType);
	}
	public void setHomeApplianceAlwaysOn(String homeApplianceAlwaysOn) {
		rooms.get(rooms.size() - 1).setHomeApplianceAlwaysOn(homeApplianceAlwaysOn);
	}
	public void setHomeApplianceDangerous(String homeApplianceDangerous) {
		rooms.get(rooms.size() - 1).setHomeApplianceDangerous(homeApplianceDangerous);
	}
	
	public boolean areTubesInstalled() {
		return areTubesInstalled;
	}
	public String getBuiltWith() {
		return builtWith;
	}
	public String getCommunication() {
		return communication;
	}
	public int getAge() {
		return age;
	}
	public ArrayList<String> getBuiltWithList() {
		return builtWithList;
	}
	public ArrayList<String> getCommunicationList() {
		return communicationList;
	}
}
