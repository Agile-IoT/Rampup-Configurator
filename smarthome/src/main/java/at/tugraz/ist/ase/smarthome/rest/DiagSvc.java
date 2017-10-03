package at.tugraz.ist.ase.smarthome.rest;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

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
		String result = clingoExecutor.createDiagnosis(userRequirements);
		
		return Response.ok(result).build();
	}
}
