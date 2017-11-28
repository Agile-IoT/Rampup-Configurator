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
