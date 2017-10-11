package at.tugraz.ist.ase.smarthome.rest;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
	public Response makeDiagnosis(String userRequirementsJson, @HeaderParam("preferredRequirements") String preferredRequirements) {
		UserRequirements userRequirements = (new Gson()).fromJson(userRequirementsJson, UserRequirements.class);
		userRequirements.populateIds();
		
		ArrayList<String> preferredRequirementsList = createPreferredRequirementsList(preferredRequirements);
		
		ClingoExecutor clingoExecutor = new ClingoExecutor(pathToClingo, pathToProgram, numOfResults);
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
