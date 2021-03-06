/*********************************************************************
* Copyright (c) 2017-11-28 Christoph Uran (TU Graz)
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.agail.polmon.classes;

import java.util.ArrayList;

public class Pollutant {
	private int								id;
	private String							type = "",
											gas = "",
											electromagnetic = "",
											ionizing = "",
											affectAir = "",
											affectWater = "",
											affectSoil = "",
											affectObjects = "",
											ban = "",
											specific = "",
											metric = "",
											rawMetric = "";
	private ArrayList<InterferingPollutant>	interferingPollutants = null;
	private ArrayList<Sensor>				sensors = null;
	
	public Pollutant(int id) {
		this.id = id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ArrayList<InterferingPollutant> getInterferingPollutants() {
		if (interferingPollutants == null) {
			interferingPollutants = new ArrayList<InterferingPollutant>();
		}
		return interferingPollutants;
	}
	public ArrayList<Sensor> getSensors() {
		if (sensors == null) {
			sensors = new ArrayList<Sensor>();
		}
		return sensors;
	}
	public int getId() {
		return id;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setGas(String gas) {
		this.gas = gas;
	}
	public void setElectromagnetic(String electromagnetic) {
		this.electromagnetic = electromagnetic;
	}
	public void setIonizing(String ionizing) {
		this.ionizing = ionizing;
	}
	public void setAffectAir(String affectAir) {
		this.affectAir = affectAir;
	}
	public void setAffectWater(String affectWater) {
		this.affectWater = affectWater;
	}
	public void setAffectSoil(String affectSoil) {
		this.affectSoil = affectSoil;
	}
	public void setAffectObjects(String affectObjects) {
		this.affectObjects = affectObjects;
	}
	public void setBan(String ban) {
		this.ban = ban;
	}
	public void setSpecific(String specific) {
		this.specific = specific;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
	public void setRawMetric(String rawMetric) {
		this.rawMetric = rawMetric;
	}
	public void addInterferingPollutant(InterferingPollutant interferingPollutant) {
		if (interferingPollutants == null) {
			interferingPollutants = new ArrayList<InterferingPollutant>();
		}
		interferingPollutants.add(interferingPollutant);
	}
	public void addSensor(Sensor sensor) {
		if (sensors == null) {
			sensors = new ArrayList<Sensor>();
		}
		sensors.add(sensor);
	}
}
