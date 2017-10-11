package at.tugraz.ist.ase.polmon.rest;

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

import at.tugraz.ist.ase.polmon.ClingoExecutor;
import at.tugraz.ist.ase.polmon.classes.MonitoringStation;

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
		MonitoringStation monitoringStation = (new Gson()).fromJson(userRequirementsJson, MonitoringStation.class);
		monitoringStation.populateIds();
		
		ArrayList<String> preferredRequirementsList = createPreferredRequirementsList(preferredRequirements);
		
		ClingoExecutor clingoExecutor = new ClingoExecutor(pathToClingo, pathToProgram, numOfResults);
		ArrayList<String> diagnosis = clingoExecutor.createDiagnosis(monitoringStation, preferredRequirementsList);
		
		return Response.ok(translateToJavaScriptId(diagnosis).toString()).build();
	}
	
	private JsonObject translateToJavaScriptId(ArrayList<String> diagnosis) {
		JsonObject diagnosisObject = new JsonObject();
		JsonArray diagnosisParts = new JsonArray();
		
		for (String diagnosisPart : diagnosis) {
			String javaScriptId = "";
			// General settings
			if (diagnosisPart.contains("attributevalue_type_monitoringstation_communication")) {
				javaScriptId = "#communicationPOS0";
			} else if (diagnosisPart.contains("attributevalue_type_monitoringstation_localstorage")) {
				javaScriptId = "#localStoragePOS0";
			} else if (diagnosisPart.contains("attributevalue_type_monitoringstation_cloudstorage")) {
				javaScriptId = "#cloudStoragePOS0";
			} else if (diagnosisPart.contains("attributevalue_type_monitoringstation_enclosure")) {
				javaScriptId = "#enclosurePOS0";
			// Deployment environment
			} else if (diagnosisPart.contains("attributevalue_type_deploymentenvironment_type")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 2;
				javaScriptId = ".select-deployment-environment-typePOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_deploymentenvironment_context")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 2;
				javaScriptId = ".select-deployment-environment-contextPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_deploymentenvironment_locationtype")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 2;
				javaScriptId = ".select-deployment-environment-location-typePOS" + position;
			// Area
			} else if (diagnosisPart.contains("attributevalue_type_area_type")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-typePOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_category")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-categoryPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_prefabricatedbuilding")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-prefabricatedbuildingPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_vehicletraffic")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-vehicletrafficPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_industrialtype")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-industrialtypePOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_pollutedsoil")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-pollutedsoilPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_floor")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-floorPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_controlledarea")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-controlledareaPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_airconditioning")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-airconditioningPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_heatingsystem")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-heatingsystemPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_windows")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-windowsPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_smokepresence")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-smokepresencePOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_moldpresence")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-moldpresencePOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_area_dustyarea")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 3;
				javaScriptId = ".select-area-dustyareaPOS" + position;
			// Environmental condition
			} else if (diagnosisPart.contains("attributevalue_type_environmetalconditions_humidity")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 1000;
				javaScriptId = ".select-environmental-condition-humidityPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_environmetalconditions_windspeed")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 1000;
				javaScriptId = ".select-environmental-condition-windspeedPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_environmetalconditions_rain")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 1000;
				javaScriptId = ".select-environmental-condition-rainPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_environmetalconditions_dust")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 1000;
				javaScriptId = ".select-environmental-condition-dustPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_environmetalconditions_averagetemperature")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 1000;
				javaScriptId = ".select-environmental-condition-averagetemperaturePOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_environmetalconditions_snow")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 1000;
				javaScriptId = ".select-environmental-condition-snowPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_environmetalconditions_ice")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 1000;
				javaScriptId = ".select-environmental-condition-icePOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_environmetalconditions_vibrations")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 1000;
				javaScriptId = ".select-environmental-condition-vibrationsPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_environmetalconditions_averagepressure")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 1000;
				javaScriptId = ".select-environmental-condition-averagepressurePOS" + position;
			// Wallpaper type
			} else if (diagnosisPart.contains("attributevalue_type_walltype_wallpaper")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 2000;
				javaScriptId = ".select-wall-type-wallpaperPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_walltype_plasticcladding")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 2000;
				javaScriptId = ".select-wall-type-plasticcladdingPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_walltype_woodenpanels")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 2000;
				javaScriptId = ".select-wall-type-woodenpanelsPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_walltype_moquette")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 2000;
				javaScriptId = ".select-wall-type-moquettePOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_walltype_tiles")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 2000;
				javaScriptId = ".select-wall-type-tilesPOS" + position;
			} else if (diagnosisPart.contains("attributevalue_type_walltype_plaster")) {
				int position = Integer.parseInt(diagnosisPart.split("\\(")[1].split(",")[0]) - 2000;
				javaScriptId = ".select-wall-type-plasterPOS" + position;
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
		if (javaScriptId.equals("communication")) {
			return "attributevalue_type_monitoringstation_communication";
		} else if (javaScriptId.equals("enclosure")) {
			return "attributevalue_type_monitoringstation_enclosure";
		} else if (javaScriptId.equals("localStorage")) {
			return "attributevalue_type_monitoringstation_localstorage";
		} else if (javaScriptId.equals("cloudStorage")) {
			return "attributevalue_type_monitoringstation_cloudstorage";
		} else if (javaScriptId.equals("select-deployment-environment-type")) {
			return "attributevalue_type_deploymentenvironment_type";
		} else if (javaScriptId.equals("select-deployment-environment-context")) {
			return "attributevalue_type_deploymentenvironment_context";
		} else if (javaScriptId.equals("select-deployment-environment-location-type")) {
			return "attributevalue_type_deploymentenvironment_locationtype";
		} else if (javaScriptId.equals("select-area-type")) {
			return "attributevalue_type_area_type";
		} else if (javaScriptId.equals("select-area-category")) {
			return "attributevalue_type_area_category";
		} else if (javaScriptId.equals("select-area-prefabricatedbuilding")) {
			return "attributevalue_type_area_prefabricatedbuilding";
		} else if (javaScriptId.equals("select-area-vehicletraffic")) {
			return "attributevalue_type_area_vehicletraffic";
		} else if (javaScriptId.equals("select-area-industrialtype")) {
			return "attributevalue_type_area_industrialtype";
		} else if (javaScriptId.equals("select-area-pollutedsoil")) {
			return "attributevalue_type_area_pollutedsoil";
		} else if (javaScriptId.equals("select-area-floor")) {
			return "attributevalue_type_area_floor";
		} else if (javaScriptId.equals("select-area-controlledarea")) {
			return "attributevalue_type_area_controlledarea";
		} else if (javaScriptId.equals("select-area-airconditioning")) {
			return "attributevalue_type_area_airconditioning";
		} else if (javaScriptId.equals("select-area-heatingsystem")) {
			return "attributevalue_type_area_heatingsystem";
		} else if (javaScriptId.equals("select-area-windows")) {
			return "attributevalue_type_area_windows";
		} else if (javaScriptId.equals("select-area-smokepresence")) {
			return "attributevalue_type_area_smokepresence";
		} else if (javaScriptId.equals("select-area-moldpresence")) {
			return "attributevalue_type_area_moldpresence";
		} else if (javaScriptId.equals("select-area-dustyarea")) {
			return "attributevalue_type_area_dustyarea";
		} else if (javaScriptId.equals("select-environmental-condition-humidity")) {
			return "attributevalue_type_environmetalconditions_humidity";
		} else if (javaScriptId.equals("select-environmental-condition-windspeed")) {
			return "attributevalue_type_environmetalconditions_windspeed";
		} else if (javaScriptId.equals("select-environmental-condition-rain")) {
			return "attributevalue_type_environmetalconditions_rain";
		} else if (javaScriptId.equals("select-environmental-condition-dust")) {
			return "attributevalue_type_environmetalconditions_dust";
		} else if (javaScriptId.equals("select-environmental-condition-averagetemperature")) {
			return "attributevalue_type_environmetalconditions_averagetemperature";
		} else if (javaScriptId.equals("select-environmental-condition-snow")) {
			return "attributevalue_type_environmetalconditions_snow";
		} else if (javaScriptId.equals("select-environmental-condition-ice")) {
			return "attributevalue_type_environmetalconditions_ice";
		} else if (javaScriptId.equals("select-environmental-condition-vibrations")) {
			return "attributevalue_type_environmetalconditions_vibrations";
		} else if (javaScriptId.equals("select-environmental-condition-averagepressure")) {
			return "attributevalue_type_environmetalconditions_averagepressure";
		} else if (javaScriptId.equals("select-wall-type-wallpaper")) {
			return "attributevalue_type_walltype_wallpaper";
		} else if (javaScriptId.equals("select-wall-type-plasticcladding")) {
			return "attributevalue_type_walltype_plasticcladding";
		} else if (javaScriptId.equals("select-wall-type-woodenpanels")) {
			return "attributevalue_type_walltype_woodenpanels";
		} else if (javaScriptId.equals("select-wall-type-moquette")) {
			return "attributevalue_type_walltype_moquette";
		} else if (javaScriptId.equals("select-wall-type-tiles")) {
			return "attributevalue_type_walltype_tiles";
		} else if (javaScriptId.equals("select-wall-type-plaster")) {
			return "attributevalue_type_walltype_plaster";
		} else {
			return "";
		}
	}
}
