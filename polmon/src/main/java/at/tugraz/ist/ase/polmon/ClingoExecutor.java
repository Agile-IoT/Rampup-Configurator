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

package at.tugraz.ist.ase.polmon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import at.tugraz.ist.ase.polmon.classes.Area;
import at.tugraz.ist.ase.polmon.classes.DeploymentEnvironment;
import at.tugraz.ist.ase.polmon.classes.EnvironmentalCondition;
import at.tugraz.ist.ase.polmon.classes.InterferingPollutant;
import at.tugraz.ist.ase.polmon.classes.MonitoringPolicy;
import at.tugraz.ist.ase.polmon.classes.MonitoringStation;
import at.tugraz.ist.ase.polmon.classes.Pollutant;
import at.tugraz.ist.ase.polmon.classes.ReferenceModel;
import at.tugraz.ist.ase.polmon.classes.Sensor;
import at.tugraz.ist.ase.polmon.classes.WallType;

public class ClingoExecutor {
	private String pathToClingo;
	private String pathToProgram;
	private int numOfResults;

	public ClingoExecutor(String pathToClingo, String pathToProgram, int numOfResults) {
		this.pathToClingo = pathToClingo;
		this.pathToProgram = pathToProgram;
		this.numOfResults = numOfResults;
	}

	public ArrayList<MonitoringStation> executeWithUserRequirements(MonitoringStation userRequirements) {
		// get program
		String basicProgram = getClingoProgram();
		// make program out of user requirements
		String userReqProgram = generateUserReqProgram(userRequirements);
		String finalProgram = basicProgram.replaceAll("% PARSER_UserRequirements", userReqProgram);

		// call executeClingo with program
		String clingoOutput = executeClingo(finalProgram);
		
		return parseClingoOutput(clingoOutput);
	}
	
	private String getClingoProgram() {
		String result = "";
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			String currentLine;
			
			fr = new FileReader(pathToProgram);
			br = new BufferedReader(fr);
			
			while ((currentLine = br.readLine()) != null) {
				result += currentLine + "\n";
			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not find file " + pathToProgram);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not read file " + pathToProgram);
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				System.err.println("Could not close file " + pathToProgram);
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	private String generateUserReqProgram(MonitoringStation userRequirements) {
		String program = "";
		
		// monitoring station
		if (!userRequirements.getCommunication().equals(""))
			program += "attributevalue_type_monitoringstation_communication(" + userRequirements.getId() + "," + userRequirements.getCommunication() + ").\n";
		if (!userRequirements.getLocalStorage().equals(""))
			program += "attributevalue_type_monitoringstation_localstorage(" + userRequirements.getId() + "," + userRequirements.getLocalStorage() + ").\n";
		if (!userRequirements.getCloudStorage().equals(""))
			program += "attributevalue_type_monitoringstation_cloudstorage(" + userRequirements.getId() + "," + userRequirements.getCloudStorage() + ").\n";
		if (!userRequirements.getEnclosure().equals(""))
			program += "attributevalue_type_monitoringstation_enclosure(" + userRequirements.getId() + "," + userRequirements.getEnclosure() + ").\n";
		
		// deployment environments
		for (DeploymentEnvironment deploymentEnvironment : userRequirements.getDeploymentEnvironments()) {
			program += "type_deploymentenvironment(" + deploymentEnvironment.getId() + ").\n";
			program += "assoc_type_monitoringstation_and_type_deploymentenvironment(" + userRequirements.getId() + "," + deploymentEnvironment.getId() + ").\n";
			if (!deploymentEnvironment.getType().equals(""))
				program += "attributedomain_type_deploymentenvironment_type(" + deploymentEnvironment.getId() + "," + deploymentEnvironment.getType() + ").\n";
			if (!deploymentEnvironment.getContext().equals(""))
				program += "attributedomain_type_deploymentenvironment_context(" + deploymentEnvironment.getId() + "," + deploymentEnvironment.getContext() + ").\n";
			if (!deploymentEnvironment.getLocationType().equals(""))
				program += "attributedomain_type_deploymentenvironment_locationtype(" + deploymentEnvironment.getId() + "," + deploymentEnvironment.getLocationType() + ").\n";
			
			// areas
			for (Area area : deploymentEnvironment.getAreas()) {
				program += "type_area(" + area.getId() + ").\n";
				program += "assoc_type_deploymentenvironment_and_type_area(" + deploymentEnvironment.getId() + "," + area.getId() + ").\n";
				
				if (!area.getType().equals(""))
					program += "attributevalue_type_area_type(" + area.getId() + "," + area.getType() + ").\n";
				if (!area.getCategory().equals(""))
					program += "attributevalue_type_area_category(" + area.getId() + "," + area.getCategory() + ").\n";
				if (!area.getPrefabricatedBuilding().equals(""))
					program += "attributevalue_type_area_prefabricatedbuilding(" + area.getId() + "," + area.getPrefabricatedBuilding() + ").\n";
				if (!area.getVehicleTraffic().equals(""))
					program += "attributevalue_type_area_vehicletraffic(" + area.getId() + "," + area.getVehicleTraffic() + ").\n";
				if (!area.getIndustrialType().equals(""))
					program += "attributevalue_type_area_industrialtype(" + area.getId() + "," + area.getIndustrialType() + ").\n";
				if (!area.getPollutedSoil().equals(""))
					program += "attributevalue_type_area_pollutedsoil(" + area.getId() + "," + area.getPollutedSoil() + ").\n";
				if (!area.getFloor().equals(""))
					program += "attributevalue_type_area_floor(" + area.getId() + "," + area.getFloor() + ").\n";
				if (!area.getControlledArea().equals(""))
					program += "attributevalue_type_area_controlledarea(" + area.getId() + "," + area.getControlledArea() + ").\n";
				if (!area.getAirConditioning().equals(""))
					program += "attributevalue_type_area_airconditioning(" + area.getId() + "," + area.getAirConditioning() + ").\n";
				if (!area.getHeatingSystem().equals(""))
					program += "attributevalue_type_area_heatingsystem(" + area.getId() + "," + area.getHeatingSystem() + ").\n";
				if (!area.getWindows().equals(""))
					program += "attributevalue_type_area_windows(" + area.getId() + "," + area.getWindows() + ").\n";
				if (!area.getSmokePresence().equals(""))
					program += "attributevalue_type_area_smokepresence(" + area.getId() + "," + area.getSmokePresence() + ").\n";
				if (!area.getMoldPresence().equals(""))
					program += "attributevalue_type_area_moldpresence(" + area.getId() + "," + area.getMoldPresence() + ").\n";
				if (!area.getDustyArea().equals(""))
					program += "attributevalue_type_area_dustyarea(" + area.getId() + "," + area.getDustyArea() + ").\n";
				
				// environmental conditions
				for (EnvironmentalCondition environmentalCondition : area.getEnvironmentalConditions()) {
					program += "type_environmetalconditions(" + environmentalCondition.getId() + ").\n";
					program += "assoc_type_area_and_type_environmetalconditions(" + area.getId() + "," + environmentalCondition.getId() + ").\n";
					if (!environmentalCondition.getHumidity().equals(""))
						program += "attributedomain_type_environmetalconditions_humidity(" + environmentalCondition.getId() + "," + environmentalCondition.getHumidity() + ").\n";
					if (!environmentalCondition.getWindSpeed().equals(""))
						program += "attributedomain_type_environmetalconditions_windspeed(" + environmentalCondition.getId() + "," + environmentalCondition.getWindSpeed() + ").\n";
					if (!environmentalCondition.getRain().equals(""))
						program += "attributedomain_type_environmetalconditions_rain(" + environmentalCondition.getId() + "," + environmentalCondition.getRain() + ").\n";
					if (!environmentalCondition.getDust().equals(""))
						program += "attributedomain_type_environmetalconditions_dust(" + environmentalCondition.getId() + "," + environmentalCondition.getDust() + ").\n";
					if (!environmentalCondition.getAvgTemperature().equals(""))
						program += "attributedomain_type_environmetalconditions_averagetemperature(" + environmentalCondition.getId() + "," + environmentalCondition.getAvgTemperature() + ").\n";
					if (!environmentalCondition.getSnow().equals(""))
						program += "attributedomain_type_environmetalconditions_snow(" + environmentalCondition.getId() + "," + environmentalCondition.getSnow() + ").\n";
					if (!environmentalCondition.getIce().equals(""))
						program += "attributedomain_type_environmetalconditions_ice(" + environmentalCondition.getId() + "," + environmentalCondition.getIce() + ").\n";
					if (!environmentalCondition.getVibrations().equals(""))
						program += "attributedomain_type_environmetalconditions_vibrations(" + environmentalCondition.getId() + "," + environmentalCondition.getVibrations() + ").\n";
					if (!environmentalCondition.getAvgPressure().equals(""))
						program += "attributedomain_type_environmetalconditions_averagepressure(" + environmentalCondition.getId() + "," + environmentalCondition.getAvgPressure() + ").\n";
				}
				
				// wall types
				for (WallType wallType : area.getWallTypes()) {
					program += "type_walltype(" + wallType.getId() + ").\n";
					program += "assoc_type_area_and_type_walltype(" + area.getId() + "," + wallType.getId() + ").\n";
					if (!wallType.getWallpaper().equals(""))
						program += "attributevalue_type_walltype_wallpaper(" + wallType.getId() + "," + wallType.getWallpaper() + ").\n";
					if (!wallType.getPlasticCladding().equals(""))
						program += "attributevalue_type_walltype_plasticcladding(" + wallType.getId() + "," + wallType.getPlasticCladding() + ").\n";
					if (!wallType.getWoodenPanels().equals(""))
						program += "attributevalue_type_walltype_woodenpanels(" + wallType.getId() + "," + wallType.getWoodenPanels() + ").\n";
					if (!wallType.getMoquette().equals(""))
						program += "attributevalue_type_walltype_moquette(" + wallType.getId() + "," + wallType.getMoquette() + ").\n";
					if (!wallType.getTiles().equals(""))
						program += "attributevalue_type_walltype_tiles(" + wallType.getId() + "," + wallType.getTiles() + ").\n";
					if (!wallType.getPlaster().equals(""))
						program += "attributevalue_type_walltype_plaster(" + wallType.getId() + "," + wallType.getPlaster() + ").\n";
				}
			}
		}
		
		return program;
	}
	
	private String executeClingo(String program) {
		String result = "";
		
		try {
			ProcessBuilder processBuilder;
			if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
				processBuilder = new ProcessBuilder("cmd.exe", "/c", pathToClingo + " -n" + numOfResults);
			else
				processBuilder = new ProcessBuilder("/bin/sh", "-c", pathToClingo + " -n" + numOfResults);
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			
			writer.write(program + "\n");
			writer.flush();
			writer.close();
			
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				result += line + "\n";
				
			}
		} catch (IOException e) {
			System.err.println("IOException when trying to execute Clingo");
			e.printStackTrace();
		}
		
		return result;
	}
	
	private ArrayList<MonitoringStation> parseClingoOutput(String clingoOutput) {
		ArrayList<MonitoringStation> results = new ArrayList<MonitoringStation>();
		
		String[] answers = clingoOutput.split("Answer: ");
		
		for (int i = 1; i < answers.length; i++) {
			String[] answerLine = answers[i].split("\n");
			String[] answerParts = answerLine[1].split(" ");
			
			MonitoringStation monitoringStation = new MonitoringStation();
			
			// check for monitoring station
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("type_monitoringstation(")) {
					monitoringStation.setId(Integer.parseInt(getVariable(answerPart, 0)));
				}
				if (answerPart.startsWith("attributevalue_type_monitoringstation_communication(")) {
					monitoringStation.setCommunication(getVariable(answerPart, 1));
				}
				if (answerPart.startsWith("attributevalue_type_monitoringstation_enclosure(")) {
					monitoringStation.setEnclosure(getVariable(answerPart, 1));
				}
				if (answerPart.startsWith("attributevalue_type_monitoringstation_localstorage(")) {
					monitoringStation.setLocalStorage(getVariable(answerPart, 1));
				}
				if (answerPart.startsWith("attributevalue_type_monitoringstation_cloudstorage(")) {
					monitoringStation.setCloudStorage(getVariable(answerPart, 1));
				}
			}
			
			// check for deployment environments
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("assoc_type_monitoringstation_and_type_deploymentenvironment(")) {
					monitoringStation.addDeploymentEnvironment(new DeploymentEnvironment(Integer.parseInt(getVariable(answerPart, 1))));
				}
			}
			// check for areas
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("assoc_type_deploymentenvironment_and_type_area(")) {
					int deploymentEnvironmentId = Integer.parseInt(getVariable(answerPart, 0));
					int areaId = Integer.parseInt(getVariable(answerPart, 1));
					
					for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
						if (deploymentEnvironment.getId() == deploymentEnvironmentId) {
							deploymentEnvironment.addArea(new Area(areaId));
						}
					}
				}
			}
			// check for environmental conditions
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("assoc_type_area_and_type_environmetalconditions(")) {
					int areaId = Integer.parseInt(getVariable(answerPart, 0));
					int environmentalConditionId = Integer.parseInt(getVariable(answerPart, 1));
					
					for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
						for (Area area : deploymentEnvironment.getAreas()) {
							if (area.getId() == areaId) {
								area.addEnvironmentalCondition(new EnvironmentalCondition(environmentalConditionId));
							}
						}
					}
				}
			}
			// check for wall types
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("assoc_type_area_and_type_walltype(")) {
					int areaId = Integer.parseInt(getVariable(answerPart, 0));
					int wallTypeId = Integer.parseInt(getVariable(answerPart, 1));
					
					for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
						for (Area area : deploymentEnvironment.getAreas()) {
							if (area.getId() == areaId) {
								area.addWallType(new WallType(wallTypeId));
							}
						}
					}
				}
			}
			// check for pollutants
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("assoc_type_deploymentenvironment_and_type_pollutant(")) {
					int deploymentEnvironmentId = Integer.parseInt(getVariable(answerPart, 0));
					int pollutantId = Integer.parseInt(getVariable(answerPart, 1));
					
					for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
						if (deploymentEnvironment.getId() == deploymentEnvironmentId) {
							deploymentEnvironment.addPollutant(new Pollutant(pollutantId));
						}
					}
				}
			}
			// check for interfering pollutants
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("assoc_type_pollutant_and_type_interferingpollutant(")) {
					int pollutantId = Integer.parseInt(getVariable(answerPart, 0));
					int interferingPollutantId = Integer.parseInt(getVariable(answerPart, 1));
					
					for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
						for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
							if (pollutant.getId() == pollutantId) {
								pollutant.addInterferingPollutant(new InterferingPollutant(interferingPollutantId));
							}
						}
					}
				}
			}
			// check for sensors
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("assoc_type_pollutant_and_type_sensor(")) {
					int pollutantId = Integer.parseInt(getVariable(answerPart, 0));
					int sensorId = Integer.parseInt(getVariable(answerPart, 1));
					
					for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
						for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
							if (pollutant.getId() == pollutantId) {
								pollutant.addSensor(new Sensor(sensorId));
							}
						}
					}
				}
			}
			// check for reference models
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("assoc_type_sensor_and_type_referencemodel(")) {
					int sensorId = Integer.parseInt(getVariable(answerPart, 0));
					int referenceModelId = Integer.parseInt(getVariable(answerPart, 1));
					
					for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
						for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
							for (Sensor sensor : pollutant.getSensors()) {
								if (sensor.getId() == sensorId) {
									sensor.addReferenceModel(new ReferenceModel(referenceModelId));
								}
							}
						}
					}
				}
			}
			// check for monitoring policies
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("assoc_type_sensor_and_type_monitoringpolicy(")) {
					int sensorId = Integer.parseInt(getVariable(answerPart, 0));
					int monitoringPolicyId = Integer.parseInt(getVariable(answerPart, 1));
					
					for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
						for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
							for (Sensor sensor : pollutant.getSensors()) {
								if (sensor.getId() == sensorId) {
									sensor.setMonitoringPolicy(new MonitoringPolicy(monitoringPolicyId));
								}
							}
						}
					}
				}
			}
			
			// setting all the remaining attribute values
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("attributevalue_type_")) {
					int objectId = Integer.parseInt(getVariable(answerPart, 0));
					String attributeValue = getVariable(answerPart, 1);
					
					if (answerPart.startsWith("attributevalue_type_deploymentenvironment_")) {
						for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
							if (deploymentEnvironment.getId() == objectId) {
								if (answerPart.startsWith("attributevalue_type_deploymentenvironment_type(")) {
									deploymentEnvironment.setType(attributeValue);
								} else if (answerPart.startsWith("attributevalue_type_deploymentenvironment_context(")) {
									deploymentEnvironment.setContext(attributeValue);
								} else if (answerPart.startsWith("attributevalue_type_deploymentenvironment_locationtype(")) {
									deploymentEnvironment.setLocationType(attributeValue);
								}
							}
						}
					} else if (answerPart.startsWith("attributevalue_type_area_")) {
						for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
							for (Area area : deploymentEnvironment.getAreas()) {
								if (area.getId() == objectId) {
									if (answerPart.startsWith("attributevalue_type_area_type(")) {
										area.setType(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_category(")) {
										area.setCategory(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_prefabricatedbuilding(")) {
										area.setPrefabricatedBuilding(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_vehicletraffic(")) {
										area.setVehicleTraffic(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_industrialtype(")) {
										area.setIndustrialType(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_pollutedsoil(")) {
										area.setPollutedSoil(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_floor(")) {
										area.setFloor(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_controlledarea(")) {
										area.setControlledArea(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_airconditioning(")) {
										area.setAirConditioning(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_heatingsystem(")) {
										area.setHeatingSystem(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_windows(")) {
										area.setWindows(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_smokepresence(")) {
										area.setSmokePresence(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_moldpresence(")) {
										area.setMoldPresence(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_area_dustyarea(")) {
										area.setDustyArea(attributeValue);
									}
								}
							}
						}
					} else if (answerPart.startsWith("attributevalue_type_environmetalconditions_")) {
						for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
							for (Area area : deploymentEnvironment.getAreas()) {
								for (EnvironmentalCondition environmentalCondition : area.getEnvironmentalConditions()) {
									if (environmentalCondition.getId() == objectId) {
										if (answerPart.startsWith("attributevalue_type_environmetalconditions_humidity(")) {
											environmentalCondition.setHumidity(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_environmetalconditions_windspeed(")) {
											environmentalCondition.setWindSpeed(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_environmetalconditions_rain(")) {
											environmentalCondition.setRain(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_environmetalconditions_dust(")) {
											environmentalCondition.setDust(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_environmetalconditions_averagetemperature(")) {
											environmentalCondition.setAvgTemperature(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_environmetalconditions_snow(")) {
											environmentalCondition.setSnow(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_environmetalconditions_ice(")) {
											environmentalCondition.setIce(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_environmetalconditions_vibrations(")) {
											environmentalCondition.setVibrations(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_environmetalconditions_averagepressure(")) {
											environmentalCondition.setAvgPressure(attributeValue);
										}
									}
								}
							}
						}
					} else if (answerPart.startsWith("attributevalue_type_walltype_")) {
						for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
							for (Area area : deploymentEnvironment.getAreas()) {
								for (WallType wallType : area.getWallTypes()) {
									if (wallType.getId() == objectId) {
										if (answerPart.startsWith("attributevalue_type_walltype_wallpaper(")) {
											wallType.setWallpaper(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_walltype_plasticcladding(")) {
											wallType.setPlasticCladding(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_walltype_woodenpanels(")) {
											wallType.setWoodenPanels(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_walltype_moquette(")) {
											wallType.setMoquette(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_walltype_tiles(")) {
											wallType.setTiles(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_walltype_plaster(")) {
											wallType.setPlaster(attributeValue);
										}
									}
								}
							}
						}
					} else if (answerPart.startsWith("attributevalue_type_pollutant_")) {
						for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
							for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
								if (pollutant.getId() == objectId) {
									if (answerPart.startsWith("attributevalue_type_pollutant_type(")) {
										pollutant.setType(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_gas(")) {
										pollutant.setGas(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_electromagnetic(")) {
										pollutant.setElectromagnetic(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_ionizing(")) {
										pollutant.setIonizing(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_affectair(")) {
										pollutant.setAffectAir(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_affectwater(")) {
										pollutant.setAffectWater(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_affectsoil(")) {
										pollutant.setAffectSoil(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_affectobjects(")) {
										pollutant.setAffectObjects(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_ban(")) {
										pollutant.setBan(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_specific(")) {
										pollutant.setSpecific(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_metric(")) {
										pollutant.setMetric(attributeValue);
									} else if (answerPart.startsWith("attributevalue_type_pollutant_rawmetric(")) {
										pollutant.setRawMetric(attributeValue);
									}
								}
							}
						}
					} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_")) {
						for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
							for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
								for (InterferingPollutant interferingPollutant : pollutant.getInterferingPollutants()) {
									if (interferingPollutant.getId() == objectId) {
										if (answerPart.startsWith("attributevalue_type_interferingpollutant_type(")) {
											interferingPollutant.setType(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_gas(")) {
											interferingPollutant.setGas(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_electromagnetic(")) {
											interferingPollutant.setElectromagnetic(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_ionizing(")) {
											interferingPollutant.setIonizing(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_affectair(")) {
											interferingPollutant.setAffectAir(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_affectwater(")) {
											interferingPollutant.setAffectWater(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_affectsoil(")) {
											interferingPollutant.setAffectSoil(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_affectobjects(")) {
											interferingPollutant.setAffectObjects(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_ban(")) {
											interferingPollutant.setBan(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_explosive(")) {
											interferingPollutant.setExplosive(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_toxic(")) {
											interferingPollutant.setToxic(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_dangerous(")) {
											interferingPollutant.setDangerous(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_poisonous(")) {
											interferingPollutant.setPoisonous(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_interferingpollutant_metric(")) {
											interferingPollutant.setMetric(attributeValue);
										}
									}
								}
							}
						}
					} else if (answerPart.startsWith("attributevalue_type_sensor_")) {
						for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
							for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
								for (Sensor sensor : pollutant.getSensors()) {
									if (sensor.getId() == objectId) {
										if (answerPart.startsWith("attributevalue_type_sensor_type(")) {
											sensor.setType(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_technology(")) {
											sensor.setTechnology(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_sensitivity(")) {
											sensor.setSensitivity(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_pod(")) {
											sensor.setPod(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_range(")) {
											sensor.setRange(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_resolution(")) {
											sensor.setResolution(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_responsetime(")) {
											sensor.setResponseTime(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_expectedoperationlife(")) {
											sensor.setExpectedOperationLife(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_minoptemperature(")) {
											sensor.setMinOpTemperature(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_maxoptemperature(")) {
											sensor.setMaxOpTemperature(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_minophumidity(")) {
											sensor.setMinOpHumidity(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_maxophumidity(")) {
											sensor.setMaxOpHumidity(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_minoppressure(")) {
											sensor.setMinOpPressure(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_maxoppressure(")) {
											sensor.setMaxOpPressure(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_upperlimit(")) {
											sensor.setUpperLimit(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_dualsensor(")) {
											sensor.setDualSensor(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_sensor_noise(")) {
											sensor.setNoise(attributeValue);
										}
									}
								}
							}
						}
					} else if (answerPart.startsWith("attributevalue_type_referencemodel_")) {
						for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
							for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
								for (Sensor sensor : pollutant.getSensors()) {
									for (ReferenceModel referenceModel : sensor.getReferenceModesl()) {
										if (referenceModel.getId() == objectId) {
											if (answerPart.startsWith("attributevalue_type_referencemodel_monitoringalgorithm(")) {
												referenceModel.setMonitoringAlgorithm(attributeValue);
											}
										}
									}
								}
							}
						}
					} else if (answerPart.startsWith("attributevalue_type_monitoringpolicy_")) {
						for (DeploymentEnvironment deploymentEnvironment : monitoringStation.getDeploymentEnvironments()) {
							for (Pollutant pollutant : deploymentEnvironment.getPollutants()) {
								for (Sensor sensor : pollutant.getSensors()) {
									MonitoringPolicy monitoringPolicy = sensor.getMonitoringPolicy();
									if (monitoringPolicy.getId() == objectId) {
										if (answerPart.startsWith("attributevalue_type_monitoringpolicy_autosampling(")) {
											monitoringPolicy.setAutoSampling(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_monitoringpolicy_samplingtype(")) {
											monitoringPolicy.setSamplingType(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_monitoringpolicy_duration(")) {
											monitoringPolicy.setDuration(attributeValue);
										} else if (answerPart.startsWith("attributevalue_type_monitoringpolicy_publishrate(")) {
											monitoringPolicy.setPublishRate(attributeValue);
										}
									}
								}
							}
						}
					}
				}
			}
			
			results.add(monitoringStation);
		}
		
		return results;
	}
	
	private String getVariable(String statement, int index) {
		return statement.split("\\(")[1].replace(")", "").split(",")[index];
	}
}
