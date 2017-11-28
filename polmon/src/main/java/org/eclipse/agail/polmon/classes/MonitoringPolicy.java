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
