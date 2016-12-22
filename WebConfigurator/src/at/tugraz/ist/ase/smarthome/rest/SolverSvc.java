package at.tugraz.ist.ase.smarthome.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import at.tugraz.ist.ase.smarthome.Main;
import at.tugraz.ist.ase.smarthome.classes.SmartHome;
import at.tugraz.ist.ase.smarthome.classes.UserRequirements;

@Path("/rest/solver")
public class SolverSvc {
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response takeUserRequirements(String userRequirementsJson) {
		UserRequirements userRequirements = (new Gson()).fromJson(userRequirementsJson, UserRequirements.class);

		String clingoOutput = Main.clingoExecutor.getClingoOutput(userRequirements.generateConstraints());
		
		// initialize the resulting object structure
		HashMap<Integer, SmartHome> resultStructure = new HashMap<>();
		
		// get all the clingo answers individually
		String[] cmdAnswers = clingoOutput.split("Answer: ");
		
		// iterate over the answers (starting at 1 because of preceding clingo output)
		for (int i = 1; i < cmdAnswers.length; i++) {
			String[] answerParts = cmdAnswers[i].split(" ooasp_attribute_value");
			int answerIndex = Integer.parseInt(answerParts[0]);
			resultStructure.put(answerIndex, new SmartHome());
			
			for (int j = 1; j < answerParts.length; j++) {
				String[] attributeParts = answerParts[j].split("\"");
				
				String attributeKey = attributeParts[3];
				String attributeValue;
				
				if (attributeParts.length == 7) {
					// for syntax like ("i1","SmartHome_builtWith",2,"Other")
					attributeValue = attributeParts[5];
				} else {
					// for syntax like ("i1","SmartHome_age",2,0)
					attributeValue = attributeParts[4].split(",")[2];
					// remove the closing bracket
					attributeValue = attributeValue.substring(0, attributeValue.length() - 2);
				}
				
				// special case for the last answer
				attributeValue = attributeValue.split("\\) SATISFIABLE")[0];
				
				switch (attributeKey) {
				case "SmartHome_builtWith":
					resultStructure.get(answerIndex).setBuiltWith(attributeValue);
					break;
				case "SmartHome_communication":
					resultStructure.get(answerIndex).setCommunication(attributeValue);
					break;
				case "SmartHome_areTubesEnabled":
					resultStructure.get(answerIndex).setAreTubesInstalled(attributeValue);
					break;
				case "Room_type":
					resultStructure.get(answerIndex).addRoom(attributeValue);
					break;
				case "HomeAppliance_devices":
					// only possible that way if order is always correct
					resultStructure.get(answerIndex).addHomeAppliance(attributeValue);
					break;
				case "HomeAppliance_isAlwaysOn":
					// only possible that way if order is always correct
					resultStructure.get(answerIndex).setHomeApplianceAlwaysOn(attributeValue);
					break;
				case "HomeAppliance_isDangerous":
					// only possible that way if order is always correct
					resultStructure.get(answerIndex).setHomeApplianceDangerous(attributeValue);
					break;
				case "SmartHome_age":
					resultStructure.get(answerIndex).setAge(attributeValue);
					break;
				default:
					System.err.println("Ignoring " + attributeKey + " with value " + attributeValue);
					break;
				}
			}
		}
		
		return Response.ok((new Gson()).toJson(resultStructure)).build();
	}
}
