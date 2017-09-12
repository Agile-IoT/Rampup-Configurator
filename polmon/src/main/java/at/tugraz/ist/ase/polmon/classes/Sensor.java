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

package at.tugraz.ist.ase.polmon.classes;

import java.util.ArrayList;

public class Sensor {
	private int							id;
	private String						type = "",
										technology = "",
										sensitivity = "",
										pod = "",
										range = "",
										resolution = "",
										responseTime = "",
										expectedOperationLife = "",
										minOpTemperature = "",
										maxOpTemperature = "",
										minOpHumidity = "",
										maxOpHumidity = "",
										minOpPressure = "",
										maxOpPressure = "",
										upperLimit = "",
										dualSensor = "",
										noise = "";
	private MonitoringPolicy			monitoringPolicy = null;
	private ArrayList<ReferenceModel>	referenceModels = null;
	
	public Sensor(int id) {
		this.id = id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public MonitoringPolicy getMonitoringPolicy() {
		if (monitoringPolicy == null) {
			monitoringPolicy = new MonitoringPolicy();
		}
		return monitoringPolicy;
	}
	public ArrayList<ReferenceModel> getReferenceModesl() {
		if (referenceModels == null) {
			referenceModels = new ArrayList<ReferenceModel>();
		}
		return referenceModels;
	}
	public int getId() {
		return id;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public void setSensitivity(String sensitivity) {
		this.sensitivity = sensitivity;
	}
	public void setPod(String pod) {
		this.pod = pod;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}
	public void setExpectedOperationLife(String expectedOperationLife) {
		this.expectedOperationLife = expectedOperationLife;
	}
	public void setMinOpTemperature(String minOpTemperature) {
		this.minOpTemperature = minOpTemperature;
	}
	public void setMaxOpTemperature(String maxOpTemperature) {
		this.maxOpTemperature = maxOpTemperature;
	}
	public void setMinOpHumidity(String minOpHumidity) {
		this.minOpHumidity = minOpHumidity;
	}
	public void setMaxOpHumidity(String maxOpHumidity) {
		this.maxOpHumidity = maxOpHumidity;
	}
	public void setMinOpPressure(String minOpPressure) {
		this.minOpPressure = minOpPressure;
	}
	public void setMaxOpPressure(String maxOpPressure) {
		this.maxOpPressure = maxOpPressure;
	}
	public void setUpperLimit(String upperLimit) {
		this.upperLimit = upperLimit;
	}
	public void setDualSensor(String dualSensor) {
		this.dualSensor = dualSensor;
	}
	public void setNoise(String noise) {
		this.noise = noise;
	}
	public void addReferenceModel(ReferenceModel referenceModel) {
		if (referenceModels == null) {
			referenceModels = new ArrayList<ReferenceModel>();
		}
		referenceModels.add(referenceModel);
	}
	public void setMonitoringPolicy(MonitoringPolicy monitoringPolicy) {
		this.monitoringPolicy = monitoringPolicy;
	}
}
