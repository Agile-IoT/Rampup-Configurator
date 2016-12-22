package at.tugraz.ist.ase.smarthome.clingo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ClingoExecutor {
	private String pathToClingo = "";
	private String programDirectory = "";
	private String mainFile = "";
	
	public ClingoExecutor(String pathToClingo, String programDirectory, String mainFile) {
		this.pathToClingo = pathToClingo;
		this.programDirectory = programDirectory;
		this.mainFile = mainFile;
	}
	
	public String getClingoOutput(String userConstraints) {
		String result = "";
		String program = "";
		
		// fill program according to files
		program = concatProgram(programDirectory + mainFile);
		
		// TODO add user generated constraints
		program += userConstraints;
		
		System.err.println(program);
		
		// run clingo with contents of program
		result = executeClingo(program);
		
		return result;
	}
	
	private String executeClingo(String program) {
		String result = "";
		
		try {
			ProcessBuilder processBuilder;
			if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
				processBuilder = new ProcessBuilder("cmd.exe", "/c", pathToClingo + " -n0");
			} else {
				processBuilder = new ProcessBuilder("/bin/sh", "-c", pathToClingo + " -n0");
			}
			processBuilder.redirectErrorStream(true);
			Process process = processBuilder.start();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
			
			writer.write(program + "\u001a\n");
			writer.flush();
			writer.close();
			
			String cmdOutput = "";
			
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				cmdOutput += line + " ";
			}
			
			result = cmdOutput;
		} catch (IOException e) {
			System.err.println("IOException when trying to execute Clingo");
			e.printStackTrace();
		}
		
		return result;
	}
	
	private String concatProgram(String pathToFile) {
		String result = "";
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			String currentLine;
			
			fr = new FileReader(pathToFile);
			br = new BufferedReader(fr);
			
			while ((currentLine = br.readLine()) != null) {
				if (!currentLine.equals("") && !currentLine.startsWith("%")) {
					if (currentLine.startsWith("#include ")) {
						result += concatProgram(programDirectory + currentLine.split("\"")[1]);
					} else {
						result += currentLine + "\n";
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not find file " + pathToFile);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not read file " + pathToFile);
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
				System.err.println("Could not close file " + pathToFile);
				e.printStackTrace();
			}
		}
		
		return result;
	}
}
