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

public class UserRequirements {
	private int			id;
	private String		isComfortControlNeeded = "",
						isEnergySavingNeeded = "",
						isHealthSupportNeeded = "",
						isSafetySecurityNeeded = "",
						isCostImportant = "",
						isStabilityNeeded = "",
						isSensibleToElectricSmog = "";
	
	public void setComfortControlNeeded(String isComfortControlNeeded) {
		this.isComfortControlNeeded = isComfortControlNeeded;
	}

	public void setEnergySavingNeeded(String isEnergySavingNeeded) {
		this.isEnergySavingNeeded = isEnergySavingNeeded;
	}

	public void setHealthSupportNeeded(String isHealthSupportNeeded) {
		this.isHealthSupportNeeded = isHealthSupportNeeded;
	}

	public void setSafetySecurityNeeded(String isSafetySecurityNeeded) {
		this.isSafetySecurityNeeded = isSafetySecurityNeeded;
	}

	public void setCostImportant(String isCostImportant) {
		this.isCostImportant = isCostImportant;
	}

	public void setStabilityNeeded(String isStabilityNeeded) {
		this.isStabilityNeeded = isStabilityNeeded;
	}

	public void setSensibleToElectricSmog(String isSensibleToElectricSmog) {
		this.isSensibleToElectricSmog = isSensibleToElectricSmog;
	}

	public void setInstallation(String installation) {
		this.installation = installation;
	}

	private SmartHome	smartHome;
	private String		installation = "";				// DIY, Professional
	
	private static ArrayList<String> installationTypeList = new ArrayList<String>() {
		private static final long serialVersionUID = 9148271970660121738L;
		{
			add("diy");
			add("professional");
		}
	};
	
	public void populateIds() {
		int currentRoomId = 11;
		int currentHomeApplianceId = 21;
		
		smartHome.setId(1);
		this.id = 2;
		
		if (smartHome.getRooms() != null) {
			for (Room room : smartHome.getRooms()) {
				room.setId(currentRoomId);
				currentRoomId++;
				
				if (room.getHomeAppliances() != null) {
					for (HomeAppliance homeAppliance : room.getHomeAppliances()) {
						homeAppliance.setId(currentHomeApplianceId);
						currentHomeApplianceId++;
					}
				}
			}
		}
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setSmartHome(SmartHome smartHome) {
		this.smartHome = smartHome;
	}
	
	public int getId() {
		return id;
	}

	public String isComfortControlNeeded() {
		return isComfortControlNeeded;
	}

	public String isEnergySavingNeeded() {
		return isEnergySavingNeeded;
	}

	public String isHealthSupportNeeded() {
		return isHealthSupportNeeded;
	}

	public String isSafetySecurityNeeded() {
		return isSafetySecurityNeeded;
	}

	public String isCostImportant() {
		return isCostImportant;
	}

	public String isStabilityNeeded() {
		return isStabilityNeeded;
	}

	public String isSensibleToElectricSmog() {
		return isSensibleToElectricSmog;
	}

	public SmartHome getSmartHome() {
		return smartHome;
	}

	public String getInstallation() {
		return installation;
	}

	public static ArrayList<String> getInstallationTypeList() {
		return installationTypeList;
	}
}
