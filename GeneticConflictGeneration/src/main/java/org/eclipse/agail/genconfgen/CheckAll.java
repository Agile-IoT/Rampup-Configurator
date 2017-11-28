/*********************************************************************
* Copyright (c) 2017-11-28 Christoph Uran (TU Graz)
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.agail.genconfgen;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class CheckAll {
	private static String clingoPath = "clingo";
	private static String programPath = "kb.lp";
	private static String userRequirementsDefinitionPath = "user_requirements_definition.json";
	private static String minConflictSetPath = "min_conflict_sets.json";

	public static void main(String[] args) {
		HashSet<ArrayList<String>> allMinConflictSets = new HashSet<ArrayList<String>>();
		
		// read config file
		String filename = "config-all.properties";
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(filename));
		} catch (IOException e) {
			System.err.println("Could not load the configuration file " + filename + ".");
		}
		clingoPath = properties.getProperty("clingo_path", clingoPath);
		programPath = properties.getProperty("program_path", programPath);
		userRequirementsDefinitionPath = properties.getProperty("user_requirements_definition_path", userRequirementsDefinitionPath);
		minConflictSetPath = properties.getProperty("min_conflict_sets_path", minConflictSetPath);
		
		ClingoExecutor clingoExecutor = new ClingoExecutor(clingoPath, programPath, 1);
		QuickXPlain quickXPlain = new QuickXPlain(clingoExecutor);
		
		// parse user requirements definition
		ArrayList<UserRequirementDefinition> userRequirements = parseUserRequirementsDefinition();
		System.out.println("Parsed user requirements definition (" + userRequirements.size() + " items)");
		ArrayList<UserRequirementDefinition> dynamicUserRequirements = new ArrayList<UserRequirementDefinition>();
		for (UserRequirementDefinition userRequirementDefinition : userRequirements) {
			if (userRequirementDefinition.getType().equals("dynamic")) {
				dynamicUserRequirements.add(userRequirementDefinition);
			}
		}
		int numOfDynConstraints = dynamicUserRequirements.size();
		System.out.println(dynamicUserRequirements.size() + " of which are dynamic.");
//		System.out.println(userRequirements.toString());
		
		int numOfPossibleCombinations = 1;
		for (UserRequirementDefinition dynamicUserRequirement : dynamicUserRequirements) {
			numOfPossibleCombinations *= (dynamicUserRequirement.getPossibleValues().size());
		}
		System.out.println("There are " + numOfPossibleCombinations + " possible combinations");
		
		int[] currentPositions = new int[numOfDynConstraints];
		int[] dynSizes = new int[numOfDynConstraints];
		for (int i = 0; i < currentPositions.length; i++) {
			currentPositions[i] = -1;
			dynSizes[i] = dynamicUserRequirements.get(i).getPossibleValues().size() - 1;
		}
		
//		dynSizes = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		
		System.out.println("Current positions: " + Arrays.toString(currentPositions));
		System.out.println("Sizes of dynamic user requirements: " + Arrays.toString(dynSizes));
		
		long startTime = System.currentTimeMillis();
		int iteration = 1;
		
		boolean finished = false;
		while (!finished) {
			printProgress(startTime, numOfPossibleCombinations, iteration);
			
			finished = !shift(currentPositions, dynSizes, currentPositions.length - 1);
//			System.out.println("Current positions: " + Arrays.toString(currentPositions));
			
			ArrayList<String> constraints = new ArrayList<String>();
			int dynUserRequirementIndex = 0;
			
			for (UserRequirementDefinition userRequirementDefinition : userRequirements) {
				String constraint = "";
				if (userRequirementDefinition.getType().equals("dynamic")) {
					if (currentPositions[dynUserRequirementIndex] != -1) {
						constraint = String.format(userRequirementDefinition.getConstraint(), dynamicUserRequirements.get(dynUserRequirementIndex).getPossibleValues().get(currentPositions[dynUserRequirementIndex]));
					}
					dynUserRequirementIndex++;
				} else {
					constraint = userRequirementDefinition.getConstraint();
				}
				constraints.add(constraint);
			}
//			System.out.println(constraints);
			
//			if (!hasKnownConflict(constraints, allMinConflictSets) || !clingoExecutor.isConsistent(constraints)) {
			if (!clingoExecutor.isConsistent(constraints)) {
//				System.out.println("Inconsistent: " + constraints);
				ArrayList<String> minConflictSet = quickXPlain.quickXPlain(constraints, "");
//				System.out.println("Minimal conflict set: " + minConflictSet);
				if (allMinConflictSets.add(minConflictSet)) {
					System.out.println();
					System.out.println("New minimal conflict set (now " + allMinConflictSets.size() + "): " + minConflictSet);
					writeMinConflictsToFile(allMinConflictSets);
				}
			}
			iteration++;
		}
	}
	
	// from https://stackoverflow.com/a/39257969/480370
	private static void printProgress(long startTime, int total, int current) {
		long eta = current == 0 ? 0 : (total - current) * (System.currentTimeMillis() - startTime) / current;

		String etaHms = current == 0 ? "N/A"
				: String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
						TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
						TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1));

		StringBuilder string = new StringBuilder(140);
		int percent = (int) (current * 100 / total);
		string.append('\r')
				.append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
				.append(String.format(" %d%% [", percent)).append(String.join("", Collections.nCopies(percent, "=")))
				.append('>').append(String.join("", Collections.nCopies(100 - percent, " "))).append(']')
				.append(String.join("", Collections.nCopies((int) (Math.log10(total)) - (int) (Math.log10(current)), " ")))
				.append(String.format(" %d/%d, ETA: %s", current, total, etaHms));

		System.out.print(string);
	}
	
	private static boolean shift(int[] currentPositions, int[] dynSizes, int index) {
		while (index >= -1) {
			if (currentPositions[index] < dynSizes[index]) {
				currentPositions[index]++;
				return true;
			} else {
				index--;
				for (int i = index + 1; i < currentPositions.length; i++) {
					currentPositions[i] = -1;
				}
			}
		}
		return false;
	}

	private static ArrayList<UserRequirementDefinition> parseUserRequirementsDefinition() {
		ArrayList<UserRequirementDefinition> userRequirements = new ArrayList<UserRequirementDefinition>();
		
		try {
			JsonReader reader = new JsonReader(new FileReader(userRequirementsDefinitionPath));
			reader.beginObject();
			reader.nextName();
			UserRequirementDefinition[] userRequirementsArray = (new Gson()).fromJson(reader, UserRequirementDefinition[].class);
			reader.close();

			for (int i = 0; i < userRequirementsArray.length; i++) {
				userRequirements.add(userRequirementsArray[i]);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not read from file " + userRequirementsDefinitionPath);
			e.printStackTrace();
			return new ArrayList<UserRequirementDefinition>();
		} catch (IOException e) {
			System.err.println("Could not close file " + userRequirementsDefinitionPath);
			e.printStackTrace();
		}

		return userRequirements;
	}

	private static void writeMinConflictsToFile(HashSet<ArrayList<String>> allMinConflictSets) {
		Writer writer = null;
		Gson gsonWriter = new GsonBuilder().setPrettyPrinting().create();
		try {
			writer = new FileWriter(minConflictSetPath);
			gsonWriter.toJson(allMinConflictSets, writer);
//			System.out.println("Wrote all known minimal conflict sets to the specified file.");
		} catch (IOException e) {
			System.err.println("Could not open file " + minConflictSetPath + " to save minimal conflict sets");
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					System.err.println("Could not close file " + minConflictSetPath);
					e.printStackTrace();
				}
			}
		}
	}
	
//	private static boolean hasKnownConflict(ArrayList<String> newIndividualConstraints, HashSet<ArrayList<String>> allMinConflictSets) {
//		if (getKnownConflict(newIndividualConstraints, allMinConflictSets) == null) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//	
//	private static ArrayList<String> getKnownConflict(ArrayList<String> constraints, HashSet<ArrayList<String>> allMinConflictSets) {
//		for (ArrayList<String> currentConflictSet : allMinConflictSets) {
//			if (constraints.containsAll(currentConflictSet)) {
//				return currentConflictSet;
//			}
//		}
//		
//		return null;
//	}

}
