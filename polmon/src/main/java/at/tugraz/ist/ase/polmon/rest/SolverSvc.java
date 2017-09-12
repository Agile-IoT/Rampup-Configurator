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

package at.tugraz.ist.ase.polmon.rest;

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

import at.tugraz.ist.ase.polmon.ClingoExecutor;
import at.tugraz.ist.ase.polmon.classes.MonitoringStation;

@Path("/rest/solver")
public class SolverSvc {
	private String pathToClingo;
	private String pathToProgram;
	private int numOfResults;
	
	public SolverSvc() {
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
	public Response takeUserRequirements(String userRequirementsJson) {
		MonitoringStation monitoringStation = (new Gson()).fromJson(userRequirementsJson, MonitoringStation.class);
		monitoringStation.populateIds();
		
		ClingoExecutor clingoExecutor = new ClingoExecutor(pathToClingo, pathToProgram, numOfResults);
		ArrayList<MonitoringStation> resultList = clingoExecutor.executeWithUserRequirements(monitoringStation);
		
		return Response.ok((new Gson()).toJson(resultList)).build();
	}
}
