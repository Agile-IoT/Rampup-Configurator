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

import org.eclipse.agail.smarthome.ClingoExecutor;
import org.eclipse.agail.smarthome.classes.UserRequirements;

import com.google.gson.Gson;

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
		UserRequirements userRequirements = (new Gson()).fromJson(userRequirementsJson, UserRequirements.class);
		userRequirements.populateIds();
		
		ClingoExecutor clingoExecutor = new ClingoExecutor(pathToClingo, pathToProgram, numOfResults);
		ArrayList<UserRequirements> resultList = clingoExecutor.executeWithUserRequirements(userRequirements);
		
		return Response.ok((new Gson()).toJson(resultList)).build();
	}
}
