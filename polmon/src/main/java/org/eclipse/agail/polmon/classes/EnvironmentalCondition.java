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

public class EnvironmentalCondition {
	private int 	id;
	private String	humidity = "",
					windSpeed = "",
					rain = "",
					dust = "",
					avgTemperature = "",
					snow = "",
					ice = "",
					vibrations = "",
					avgPressure = "";
	
	public EnvironmentalCondition(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public String getHumidity() {
		return humidity;
	}
	public String getWindSpeed() {
		return windSpeed;
	}
	public String getRain() {
		return rain;
	}
	public String getDust() {
		return dust;
	}
	public String getAvgTemperature() {
		return avgTemperature;
	}
	public String getSnow() {
		return snow;
	}
	public String getIce() {
		return ice;
	}
	public String getVibrations() {
		return vibrations;
	}
	public String getAvgPressure() {
		return avgPressure;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public void setWindSpeed(String windSpeed) {
		this.windSpeed = windSpeed;
	}
	public void setRain(String rain) {
		this.rain = rain;
	}
	public void setDust(String dust) {
		this.dust = dust;
	}
	public void setAvgTemperature(String avgTemperature) {
		this.avgTemperature = avgTemperature;
	}
	public void setSnow(String snow) {
		this.snow = snow;
	}
	public void setIce(String ice) {
		this.ice = ice;
	}
	public void setVibrations(String vibrations) {
		this.vibrations = vibrations;
	}
	public void setAvgPressure(String avgPressure) {
		this.avgPressure = avgPressure;
	}
}
