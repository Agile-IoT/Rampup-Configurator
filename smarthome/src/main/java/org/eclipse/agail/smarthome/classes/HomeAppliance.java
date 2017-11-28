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

public class HomeAppliance {
	private int			id;
	private String		type = "";			// Computer, Console, Dishwasher, Freezer, Fridge, Microwave, Oven, Radio, Router, Server, Stove, TV, WashingMachine
	private String		isAlwaysOn = "",
						isDangerous = "";
	
	public HomeAppliance(String homeApplianceType) {
		this.type = homeApplianceType;
	}
	public HomeAppliance(int id) {
		this.id = id;
	}
	public void setAlwaysOn(String homeApplianceAlwaysOn) {
		this.isAlwaysOn = homeApplianceAlwaysOn;
	}
	public String isAlwaysOn() {
		if (isAlwaysOn == null)
			return "";
		else
			return isAlwaysOn;
	}
	public void setDangerous(String homeApplianceDangerous) {
		this.isDangerous = homeApplianceDangerous;
	}
	public String isDangerous() {
		if (isDangerous == null)
			return "";
		else
			return isDangerous;
	}
	public void setType(String type) {
		this.type = type;
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
}
