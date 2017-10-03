package at.tugraz.ist.ase.smarthome.rest;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import at.tugraz.ist.ase.smarthome.ClingoExecutor;
import at.tugraz.ist.ase.smarthome.classes.UserRequirements;

@Path("/rest/diagnosis")
public class DiagSvc {
	private String pathToClingo;
	private String pathToProgram;
	private int numOfResults;
	
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
	}

	@POST
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	public Response makeDiagnosis(String userRequirementsJson) {
		UserRequirements userRequirements = (new Gson()).fromJson(userRequirementsJson, UserRequirements.class);
		userRequirements.populateIds();
		
		ClingoExecutor clingoExecutor = new ClingoExecutor(pathToClingo, pathToProgram, numOfResults);
		ArrayList<String> diagnosis = clingoExecutor.createDiagnosis(userRequirements);
		
		System.out.println(diagnosis);
		
		return Response.ok(translateToJavaScriptId(diagnosis).toString()).build();
	}
	
	private JsonObject translateToJavaScriptId(ArrayList<String> diagnosis) {
		JsonObject diagnosisObject = new JsonObject();
		JsonArray diagnosisParts = new JsonArray();
		
		for (String diagnosisPart : diagnosis) {
			String javaScriptId = "";
			if (diagnosisPart.contains("attributevalue_type_userrequirements_iscomfortcontrolneeded")) {
				javaScriptId = "#isComfortControlNeeded";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_isenenergysavingneeded")) {
				javaScriptId = "#isEnergySavingNeeded";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_ishealthsupportneeded")) {
				javaScriptId = "#isHealthSupportNeeded";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_issafetysecurityneeded")) {
				javaScriptId = "#isSafetySecurityNeeded";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_iscostimportant")) {
				javaScriptId = "#isCostImportant";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_isstabilityneeded")) {
				javaScriptId = "#isStabilityNeeded";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_issensibletoelectricsmog")) {
				javaScriptId = "#isSensibleToElectricSmog";
			} else if (diagnosisPart.contains("attributevalue_type_userrequirements_installation")) {
				javaScriptId = "#installation";
			} else if (diagnosisPart.contains("attributevalue_type_smarthome_communication")) {
				javaScriptId = "#communication";
			} else if (diagnosisPart.contains("attributevalue_type_smarthome_builtwith")) {
				javaScriptId = "#builtWith";
			} else if (diagnosisPart.contains("attributevalue_type_smarthome_aretubesenabled")) {
				javaScriptId = "#areTubesInstalled";
			} else if (diagnosisPart.contains("attributevalue_type_room_roomtype")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 10;
				javaScriptId = "#room-container div:nth-child(" + position + ") select#select-room-type";
			}
			diagnosisParts.add(javaScriptId);
		}
		
		diagnosisObject.add("diagnosisParts", diagnosisParts);
		
		return diagnosisObject;
	}
}
