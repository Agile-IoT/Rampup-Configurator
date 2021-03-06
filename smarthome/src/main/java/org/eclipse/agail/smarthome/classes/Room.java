/*********************************************************************
* Copyright (c) 2017-11-28 Christoph Uran (TU Graz)
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.agail.smarthome.classes;

import java.util.ArrayList;

public class Room {
	private int							id;
	private String						type = "";					// Bathroon, Bedroom, Corridor, Kitchen, Livingroom, Other
	private ArrayList<HomeAppliance>	homeAppliances = null;
	
	public Room(String roomType) {
		this.type = roomType;
	}
	public Room(int id) {
		this.id = id;
	}
	
	public void addHomeAppliance(String homeApplianceType) {
		if (homeAppliances == null) {
			homeAppliances = new ArrayList<HomeAppliance>();
		}
		homeAppliances.add(new HomeAppliance(homeApplianceType));
	}
	public void addHomeAppliance(int id) {
		if (homeAppliances == null) {
			homeAppliances = new ArrayList<HomeAppliance>();
		}
		homeAppliances.add(new HomeAppliance(id));
	}
	public void addHomeAppliance(HomeAppliance homeAppliance) {
		if (homeAppliances == null) {
			homeAppliances = new ArrayList<HomeAppliance>();
		}
		homeAppliances.add(homeAppliance);
	}
	public void setHomeApplianceAlwaysOn(String homeApplianceAlwaysOn) {
		homeAppliances.get(homeAppliances.size() - 1).setAlwaysOn(homeApplianceAlwaysOn);
	}
	public void setHomeApplianceDangerous(String homeApplianceDangerous) {
		homeAppliances.get(homeAppliances.size() - 1).setDangerous(homeApplianceDangerous);
	}
	
	public int getNumOfHomeAppliances() {
		if (homeAppliances != null)
			return homeAppliances.size();
		else
			return 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public ArrayList<HomeAppliance> getHomeAppliances() {
		return homeAppliances;
	}
}
