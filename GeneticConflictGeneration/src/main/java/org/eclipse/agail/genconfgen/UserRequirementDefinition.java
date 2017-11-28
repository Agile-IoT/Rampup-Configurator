/*********************************************************************
* Copyright (c) 2017-11-28 Christoph Uran (TU Graz)
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.agail.genconfgen;

import java.util.ArrayList;

public class UserRequirementDefinition {
	private String				type = null;
	private String				constraint = null;
	private ArrayList<String>	possibleValues = null;
	private int					lowerBound = Integer.MIN_VALUE;
	private int					upperBound = Integer.MAX_VALUE;
	
	public String getType() {
		return type;
	}
	public String getConstraint() {
		return constraint;
	}
	public ArrayList<String> getPossibleValues() {
		if (possibleValues == null || possibleValues.isEmpty()) {
			return new ArrayList<String>();
		} else {
			return possibleValues;
		}
	}
	public int getLowerBound() {
		return lowerBound;
	}
	public int getUpperBound() {
		return upperBound;
	}
	
	public String toString() {
		String returnString = "";
		
		returnString += type + " constraint '" + constraint + "' ";
		if (!type.equals("static")) {
			if (lowerBound == Integer.MIN_VALUE && upperBound == Integer.MAX_VALUE) {
				returnString += "with possible values " + possibleValues.toString();
			} else {
				returnString += "with an upper bound of " + upperBound + " and a lower bound of " + lowerBound;
			}
		}
		
		return returnString;
	}
}
