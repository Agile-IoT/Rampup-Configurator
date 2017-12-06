/*********************************************************************
* Copyright (c) 2017-11-28 Christoph Uran (TU Graz)
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.agail.smarthome.rest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.agail.smarthome.ClingoExecutor;
import org.eclipse.agail.smarthome.classes.UserRequirements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

@Path("/rest/diagnosis")
@Singleton
public class DiagSvc {
	private String pathToClingo;
	private String pathToProgram;
	private int numOfResults;
	private HashSet<ArrayList<String>> allMinConflictSets;
	
	public DiagSvc() {
		String filename = "config.properties";
		
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(filename));
		} catch (IOException e) {
			System.err.println("Could not load the configuration file " + filename + ".");
		}
		
		// load the values of the properties file
		pathToClingo = properties.getProperty("clingo_path");
		pathToProgram = properties.getProperty("program_path");
		numOfResults = Integer.parseInt(properties.getProperty("num_of_solutions", "10"));
		
		// all the known minimal conflict sets
		String pathToMinConflictSetFiles = properties.getProperty("min_conflict_set_dir_path");
		allMinConflictSets = new HashSet<ArrayList<String>>();
		Gson minConflictSetGson = new Gson();
		Type minConflictSetType = new TypeToken<HashSet<ArrayList<String>>>() {}.getType();
		for (final File currentFile : new File(pathToMinConflictSetFiles).listFiles()) {
			if (currentFile.isFile() && currentFile.getName().endsWith(".json")) {
				try {
					HashSet<ArrayList<String>> currentFileMinConflictSet = minConflictSetGson.fromJson(new JsonReader(new FileReader(currentFile)), minConflictSetType);
					allMinConflictSets.addAll(currentFileMinConflictSet);
				} catch (JsonIOException e) {
					System.err.println("Could not read file " + currentFile.getName());
					e.printStackTrace();
				} catch (JsonSyntaxException e) {
					System.err.println("File " + currentFile.getName() + " does not contain valid JSON");
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					System.err.println("File " + currentFile.getName() + " not found");
					e.printStackTrace();
				}
			}
		}
	}

	@POST
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	public Response makeDiagnosis(String userRequirementsJson, @HeaderParam("preferredRequirements") String preferredRequirements) {
		UserRequirements userRequirements = (new Gson()).fromJson(userRequirementsJson, UserRequirements.class);
		userRequirements.populateIds();
		
		ArrayList<String> preferredRequirementsList = createPreferredRequirementsList(preferredRequirements);
		
		ClingoExecutor clingoExecutor = new ClingoExecutor(pathToClingo, pathToProgram, numOfResults, allMinConflictSets);
		ArrayList<String> diagnosis = clingoExecutor.createDiagnosis(userRequirements, preferredRequirementsList);
		
		return Response.ok(translateToJavaScriptId(diagnosis).toString()).build();
	}
	
	private JsonObject translateToJavaScriptId(ArrayList<String> diagnosis) {
		JsonObject diagnosisObject = new JsonObject();
		JsonArray diagnosisParts = new JsonArray();
		
		for (String diagnosisPart : diagnosis) {
			String javaScriptId = "";
			if (diagnosisPart.contains("attributevalue_type_userrequirements_iscomfortcontrolneeded")) {
				javaScriptId = "#isComfortControlNeededPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_isenenergysavingneeded")) {
				javaScriptId = "#isEnergySavingNeededPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_ishealthsupportneeded")) {
				javaScriptId = "#isHealthSupportNeededPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_issafetysecurityneeded")) {
				javaScriptId = "#isSafetySecurityNeededPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_iscostimportant")) {
				javaScriptId = "#isCostImportantPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_isstabilityneeded")) {
				javaScriptId = "#isStabilityNeededPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_issensibletoelectricsmog")) {
				javaScriptId = "#isSensibleToElectricSmogPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_installation")) {
				javaScriptId = "#installationPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_smarthome_communication")) {
				javaScriptId = "#communicationPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_smarthome_builtwith")) {
				javaScriptId = "#builtWithPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_smarthome_aretubesenabled")) {
				javaScriptId = "#areTubesInstalledPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_room_roomtype")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 11;
				javaScriptId = ".select-room-typePOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_homeappliance_homeappliancetype")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 21;
				javaScriptId = ".select-homeappliance-typePOS" + position;
			}
			diagnosisParts.add(javaScriptId);
		}
		
		diagnosisObject.add("diagnosisParts", diagnosisParts);
		
		return diagnosisObject;
	}
	
	private ArrayList<String> createPreferredRequirementsList(String preferredRequirements) {
		ArrayList<String> preferredRequirementsList = new ArrayList<String>();
		
		if (preferredRequirements != null) {
			JsonElement jsonElement = new JsonParser().parse(preferredRequirements);
			JsonArray jsonArray = jsonElement.getAsJsonArray();
			for (JsonElement currentJsonElement : jsonArray) {
				JsonObject jsonObject = currentJsonElement.getAsJsonObject();
				String attribute = translateToCligoName(jsonObject.getAsJsonPrimitive("id").getAsString());
				String value = jsonObject.getAsJsonPrimitive("value").getAsString();
				preferredRequirementsList.add(attribute + ".*" + value + ".*");
			}
		}
		
		return preferredRequirementsList;
	}
	
	private String translateToCligoName(String javaScriptId) {
		if (javaScriptId.equals("isComfortControlNeeded")) {
			return "attributevalue_type_userrequirements_iscomfortcontrolneeded";
		} else if (javaScriptId.equals("isEnergySavingNeeded")) {
			return "attributevalue_type_userrequirements_isenenergysavingneeded";
		} else if (javaScriptId.equals("isHealthSupportNeeded")) {
			return "attributevalue_type_userrequirements_ishealthsupportneeded";
		} else if (javaScriptId.equals("isSafetySecurityNeeded")) {
			return "attributevalue_type_userrequirements_issafetysecurityneeded";
		} else if (javaScriptId.equals("isCostImportant")) {
			return "attributevalue_type_userrequirements_iscostimportant";
		} else if (javaScriptId.equals("isStabilityNeeded")) {
			return "attributevalue_type_userrequirements_isstabilityneeded";
		} else if (javaScriptId.equals("isSensibleToElectricSmog")) {
			return "attributevalue_type_userrequirements_issensibletoelectricsmog";
		} else if (javaScriptId.equals("installation")) {
			return "attributevalue_type_userrequirements_installation";
		} else if (javaScriptId.equals("areTubesInstalled")) {
			return "attributevalue_type_smarthome_aretubesenabled";
		} else if (javaScriptId.equals("builtWith")) {
			return "attributevalue_type_smarthome_builtwith";
		} else if (javaScriptId.equals("communication")) {
			return "attributevalue_type_smarthome_communication";
		} else if (javaScriptId.equals("select-room-type")) {
			return "attributevalue_type_room_roomtype";
		} else if (javaScriptId.equals("select-homeappliance-type")) {
			return "attributevalue_type_homeappliance_homeappliancetype";
		} else {
			return "";
		}
	}
}
