package at.tugraz.ist.ase.genconfgen;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Main {
	private static String clingoPath = "clingo";
	private static String programPath = "kb.lp";
	private static String userRequirementsDefinitionPath = "user_requirements_definition.json";
	private static String minConflictSetPath = "min_conflict_sets.json";
	private static double initNoPreference = 0.9;
	private static int populationSize = 100;
	private static int numOfPopulations = 1;
	private static double mutationProbability = 0.1;

	public static void main(String[] args) {
		HashSet<ArrayList<String>> allMinConflictSets;
		boolean doStop = false;
		int generation = 0;
		
		// read config file
		String filename = "config.properties";
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
		initNoPreference = Double.parseDouble(properties.getProperty("init_no_preference", initNoPreference + ""));
		populationSize = Integer.parseInt(properties.getProperty("population_size", populationSize + ""));
		numOfPopulations = Integer.parseInt(properties.getProperty("num_of_populations", numOfPopulations + ""));
		mutationProbability = Double.parseDouble(properties.getProperty("mutation_probability", mutationProbability + ""));
		
		// parse user requirements definition
		ArrayList<UserRequirementDefinition> userRequirements = parseUserRequirementsDefinition();
		System.out.println("Parsed user requirements definition (" + userRequirements.size() + " items)");
		ArrayList<UserRequirementDefinition> dynamicUserRequirements = new ArrayList<UserRequirementDefinition>();
		for (UserRequirementDefinition userRequirementDefinition : userRequirements) {
			if (userRequirementDefinition.getType().equals("dynamic")) {
				dynamicUserRequirements.add(userRequirementDefinition);
			}
		}
		System.out.println(dynamicUserRequirements.size() + " of which are dynamic.");
//		System.out.println(userRequirements.toString());
		
		// get number of dynamic constraints
		int numOfDynConstraints = 0;
		for (UserRequirementDefinition userRequirementDefinition : userRequirements) {
			if (userRequirementDefinition.getType().equals("dynamic"))
				numOfDynConstraints++;
		}
//		System.out.println("There are " + numOfDynConstraints + " dynamic constraints");
		
		// deserialize existing minimal conflict sets
		Gson minConflictSetGson = new Gson();
		Type minConflictSetType = new TypeToken<HashSet<ArrayList<String>>>() {}.getType();
		try {
			allMinConflictSets = minConflictSetGson.fromJson(new JsonReader(new FileReader(minConflictSetPath)), minConflictSetType);
		} catch (JsonIOException e) {
			System.err.println("Could not read file " + minConflictSetPath);
			e.printStackTrace();
			allMinConflictSets = new HashSet<ArrayList<String>>();
			System.out.println("Starting with an empty set of minimal conflict sets ...");
		} catch (JsonSyntaxException e) {
			System.err.println("File " + minConflictSetPath + " does not contain valid JSON");
			e.printStackTrace();
			allMinConflictSets = new HashSet<ArrayList<String>>();
			System.out.println("Starting with an empty set of minimal conflict sets ...");
		} catch (FileNotFoundException e) {
			System.err.println("File " + minConflictSetPath + " not found");
			e.printStackTrace();
			allMinConflictSets = new HashSet<ArrayList<String>>();
			System.out.println("Starting with an empty set of minimal conflict sets ...");
		}
		
		// randomize a population
		// TODO do not generate known conflicts
		System.out.println("GENERATION " + generation + ": Randomizing a starting population of " + populationSize + " individuals with a no-preference-probability of " + initNoPreference + " ...");
		String[][] population = new String[populationSize][numOfDynConstraints];
		for (int i = 0; i < populationSize; i++) {
			int j = 0;
			for (UserRequirementDefinition userRequirementDefinition : userRequirements) {
				if (userRequirementDefinition.getType().equals("dynamic")) {
					if (Math.random() <= (1 - initNoPreference)) {
						if (userRequirementDefinition.getLowerBound() == Integer.MIN_VALUE && userRequirementDefinition.getUpperBound() == Integer.MAX_VALUE) {
							ArrayList<String> possibleValues = userRequirementDefinition.getPossibleValues();
							population[i][j] = possibleValues.get(new Random().nextInt(possibleValues.size()));
						} else {
							population[i][j] = (new Random().nextInt(userRequirementDefinition.getUpperBound() - userRequirementDefinition.getLowerBound() + 1) + userRequirementDefinition.getLowerBound()) + "";
						}
					} else {
						population[i][j] = "";
					}
					
					j++;
				}
			}
//			System.out.println(Arrays.toString(population[i]));
		}
		
		while (!doStop) {
			// check all individuals for conflicts
			System.out.println("GENERATION " + generation + ": Checking all individuals for conflicts ...");
			ClingoExecutor clingoExecutor = new ClingoExecutor(clingoPath, programPath, 1);
			ArrayList<Integer> conflictIndices = new ArrayList<Integer>();
			ArrayList<ArrayList<String>> conflictSets = new ArrayList<ArrayList<String>>();
			for (int i = 0; i < populationSize; i++) {
				ArrayList<String> constraints = new ArrayList<String>();
				int j = 0;
				for (UserRequirementDefinition userRequirementDefinition : userRequirements) {
					if (userRequirementDefinition.getType().equals("dynamic")) {
						if (!population[i][j].equals("")) {
							constraints.add(String.format(userRequirementDefinition.getConstraint(), population[i][j]));
						}
						j++;
					} else {
						constraints.add(userRequirementDefinition.getConstraint());
					}
				}
				if (!clingoExecutor.isConsistent(constraints)) {
//					System.out.println("Conflicting individual " + i + ": " + constraints.toString());
					conflictIndices.add(i);
					conflictSets.add(constraints);
				}
			}
			System.out.println("GENERATION " + generation + ": Found " + conflictSets.size() + " conflicting individuals.");
			
			// determine (all) minimal conflict sets of all conflicting individuals
			// TODO create alternate minimal conflict sets
			System.out.println("GENERATION " + generation + ": Generating minimal conflict sets for conflicting individuals ...");
			QuickXPlain quickXPlain = new QuickXPlain(clingoExecutor);
			ArrayList<ArrayList<String>> minConflictSets = new ArrayList<ArrayList<String>>();
//			int i = 0;
			for (ArrayList<String> conflictSet : conflictSets) {
				ArrayList<String> minConflictSet = quickXPlain.quickXPlain(conflictSet, "");
				minConflictSets.add(minConflictSet);
//				System.out.println("Minimal conflict set for individual " + conflictIndices.get(i) + ": " + minConflictSet);
//				i++;
			}
			System.out.println("GENERATION " + generation + ": Found " + minConflictSets.size() + " unique minimal conflict sets in this round.");
			int knownMinConflictsBefore = allMinConflictSets.size();
			allMinConflictSets.addAll(minConflictSets);
			System.out.println("GENERATION " + generation + ": Found " + (allMinConflictSets.size() - knownMinConflictsBefore)
					+ " globally new minimal conflict sets in this round.");
			// save all known minimal conflict sets to a file
			writeMinConflictsToFile(allMinConflictSets);
			// creating healed individuals by cloning conflicting individuals times the number
			// of entries in the respective minimal conflict set and healing each of these
			// individuals by removing one part of the respective minimal conflict set
			ArrayList<ArrayList<String>> healedConflictSets = new ArrayList<ArrayList<String>>();
			for (int j = 0; j < minConflictSets.size(); j++) {
				for (String minConflictSetPart : minConflictSets.get(j)) {
					ArrayList<String> healedConflictSet = new ArrayList<String>(conflictSets.get(j));
					healedConflictSet.remove(minConflictSetPart);
					healedConflictSets.add(healedConflictSet);
				}
			}
			System.out.println("GENERATION " + generation + ": Created " + healedConflictSets.size() + " healed individuals from "
					+ conflictSets.size() + " conflicting individuals.");
			//		for (ArrayList<String> testIndividual : healedConflictSets) {
			//			System.out.println(testIndividual.toString());
			//			if (clingoExecutor.isConsistent(testIndividual)) {
			//				System.out.println("TESTING: YES");
			//			} else {
			//				System.out.println("TESTING: OH NO! °_°");
			//			}
			//		}
			// put them into array notation
			String[][] parentGeneration;
			//if (!healedConflictSets.isEmpty() && binomial(healedConflictSets.size(), 2) > populationSize / 2) {
			if (!healedConflictSets.isEmpty()) {
				parentGeneration = new String[healedConflictSets.size()][numOfDynConstraints];
				for (int j = 0; j < parentGeneration.length; j++) {
					int arrayCounter = 0;
					int arrayListCounter = 0;
					for (UserRequirementDefinition userRequirementDefinition : userRequirements) {
						if (userRequirementDefinition.getType().equals("dynamic")) {
							if (arrayListCounter < healedConflictSets.get(j).size()
									&& userRequirementDefinition.getConstraint().split("\\(")[0]
											.equals(healedConflictSets.get(j).get(arrayListCounter).split("\\(")[0])) {
								// put value of healedConflictSets[j][arrayListCounter] into parentGeneration[j][arrayCounter]
								parentGeneration[j][arrayCounter] = healedConflictSets.get(j).get(arrayListCounter)
										.split(",")[1].split("\\)")[0];
								//							System.out.println("parentGeneration[" + j + "][" + arrayCounter + "] = " + parentGeneration[j][arrayCounter]);
								arrayListCounter++;
							} else {
								parentGeneration[j][arrayCounter] = "";
							}
							arrayCounter++;
						} else {
							arrayListCounter++;
						}
					}
//					System.out.println(Arrays.toString(parentGeneration[j]));
				}

				// randomly crossover so that a defined population size is reached
				System.out.println("GENERATION " + generation + ": Generating a new generation of " + populationSize + " individuals with genetic crossover ...");
				population = new String[populationSize][numOfDynConstraints];
				for (int j = 0; j < population.length; j++) {
					int fatherIndex = 0;
					int motherIndex = 0;

					while (fatherIndex == motherIndex) {
						fatherIndex = new Random().nextInt(parentGeneration.length);
						motherIndex = new Random().nextInt(parentGeneration.length);
					}

					String[] newIndividual = geneticCrossover(parentGeneration[fatherIndex], parentGeneration[motherIndex]);
					// check if this contains a known minimal conflict set before adding to the new population
					ArrayList<String> newIndividualConstraints = new ArrayList<String>();
					for (int k = 0; k < newIndividual.length; k++) {
						if (!newIndividual[k].equals("")) {
							newIndividualConstraints.add(String.format(dynamicUserRequirements.get(k).getConstraint(), newIndividual[k]));
						}
					}
					if (!hasKnownConflict(newIndividualConstraints, allMinConflictSets)) {
						population[j] = newIndividual;
//						System.out.println(Arrays.toString(population[j]));
					} else {
						j--;
					}
				}

				// mutate the new generation
				System.out.println("GENERATION " + generation + ": Mutating new generation ...");
				mutateGeneration(population, dynamicUserRequirements);
				generation++;
			} else {
//				doStop = true;
//				System.err.println("GENERATION " + generation + ": Stopping due to an insufficient number of parents to generate new generation.");
				System.out.println("GENERATION " + generation + ": Mutating and repeating this generation due to no conflicts ...");
				mutateGeneration(population, dynamicUserRequirements);
			}
		}
	}

	private static void mutateGeneration(String[][] population, ArrayList<UserRequirementDefinition> dynamicUserRequirements) {
		for (int j = 0; j < population.length; j++) {
			for (int k = 0; k < population[j].length; k++) {
				if (Math.random() <= mutationProbability) {
					if (Math.random() <= (1 - initNoPreference)) {
						UserRequirementDefinition userRequirementDefinition = dynamicUserRequirements.get(k);
						if (userRequirementDefinition.getLowerBound() == Integer.MIN_VALUE && userRequirementDefinition.getUpperBound() == Integer.MAX_VALUE) {
							ArrayList<String> possibleValues = userRequirementDefinition.getPossibleValues();
							population[j][k] = possibleValues.get(new Random().nextInt(possibleValues.size()));
						} else {
							population[j][k] = (new Random().nextInt(userRequirementDefinition.getUpperBound() - userRequirementDefinition.getLowerBound() + 1) + userRequirementDefinition.getLowerBound()) + "";
						}
					} else {
						population[j][k] = "";
					}
				}
			}
		}
	}

	private static boolean hasKnownConflict(ArrayList<String> newIndividualConstraints, HashSet<ArrayList<String>> allMinConflictSets) {
		// parse new individual into right format
		for (ArrayList<String> currentConflictSet : allMinConflictSets) {
			for (String constraint : currentConflictSet) {
				if (!newIndividualConstraints.contains(constraint)) {
					break;
				}
				return true;
			}
		}
		
		return false;
	}

	private static String[] geneticCrossover(String[] father, String[] mother) {
		String[] child = new String[father.length];
		
		for (int i = 0; i < child.length; i++) {
			if (father[i].equals(mother[i])) {
				child[i] = father[i];
			} else {
				if (new Random().nextBoolean()) {
					child[i] = father[i];
				} else {
					child[i] = mother[i];
				}
			}
		}
		
		return child;
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
	
//	private static long binomial(int n, int k) {
//		if (k > n - k) {
//			k = n - k;
//		}
//		
//		long b = 1;
//		
//		for (int i = 1, m = n; i <= k; i++, m--) {
//			b = b * m / i;
//		}
//		
//		return b;
//	}
}
