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
	
	public ArrayList<String> createDiagnosis (UserRequirements userRequirements) {
		ArrayList<String> userConstraintList = createUserConstraintList(userRequirements);
		
		// if isEmpty(C) or inconsistent(AC – C) return null
		if (userConstraintList.isEmpty() || !isConsistent(getClingoProgram())) {
			return null;
		// else return FD(null, C, AC);
		} else {
			ArrayList<String> ac = new ArrayList<String>();
			ac.add(getClingoProgram());
			ac.addAll(userConstraintList);
			return fastDiag(null, userConstraintList, ac);
		}
	}
	
	private boolean isConsistent(String program) {
		if (parseClingoOutput(executeClingo(program)).size() >= 1) {
			return true;
		} else {
			return false;
		}
	}
	private boolean isConsistent(ArrayList<String> constraints) {
		String program = "";
		for (String constraint : constraints) {
			program += constraint + "\n";
		}
		return isConsistent(program);
	}
	
	private ArrayList<String> subtract(ArrayList<String> first, ArrayList<String> second) {
		@SuppressWarnings("unchecked")
		ArrayList<String> result = (ArrayList<String>) first.clone();
		if (second != null) {
			result.removeAll(second);
		}
		return result;
	}
	
	private ArrayList<String> union(ArrayList<String> first, ArrayList<String> second) {
		if (second != null) {
			if (first != null) {
				@SuppressWarnings("unchecked")
				ArrayList<String> result = (ArrayList<String>) first.clone();
				for (String constraint : second) {
					if (!result.contains(constraint)) {
						result.add(constraint);
					}
				}
				return result;
			} else {
				return second;
			}
		} else {
			return first;
		}
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
	
	private ArrayList<String> fastDiag(ArrayList<String> d, ArrayList<String> c, ArrayList<String> ac) {
		// if D != null and consistent(AC) return null;
		if (d != null && isConsistent(ac)) {
			return null;
		}
		// if singleton(C) return C;
		if (isSingleton(c)) {
			return c;
		}
		// k = q/2;
		int q = c.size();
		int k = q / 2;
		// C1 = {c1..ck}; C2 = {ck+1..cq};
		ArrayList<String> c1 = new ArrayList<String>();
		ArrayList<String> c2 = new ArrayList<String>();
		c1.addAll(c.subList(0, k));
		c2.addAll(c.subList(k, q));
		// D1 = FD(C1, C2, AC - C1);
		ArrayList<String> d1 = fastDiag(c1, c2, subtract(ac, c1));
		// D2 = FD(D1, C1, AC - D1);
		ArrayList<String> d2 = fastDiag(d1, c1, subtract(ac, d1));
		
		// return(D1 U D2);
		return union(d1, d2);
	}
	
	private boolean isSingleton(ArrayList<String> c) {
		if (c.size() == 1) {
			return true;
		} else {
			return false;
		}
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
		
		for (String userConstraint : createUserConstraintList(userRequirements)) {
			program += userConstraint + "\n";
		}
		
		//System.out.println(program);
		
		return program;
	}
	
	private ArrayList<String> createUserConstraintList(UserRequirements userRequirements) {
		ArrayList<String> userConstraints = new ArrayList<String>();
		
		// user requirements
		userConstraints.add("type_userrequirements(" + userRequirements.getId() + ").");
		if (!userRequirements.isComfortControlNeeded().equals(""))
			userConstraints.add("attributevalue_type_userrequirements_iscomfortcontrolneeded(" + userRequirements.getId() + "," + userRequirements.isComfortControlNeeded() + ").");
		if (!userRequirements.isEnergySavingNeeded().equals(""))
			userConstraints.add("attributevalue_type_userrequirements_isenenergysavingneeded(" + userRequirements.getId() + "," + userRequirements.isEnergySavingNeeded() + ").");
		if (!userRequirements.isHealthSupportNeeded().equals(""))
			userConstraints.add("attributevalue_type_userrequirements_ishealthsupportneeded(" + userRequirements.getId() + "," + userRequirements.isHealthSupportNeeded() + ").");
		if (!userRequirements.isSafetySecurityNeeded().equals(""))
			userConstraints.add("attributevalue_type_userrequirements_issafetysecurityneeded(" + userRequirements.getId() + "," + userRequirements.isSafetySecurityNeeded() + ").");
		if (!userRequirements.isCostImportant().equals(""))
			userConstraints.add("attributevalue_type_userrequirements_iscostimportant(" + userRequirements.getId() + "," + userRequirements.isCostImportant() + ").");
		if (!userRequirements.isStabilityNeeded().equals(""))
			userConstraints.add("attributevalue_type_userrequirements_isstabilityneeded(" + userRequirements.getId() + "," + userRequirements.isStabilityNeeded() + ").");
		if (!userRequirements.isSensibleToElectricSmog().equals(""))
			userConstraints.add("attributevalue_type_userrequirements_issensibletoelectricsmog(" + userRequirements.getId() + "," + userRequirements.isSensibleToElectricSmog() + ").");
		if (!userRequirements.getInstallation().equals(""))
			userConstraints.add("attributevalue_type_userrequirements_installation(" + userRequirements.getId() + "," + userRequirements.getInstallation() + ").");
		
		// smart home
		SmartHome smartHome = userRequirements.getSmartHome();
		userConstraints.add("type_smarthome(" + smartHome.getId() + ").");
		if (!smartHome.areTubesInstalled().equals(""))
			userConstraints.add("attributevalue_type_smarthome_aretubesenabled(" + smartHome.getId() + "," + smartHome.areTubesInstalled() + ").");
		if (!smartHome.getBuiltWith().equals(""))
			userConstraints.add("attributevalue_type_smarthome_builtwith(" + smartHome.getId() + "," + smartHome.getBuiltWith() + ").");
		if (!smartHome.getCommunication().equals(""))
			userConstraints.add("attributevalue_type_smarthome_communication(" + smartHome.getId() + "," + smartHome.getCommunication() + ").");
			userConstraints.add("assoc_type_userrequirements_and_type_smarthome(" + userRequirements.getId() + "," + smartHome.getId() + ").");
		
		// rooms
		for (Room room : smartHome.getRooms()) {
			userConstraints.add("type_room(" + room.getId() + ").");
			if (!room.getType().equals(""))
				userConstraints.add("attributevalue_type_room_roomtype(" + room.getId() + "," + room.getType() + ").");
				userConstraints.add("assoc_type_room_and_type_smarthome(" + room.getId() + "," + smartHome.getId() + ").");
			if (room.getHomeAppliances() != null) {
				for (HomeAppliance homeAppliance : room.getHomeAppliances()) {
					userConstraints.add("type_homeappliance(" + homeAppliance.getId() + ").");
					if (!homeAppliance.getType().equals(""))
						userConstraints.add("attributevalue_type_homeappliance_homeappliancetype(" + homeAppliance.getId() + "," + homeAppliance.getType() + ").");
					if (!homeAppliance.isAlwaysOn().equals(""))
						userConstraints.add("attributevalue_type_homeappliance_isalwayson(" + homeAppliance.getId() + "," + homeAppliance.isAlwaysOn() + ").");
					if (!homeAppliance.isDangerous().equals(""))
						userConstraints.add("attributevalue_type_homeappliance_isdangerous(" + homeAppliance.getId() + "," + homeAppliance.isDangerous() + ").");
						userConstraints.add("assoc_type_room_and_type_homeappliance(" + room.getId() + "," + homeAppliance.getId() + ").");
				}
			}
		}
		
		return userConstraints;
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
