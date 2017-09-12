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

public class MonitoringPolicy {
	private int		id;
	private String	autoSampling = "",
					samplingType = "",
					duration = "",
					publishRate = "";
	
	public MonitoringPolicy() {
		
	}
	public MonitoringPolicy(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setAutoSampling(String autoSampling) {
		this.autoSampling = autoSampling;
	}
	public void setSamplingType(String samplingType) {
		this.samplingType = samplingType;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public void setPublishRate(String publishRate) {
		this.publishRate = publishRate;
	}
	public void setId(int id) {
		this.id = id;
	}
}
