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

package at.tugraz.ist.ase.smarthome;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import at.tugraz.ist.ase.smarthome.classes.HomeAppliance;
import at.tugraz.ist.ase.smarthome.classes.Room;
import at.tugraz.ist.ase.smarthome.classes.SmartHome;
import at.tugraz.ist.ase.smarthome.classes.UserRequirements;

public class ClingoExecutor {
	private String pathToClingo;
	private String pathToProgram;
	private int numOfResults;
	
	public ClingoExecutor(String pathToClingo, String pathToProgram, int numOfResults) {
		this.pathToClingo = pathToClingo;
		this.pathToProgram = pathToProgram;
		this.numOfResults = numOfResults;
	}
	
	public ArrayList<UserRequirements> executeWithUserRequirements(UserRequirements userRequirements) {
		// get program
		String basicProgram = getClingoProgram();
		// make program out of user requirements
		String userReqProgram = generateUserReqProgram(userRequirements);
		String finalProgram = basicProgram.replaceAll("% PARSER_UserRequirements", userReqProgram);
		// call executeClingo with program
		String clingoOutput = executeClingo(finalProgram);
		
		return parseClingoOutput(clingoOutput);
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
	
	private String generateUserReqProgram(UserRequirements userRequirements) {
		String program = "";
		
		// user requirements
		program += "type_userrequirements(" + userRequirements.getId() + ").\n";
		if (!userRequirements.isComfortControlNeeded().equals(""))
			program += "attributevalue_type_userrequirements_iscomfortcontrolneeded(" + userRequirements.getId() + "," + userRequirements.isComfortControlNeeded() + ").\n";
		if (!userRequirements.isEnergySavingNeeded().equals(""))
			program += "attributevalue_type_userrequirements_isenenergysavingneeded(" + userRequirements.getId() + "," + userRequirements.isEnergySavingNeeded() + ").\n";
		if (!userRequirements.isHealthSupportNeeded().equals(""))
			program += "attributevalue_type_userrequirements_ishealthsupportneeded(" + userRequirements.getId() + "," + userRequirements.isHealthSupportNeeded() + ").\n";
		if (!userRequirements.isSafetySecurityNeeded().equals(""))
			program += "attributevalue_type_userrequirements_issafetysecurityneeded(" + userRequirements.getId() + "," + userRequirements.isSafetySecurityNeeded() + ").\n";
		if (!userRequirements.isCostImportant().equals(""))
			program += "attributevalue_type_userrequirements_iscostimportant(" + userRequirements.getId() + "," + userRequirements.isCostImportant() + ").\n";
		if (!userRequirements.isStabilityNeeded().equals(""))
			program += "attributevalue_type_userrequirements_isstabilityneeded(" + userRequirements.getId() + "," + userRequirements.isStabilityNeeded() + ").\n";
		if (!userRequirements.isSensibleToElectricSmog().equals(""))
			program += "attributevalue_type_userrequirements_issensibletoelectricsmog(" + userRequirements.getId() + "," + userRequirements.isSensibleToElectricSmog() + ").\n";
		if (!userRequirements.getInstallation().equals(""))
			program += "attributevalue_type_userrequirements_installation(" + userRequirements.getId() + "," + userRequirements.getInstallation() + ").\n";
		
		// smart home
		SmartHome smartHome = userRequirements.getSmartHome();
		program += "type_smarthome(" + smartHome.getId() + ").\n";
		if (!smartHome.areTubesInstalled().equals(""))
			program += "attributevalue_type_smarthome_aretubesenabled(" + smartHome.getId() + "," + smartHome.areTubesInstalled() + ").\n";
		if (!smartHome.getBuiltWith().equals(""))
			program += "attributevalue_type_smarthome_builtwith(" + smartHome.getId() + "," + smartHome.getBuiltWith() + ").\n";
		if (!smartHome.getCommunication().equals(""))
			program += "attributevalue_type_smarthome_communication(" + smartHome.getId() + "," + smartHome.getCommunication() + ").\n";
		program += "assoc_type_userrequirements_and_type_smarthome(" + userRequirements.getId() + "," + smartHome.getId() + ").\n";
		
		// rooms
		for (Room room : smartHome.getRooms()) {
			program += "type_room(" + room.getId() + ").\n";
			if (!room.getType().equals(""))
				program += "attributevalue_type_room_roomtype(" + room.getId() + "," + room.getType() + ").\n";
			program += "assoc_type_room_and_type_smarthome(" + room.getId() + "," + smartHome.getId() + ").\n";
			if (room.getHomeAppliances() != null) {
				for (HomeAppliance homeAppliance : room.getHomeAppliances()) {
					program += "type_homeappliance(" + homeAppliance.getId() + ").\n";
					if (!homeAppliance.getType().equals(""))
						program += "attributevalue_type_homeappliance_homeappliancetype(" + homeAppliance.getId() + "," + homeAppliance.getType() + ").\n";
					if (!homeAppliance.isAlwaysOn().equals(""))
						program += "attributevalue_type_homeappliance_isalwayson(" + homeAppliance.getId() + "," + homeAppliance.isAlwaysOn() + ").\n";
					if (!homeAppliance.isDangerous().equals(""))
						program += "attributevalue_type_homeappliance_isdangerous(" + homeAppliance.getId() + "," + homeAppliance.isDangerous() + ").\n";
					program += "assoc_type_room_and_type_homeappliance(" + room.getId() + "," + homeAppliance.getId() + ").\n";
				}
			}
		}
		
		System.out.println(program);
		
		return program;
	}
	
	private ArrayList<UserRequirements> parseClingoOutput(String clingoOutput) {
		ArrayList<UserRequirements> results = new ArrayList<UserRequirements>();
		
		String[] answers = clingoOutput.split("Answer: ");
		
		for (int i = 1; i < answers.length; i++) {
			String[] answerLine = answers[i].split("\n");
			String[] answerParts = answerLine[1].split(" ");
			
			UserRequirements userRequirements = new UserRequirements();
			
			// check for the UserRequirements
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("type_userrequirements(")) {
					userRequirements.setId(getIdOfInstantiation(answerPart, "type_userrequirements"));
				}
				if (answerPart.startsWith("attributevalue_type_userrequirements_iscomfortcontrolneeded")) {
					userRequirements.setComfortControlNeeded(getAttributeValue(answerPart));
				}
				if (answerPart.startsWith("attributevalue_type_userrequirements_isenenergysavingneeded")) {
					userRequirements.setEnergySavingNeeded(getAttributeValue(answerPart));
				}
				if (answerPart.startsWith("attributevalue_type_userrequirements_ishealthsupportneeded")) {
					userRequirements.setHealthSupportNeeded(getAttributeValue(answerPart));
				}
				if (answerPart.startsWith("attributevalue_type_userrequirements_issafetysecurityneeded")) {
					userRequirements.setSafetySecurityNeeded(getAttributeValue(answerPart));
				}
				if (answerPart.startsWith("attributevalue_type_userrequirements_iscostimportant")) {
					userRequirements.setCostImportant(getAttributeValue(answerPart));
				}
				if (answerPart.startsWith("attributevalue_type_userrequirements_issensibletoelectricsmog")) {
					userRequirements.setSensibleToElectricSmog(getAttributeValue(answerPart));
				}
				if (answerPart.startsWith("attributevalue_type_userrequirements_isstabilityneeded")) {
					userRequirements.setStabilityNeeded(getAttributeValue(answerPart));
				}
				if (answerPart.startsWith("attributevalue_type_userrequirements_installation")) {
					userRequirements.setInstallation(getAttributeValue(answerPart));
				}
			}
			
			// check for the SmartHomes
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("type_smarthome(")) {
					SmartHome smartHome = new SmartHome();
					smartHome.setId(getIdOfInstantiation(answerPart, "type_smarthome"));
					userRequirements.setSmartHome(smartHome);
				}
			}
			
			// check for the Rooms
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("type_room(")) {
					Room room = new Room(getIdOfInstantiation(answerPart, "type_room"));
					userRequirements.getSmartHome().addRoom(room);
				}
			}
			
			// check for the HomeAppliances
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("type_homeappliance(")) {
					int homeApplianceId = getIdOfInstantiation(answerPart, "type_homeappliance");
					HomeAppliance homeAppliance = new HomeAppliance(homeApplianceId);
					int roomId = 0;
					for (String answerPart2 : answerParts) {
						if (answerPart2.startsWith("assoc_type_room_and_type_homeappliance") && answerPart2.contains(homeApplianceId + "")) {
							//roomId = Integer.parseInt(getAttributeValue(answerPart2));
							roomId = Integer.parseInt(getFactPart(answerPart2, 0));
							break;
						}
					}
					for (Room room : userRequirements.getSmartHome().getRooms()) {
						if (room.getId() == roomId) {
							room.addHomeAppliance(homeAppliance);
							break;
						}
					}
				}
			}
			
			// set the attribute values
			for (String answerPart : answerParts) {
				if (answerPart.startsWith("attributevalue_type_smarthome_aretubesenabled"))
					userRequirements.getSmartHome().setAreTubesInstalled(getAttributeValue(answerPart));
				if (answerPart.startsWith("attributevalue_type_smarthome_builtwith"))
					userRequirements.getSmartHome().setBuiltWith(getAttributeValue(answerPart));
				if (answerPart.startsWith("attributevalue_type_smarthome_communication"))
					userRequirements.getSmartHome().setCommunication(getAttributeValue(answerPart));
				if (answerPart.startsWith("attributevalue_type_room_roomtype")) {
					String type = getAttributeValue(answerPart);
					int roomId = getAttributeId(answerPart);
					for (Room room : userRequirements.getSmartHome().getRooms()) {
						if (room.getId() == roomId) {
							room.setType(type);
							break;
						}
					}
				}
				if (answerPart.startsWith("attributevalue_type_homeappliance_homeappliancetype")) {
					String type = getAttributeValue(answerPart);
					int homeApplianceId = getAttributeId(answerPart);
					for (Room room : userRequirements.getSmartHome().getRooms()) {
						boolean finished = false;
						if (room.getHomeAppliances() != null) {
							for (HomeAppliance homeAppliance : room.getHomeAppliances()) {
								if (homeAppliance.getId() == homeApplianceId) {
									homeAppliance.setType(type);
									finished = true;
									break;
								}
								if (finished)
									break;
							}
						}
					}
				}
				if (answerPart.startsWith("attributevalue_type_homeappliance_isalwayson")) {
					String value = getAttributeValue(answerPart);
					int homeApplianceId = getAttributeId(answerPart);
					for (Room room : userRequirements.getSmartHome().getRooms()) {
						boolean finished = false;
						if (room.getHomeAppliances() != null) {
							for (HomeAppliance homeAppliance : room.getHomeAppliances()) {
								if (homeAppliance.getId() == homeApplianceId) {
									homeAppliance.setAlwaysOn(value);
									finished = true;
									break;
								}
								if (finished)
									break;
							}
						}
					}
				}
				if (answerPart.startsWith("attributevalue_type_homeappliance_isdangerous")) {
					String value = getAttributeValue(answerPart);
					int homeApplianceId = getAttributeId(answerPart);
					for (Room room : userRequirements.getSmartHome().getRooms()) {
						boolean finished = false;
						if (room.getHomeAppliances() != null) {
							for (HomeAppliance homeAppliance : room.getHomeAppliances()) {
								if (homeAppliance.getId() == homeApplianceId) {
									homeAppliance.setDangerous(value);
									finished = true;
									break;
								}
								if (finished)
									break;
							}
						}
					}
				}
			}
			
			
			results.add(userRequirements);
		}
		
		return results;
	}
	
	private int getIdOfInstantiation(String instantiation, String type) {
		return Integer.parseInt(instantiation.replaceAll(type, "").replaceAll("\\p{P}", ""));
	}
	
	private String getAttributeValue(String answerPart) {
		return answerPart.split(",")[1].replaceAll("\\p{P}", "");
	}
	private String getFactPart(String answerPart, int index) {
		String idString = answerPart.split("\\(")[1].replaceAll("\\)", "");
		return idString.split(",")[index];
	}
	private int getAttributeId(String answerPart) {
		return Integer.parseInt(answerPart.split(",")[0].split("\\(")[1]);
	}
}
