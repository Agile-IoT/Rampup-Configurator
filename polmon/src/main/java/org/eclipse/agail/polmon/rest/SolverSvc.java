/*********************************************************************
* Copyright (c) 2017-11-28 Christoph Uran (TU Graz)
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.agail.polmon.rest;

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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.agail.polmon.ClingoExecutor;
import org.eclipse.agail.polmon.classes.MonitoringStation;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

@Path("/rest/solver")
@Singleton
public class SolverSvc {
	private String pathToClingo;
	private String pathToProgram;
	private int numOfResults;
	private HashSet<ArrayList<String>> allMinConflictSets;
	
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
		System.out.println("Got " + allMinConflictSets.size() + " minimal conflict sets from file(s) in " + pathToMinConflictSetFiles);
	}
	
	@POST
	@Consumes(MediaType.WILDCARD)
	@Produces(MediaType.APPLICATION_JSON)
	public Response takeUserRequirements(String userRequirementsJson) {
		MonitoringStation monitoringStation = (new Gson()).fromJson(userRequirementsJson, MonitoringStation.class);
		monitoringStation.populateIds();
		
		ClingoExecutor clingoExecutor = new ClingoExecutor(pathToClingo, pathToProgram, numOfResults, allMinConflictSets);
		ArrayList<MonitoringStation> resultList = clingoExecutor.executeWithUserRequirements(monitoringStation);
		
		return Response.ok((new Gson()).toJson(resultList)).build();
	}
}
