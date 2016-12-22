package at.tugraz.ist.ase.smarthome;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import at.tugraz.ist.ase.smarthome.clingo.ClingoExecutor;
import at.tugraz.ist.ase.smarthome.rest.SolverSvc;

public class Main {
	private static int serverPort = 8080;
	public static String pathToClingo = "clingo";
	public static String programDirectory = "";
	public static String mainFile = "";
	public static String clingoCall = "";
	
	public static ClingoExecutor clingoExecutor;

	public static void main(String[] args) {
		String configFile = "config.properties";
		if (args.length > 0) {
			configFile = args[0];
		}
		
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(configFile));
		} catch (FileNotFoundException e) {
			System.err.println("Properties file " + configFile + " not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Could not read properties file " + configFile + " .");
			e.printStackTrace();
		}
		
		// get values from configuration file
		serverPort = Integer.parseInt(properties.getProperty("server_port", Integer.toString(serverPort)));
		pathToClingo = properties.getProperty("path_to_clingo");
		programDirectory = properties.getProperty("program_directory");
		mainFile = properties.getProperty("main_file");
		clingoCall = properties.getProperty("clingo_call");
		
		clingoExecutor = new ClingoExecutor(pathToClingo, programDirectory, mainFile);
		
		// handler to serve web service
		ServletContextHandler restHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		restHandler.setContextPath("/");
		ServletHolder jerseyServlet = restHandler.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		
		String restClasses = SolverSvc.class.getCanonicalName();
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", restClasses);
		
		// handler to serve static files
		ResourceHandler staticHandler = new ResourceHandler();
		staticHandler.setDirectoriesListed(false);
		staticHandler.setWelcomeFiles(new String[]{"index.html"});
		staticHandler.setResourceBase("static/");
		
		// create server and add handlers
		HandlerList handlers = new HandlerList();
		handlers.addHandler(staticHandler);
		handlers.addHandler(restHandler);
		Server jettyServer = new Server(serverPort);
		jettyServer.setHandler(handlers);
		
		// start jetty server
		try {
			jettyServer.start();
		} catch (Exception e) {
			System.err.println("Could not start jetty server.");
			e.printStackTrace();
		}
	}
}
