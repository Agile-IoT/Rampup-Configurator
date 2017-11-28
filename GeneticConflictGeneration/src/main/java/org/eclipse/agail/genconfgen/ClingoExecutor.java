/*******************************************************************************
 * Copyright (C) 2017 TUGraz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     TUGraz - initial implementation
 ******************************************************************************/

package at.tugraz.ist.ase.genconfgen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ClingoExecutor {
	private String pathToClingo;
	private String pathToProgram;
	private int numOfResults;

	public ClingoExecutor(String pathToClingo, String pathToProgram, int numOfResults) {
		this.pathToClingo = pathToClingo;
		this.pathToProgram = pathToProgram;
		this.numOfResults = numOfResults;
	}
	
//	public ArrayList<String> createDiagnosis (MonitoringStation userRequirements, ArrayList<String> preferredRequirementsList) {
//		ArrayList<String> userConstraintList = createUserConstraintList(userRequirements);
//		
//		// if isEmpty(C) or inconsistent(AC � C) return null
//		if (userConstraintList.isEmpty() || !isConsistent(getClingoProgram())) {
//			return null;
//		// else return FD(null, C, AC);
//		} else {
//			ArrayList<String> ac = new ArrayList<String>();
//			ac.add(getClingoProgram());
//			ac.addAll(userConstraintList);
//			
//			if (!preferredRequirementsList.isEmpty()) {
//				for (String preferredRequirement : preferredRequirementsList) {
//					priorizeRequirement(preferredRequirement, ac, userConstraintList);
//				}
//			}
//			return fastDiag(null, userConstraintList, ac);
//		}
//	}
//	
//	private void priorizeRequirement(String preferredRequirement, ArrayList<String> ac, ArrayList<String> userConstraintList) {
//		String matchedRequirement = "";
//		
//		for (String userRequirement : ac) {
//			if (userRequirement.matches(preferredRequirement)) {
//				matchedRequirement = userRequirement;
//				break;
//			}
//		}
//		
//		if (ac.remove(matchedRequirement) && userConstraintList.remove(matchedRequirement)) {
//			ac.add(matchedRequirement);
//			userConstraintList.add(matchedRequirement);
//		} else {
//			System.err.println("ERROR: Could not remove matched requirement '" + matchedRequirement + "' from one or both of the constraint lists.");
//		}
//	}
	
	private boolean isConsistent(String program) {
		program = getClingoProgram() + program;
		
		if (executeClingo(program).contains("Answer: 1")) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isConsistent(ArrayList<String> constraints) {
		String program = "";
		for (String constraint : constraints) {
			program += constraint + "\n";
		}
		return isConsistent(program);
	}
	
//	private ArrayList<String> subtract(ArrayList<String> first, ArrayList<String> second) {
//		@SuppressWarnings("unchecked")
//		ArrayList<String> result = (ArrayList<String>) first.clone();
//		if (second != null) {
//			result.removeAll(second);
//		}
//		return result;
//	}
//	
//	private ArrayList<String> union(ArrayList<String> first, ArrayList<String> second) {
//		if (second != null) {
//			if (first != null) {
//				@SuppressWarnings("unchecked")
//				ArrayList<String> result = (ArrayList<String>) first.clone();
//				for (String constraint : second) {
//					if (!result.contains(constraint)) {
//						result.add(constraint);
//					}
//				}
//				return result;
//			} else {
//				return second;
//			}
//		} else {
//			return first;
//		}
//	}
	
	private String getClingoProgram() {
		String result = "";
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			String currentLine;
			
			fr = new FileReader(pathToProgram);
			br = new BufferedReader(fr);
			
			while ((currentLine = br.readLine()) != null) {
				result += currentLine + "\n";
			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not find file " + pathToProgram);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not read file " + pathToProgram);
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				System.err.println("Could not close file " + pathToProgram);
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	private String executeClingo(String program) {
		String result = "";
		
		try {
			ProcessBuilder processBuilder;
			if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0)
				processBuilder = new ProcessBuilder("cmd.exe", "/c", pathToClingo + " -n" + numOfResults);
			else
				processBuilder = new ProcessBuilder("/bin/sh", "-c", pathToClingo + " -n" + numOfResults);
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			
			writer.write(program + "\n");
			writer.flush();
			writer.close();
			
			while (true) {
				String line = reader.readLine();
				if (line == null)
					break;
				result += line + "\n";
				
			}
		} catch (IOException e) {
			System.err.println("IOException when trying to execute Clingo");
			e.printStackTrace();
		}
		
		return result;
	}
	
//	private ArrayList<String> fastDiag(ArrayList<String> d, ArrayList<String> c, ArrayList<String> ac) {
//		// if D != null and consistent(AC) return null;
//		if (d != null && isConsistent(ac)) {
//			return null;
//		}
//		// if singleton(C) return C;
//		if (isSingleton(c)) {
//			return c;
//		}
//		// k = q/2;
//		int q = c.size();
//		int k = q / 2;
//		// C1 = {c1..ck}; C2 = {ck+1..cq};
//		ArrayList<String> c1 = new ArrayList<String>();
//		ArrayList<String> c2 = new ArrayList<String>();
//		c1.addAll(c.subList(0, k));
//		c2.addAll(c.subList(k, q));
//		// D1 = FD(C1, C2, AC - C1);
//		ArrayList<String> d1 = fastDiag(c1, c2, subtract(ac, c1));
//		// D2 = FD(D1, C1, AC - D1);
//		ArrayList<String> d2 = fastDiag(d1, c1, subtract(ac, d1));
//		
//		// return(D1 U D2);
//		return union(d1, d2);
//	}
//	
//	private boolean isSingleton(ArrayList<String> c) {
//		if (c.size() == 1) {
//			return true;
//		} else {
//			return false;
//		}
//	}
}
