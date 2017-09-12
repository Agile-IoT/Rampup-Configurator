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

package at.tugraz.ist.ase.polmon;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import at.tugraz.ist.ase.polmon.rest.SolverSvc;

public class Main {
	private static int serverPort = 8081;

	public static void main(String[] args) {
		String filename = "config.properties";
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(filename));
		} catch (IOException e) {
			System.err.println("Could not load the configuration file " + filename + ".");
		}
		serverPort = Integer.parseInt(properties.getProperty("server_port"));
		
		ServletContextHandler restHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		restHandler.setContextPath("/");
		ServletHolder jerseyServlet = restHandler.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
		jerseyServlet.setInitOrder(0);
		
		String restClasses = SolverSvc.class.getCanonicalName();
		jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", restClasses);
		
		// handler to serve static files
		ResourceHandler staticHandler = new ResourceHandler();
		staticHandler.setResourceBase("static");
		staticHandler.setDirectoriesListed(true);
		staticHandler.setWelcomeFiles(new String[]{"index.html"});
		
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
