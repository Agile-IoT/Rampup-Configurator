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

package at.tugraz.ist.ase.polmon.classes;

import java.util.ArrayList;

public class DeploymentEnvironment {
	private int						id;
	private String					type = "",
									context = "",
									locationType = "";
	private ArrayList<Area>			areas = null;
	private ArrayList<Pollutant>	pollutants = null;
	
	public DeploymentEnvironment(int id) {
		this.id = id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public String getType() {
		return type;
	}
	public String getContext() {
		return context;
	}
	public String getLocationType() {
		return locationType;
	}
	public ArrayList<Area> getAreas() {
		if (areas == null) {
			areas = new ArrayList<Area>();
		}
		return areas;
	}
	public ArrayList<Pollutant> getPollutants() {
		if (pollutants == null) {
			pollutants = new ArrayList<Pollutant>();
		}
		return pollutants;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}
	public void addArea(Area area) {
		if (this.areas == null) {
			areas = new ArrayList<Area>();
		}
		areas.add(area);
	}
	public void addPollutant(Pollutant pollutant) {
		if (pollutants == null) {
			pollutants = new ArrayList<Pollutant>();
		}
		pollutants.add(pollutant);
	}
}
