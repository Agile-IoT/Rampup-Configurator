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

public class MonitoringStation {
	private int									id;
	private String								communication = "",
												localStorage = "",
												cloudStorage = "",
												enclosure = "";
	private ArrayList<DeploymentEnvironment>	deploymentEnvironments = null;
	
	public int getId() {
		return id;
	}
	public String getCommunication() {
		return communication;
	}
	public String getLocalStorage() {
		return localStorage;
	}
	public String getCloudStorage() {
		return cloudStorage;
	}
	public String getEnclosure() {
		return enclosure;
	}
	public ArrayList<DeploymentEnvironment> getDeploymentEnvironments() {
		return deploymentEnvironments;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setCommunication(String communication) {
		this.communication = communication;
	}
	public void setLocalStorage(String localStorage) {
		this.localStorage = localStorage;
	}
	public void setCloudStorage(String cloudStorage) {
		this.cloudStorage = cloudStorage;
	}
	public void setEnclosure(String enclosure) {
		this.enclosure = enclosure;
	}
	public void addDeploymentEnvironment(DeploymentEnvironment deploymentEnvironment) {
		if (deploymentEnvironments == null) {
			deploymentEnvironments = new ArrayList<DeploymentEnvironment>();
		}
		deploymentEnvironments.add(deploymentEnvironment);
	}

	public void populateIds() {
		int currentDeploymentEnvironmentId = 2;
		int currentAreaId = deploymentEnvironments.size() + 2;
		int currentEnvironmentalConditionId = 1000;
		int currentWallTypeId = 2000;
//		int currentPollutantId = 3000;
//		int currentInterferingPollutantId = 4000;
//		int currentSensorId = 5000;
//		int currentReferenceModelId = 6000;
//		int currentMonitoringPolicyId = 7000;
		
		this.id = 1;
		
		if (deploymentEnvironments == null) {
			deploymentEnvironments = new ArrayList<DeploymentEnvironment>();
		}
		
		for (DeploymentEnvironment deploymentEnvironment : deploymentEnvironments) {
			deploymentEnvironment.setId(currentDeploymentEnvironmentId);
			currentDeploymentEnvironmentId++;
			
			for (Area area : deploymentEnvironment.getAreas()) {
				area.setId(currentAreaId);
				currentAreaId++;
				
				for (EnvironmentalCondition environmentalCondition : area.getEnvironmentalConditions()) {
					environmentalCondition.setId(currentEnvironmentalConditionId);
					currentEnvironmentalConditionId++;
				}
				
				for (WallType wallType : area.getWallTypes()) {
					wallType.setId(currentWallTypeId);
					currentWallTypeId++;
				}
			}
			
//			for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
//				pollutant.setId(currentPollutantId);
//				currentPollutantId++;
//				
//				for (InterferingPollutant interferingPollutant : pollutant.getInterferingPollutants()) {
//					interferingPollutant.setId(currentInterferingPollutantId);
//					currentInterferingPollutantId++;
//				}
//				
//				for (Sensor sensor : pollutant.getSensors()) {
//					sensor.setId(currentSensorId);
//					currentSensorId++;
//					
//					for (ReferenceModel referenceModel : sensor.getReferenceModesl()) {
//						referenceModel.setId(currentReferenceModelId);
//						currentReferenceModelId++;
//					}
//					
//					sensor.getMonitoringPolicy().setId(currentMonitoringPolicyId);
//					currentMonitoringPolicyId++;
//				}
//			}
		}
	}
}
