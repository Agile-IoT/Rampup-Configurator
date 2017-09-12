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
