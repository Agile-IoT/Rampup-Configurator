/*******************************************************************************
 * Copyright (C) 2017 TUGraz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     TUGraz - initial API and implementation
 ******************************************************************************/

package at.tugraz.ist.ase.smarthome.classes;

import java.util.ArrayList;

public class SmartHome {
	private int					id;
	private String				areTubesInstalled = "";
	private String				builtWith = "",				// Steel, Other, Wood
								communication = "";			// Wired, Wireless
	private ArrayList<Room>		rooms = null;
	
	public void setBuiltWith(String builtWith) {
		this.builtWith = builtWith;
	}
	public void setCommunication(String communication) {
		this.communication = communication;
	}
	public void setAreTubesInstalled(String areTubesInstalled) {
		this.areTubesInstalled = areTubesInstalled;
	}
	public void addRoom(String roomType) {
		if (rooms == null) {
			rooms = new ArrayList<Room>();
		}
		rooms.add(new Room(roomType));
	}
	public void addRoom(int roomId) {
		if (rooms == null) {
			rooms = new ArrayList<Room>();
		}
		rooms.add(new Room(roomId));
	}
	public void addRoom(Room room) {
		if (rooms == null) {
			rooms = new ArrayList<Room>();
		}
		rooms.add(room);
	}
	public void addHomeAppliance(String homeApplianceType) {
		rooms.get(rooms.size() - 1).addHomeAppliance(homeApplianceType);
	}
	public void addHomeAppliance(int homeApplianceId, int roomId) {
		for (Room room : rooms) {
			if (room.getId() == roomId) {
				room.addHomeAppliance(homeApplianceId);
			}
		}
	}
	public void setHomeApplianceAlwaysOn(String homeApplianceAlwaysOn) {
		if (rooms == null) {
			rooms = new ArrayList<Room>();
			rooms.add(new Room("Other"));
			rooms.get(rooms.size() - 1).addHomeAppliance("Other");
		}
		rooms.get(rooms.size() - 1).setHomeApplianceAlwaysOn(homeApplianceAlwaysOn);
	}
	public void setHomeApplianceDangerous(String homeApplianceDangerous) {
		rooms.get(rooms.size() - 1).setHomeApplianceDangerous(homeApplianceDangerous);
	}
	
	public String areTubesInstalled() {
		return areTubesInstalled;
	}
	public String getBuiltWith() {
		return builtWith;
	}
	public String getCommunication() {
		return communication;
	}
	
	public int getNumOfRooms() {
		return rooms.size();
	}
	public int getNumOfHomeAppliances() {
		int numOfHomeAppliances = 0;
		
		for (Room room : rooms) {
			numOfHomeAppliances += room.getNumOfHomeAppliances();
		}
		
		return numOfHomeAppliances;
	}
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	
	public boolean hasRoom(int id) {
		if (rooms == null)
			return false;
		else for (Room room : rooms) {
			if (room.getId() == id)
				return true;
		}
		return false;
	}
}
