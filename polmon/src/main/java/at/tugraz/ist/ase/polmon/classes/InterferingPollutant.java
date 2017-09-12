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
