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

public class ReferenceModel {
	private int		id;
	private String	monitoringAlgorithm = "";
	
	public ReferenceModel(int id) {
		this.id = id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setMonitoringAlgorithm(String monitoringAlgorithm) {
		this.monitoringAlgorithm = monitoringAlgorithm;
	}
}
