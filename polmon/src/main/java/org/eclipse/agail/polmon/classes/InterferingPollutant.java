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

public class InterferingPollutant {
	private int		id;
	private String	type = "",
					gas = "",
					electromagnetic = "",
					ionizing = "",
					affectAir = "",
					affectWater = "",
					affectSoil = "",
					affectObjects = "",
					ban = "",
					explosive = "",
					toxic = "",
					dangerous = "",
					poisonous = "",
					metric = "";
	
	public InterferingPollutant(int id) {
		this.id = id;
	}
	public void setId(int id) {
		this.id = id;
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
	public void setExplosive(String explosive) {
		this.explosive = explosive;
	}
	public void setToxic(String toxic) {
		this.toxic = toxic;
	}
	public void setDangerous(String dangerous) {
		this.dangerous = dangerous;
	}
	public void setPoisonous(String poisonous) {
		this.poisonous = poisonous;
	}
	public void setMetric(String metric) {
		this.metric = metric;
	}
}
