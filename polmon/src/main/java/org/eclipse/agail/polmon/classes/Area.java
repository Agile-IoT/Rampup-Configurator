/*********************************************************************
* Copyright (c) 2017-11-28 Christoph Uran (TU Graz)
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.agail.polmon.classes;

import java.util.ArrayList;

public class Area {
	private int									id;
	private String								type = "",
												category = "",
												prefabricatedBuilding = "",
												vehicleTraffic = "",
												industrialType = "",
												pollutedSoil = "",
												floor = "",
												controlledArea = "",
												airConditioning = "",
												heatingSystem = "",
												windows = "",
												smokePresence = "",
												moldPresence = "",
												dustyArea = "";
	private ArrayList<EnvironmentalCondition>	environmentalConditions = null;
	private ArrayList<WallType>					wallTypes = null;
	
	public Area(int id) {
		this.id = id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public String getCategory() {
		return category;
	}
	public String getPrefabricatedBuilding() {
		return prefabricatedBuilding;
	}
	public String getVehicleTraffic() {
		return vehicleTraffic;
	}
	public String getIndustrialType() {
		return industrialType;
	}
	public String getPollutedSoil() {
		return pollutedSoil;
	}
	public String getFloor() {
		return floor;
	}
	public String getControlledArea() {
		return controlledArea;
	}
	public String getAirConditioning() {
		return airConditioning;
	}
	public String getHeatingSystem() {
		return heatingSystem;
	}
	public String getWindows() {
		return windows;
	}
	public String getSmokePresence() {
		return smokePresence;
	}
	public String getMoldPresence() {
		return moldPresence;
	}
	public String getDustyArea() {
		return dustyArea;
	}
	public ArrayList<EnvironmentalCondition> getEnvironmentalConditions() {
		if (environmentalConditions == null) {
			environmentalConditions = new ArrayList<EnvironmentalCondition>();
		}
		return environmentalConditions;
	}
	public ArrayList<WallType> getWallTypes() {
		if (wallTypes == null) {
			wallTypes = new ArrayList<WallType>();
		}
		return wallTypes;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public void setPrefabricatedBuilding(String prefabricatedBuilding) {
		this.prefabricatedBuilding = prefabricatedBuilding;
	}
	public void setVehicleTraffic(String vehicleTraffic) {
		this.vehicleTraffic = vehicleTraffic;
	}
	public void setIndustrialType(String industrialType) {
		this.industrialType = industrialType;
	}
	public void setPollutedSoil(String pollutedSoil) {
		this.pollutedSoil = pollutedSoil;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public void setControlledArea(String controlledArea) {
		this.controlledArea = controlledArea;
	}
	public void setAirConditioning(String airConditioning) {
		this.airConditioning = airConditioning;
	}
	public void setHeatingSystem(String heatingSystem) {
		this.heatingSystem = heatingSystem;
	}
	public void setWindows(String windows) {
		this.windows = windows;
	}
	public void setSmokePresence(String smokePresence) {
		this.smokePresence = smokePresence;
	}
	public void setMoldPresence(String moldPresence) {
		this.moldPresence = moldPresence;
	}
	public void setDustyArea(String dustyArea) {
		this.dustyArea = dustyArea;
	}
	public void addEnvironmentalCondition(EnvironmentalCondition environmentalCondition) {
		if (environmentalConditions == null) {
			environmentalConditions = new ArrayList<EnvironmentalCondition>();
		}
		environmentalConditions.add(environmentalCondition);
	}
	public void addWallType(WallType wallType) {
		if (wallTypes == null) {
			wallTypes = new ArrayList<WallType>();
		}
		wallTypes.add(wallType);
	}
}
