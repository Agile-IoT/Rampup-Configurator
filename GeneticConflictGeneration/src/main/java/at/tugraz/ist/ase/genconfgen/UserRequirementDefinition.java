/*******************************************************************************
 * Copyright (C) 2017 TUGraz.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     TUGraz - initial implementation
 ******************************************************************************/

package at.tugraz.ist.ase.genconfgen;

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
