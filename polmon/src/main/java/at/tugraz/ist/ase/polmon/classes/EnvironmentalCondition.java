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
