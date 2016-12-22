package at.tugraz.ist.ase.smarthome.classes;

import java.util.ArrayList;

public class UserRequirements {
	private boolean		isComfortControlNeeded,
						isEnergySavingNeeded,
						isHealthSupportNeeded,
						isSafetySecurityNeeded,
						isCostImportant,
						isStabilityNeeded,
						isSensibleToElectricSmog;
	private SmartHome	smartHome;
	private String		installation;				// DIY, Professional
	
	private static ArrayList<String> installationTypeList = new ArrayList<String>() {
		private static final long serialVersionUID = 9148271970660121738L;
		{
			add("DIY");
			add("Professional");
		}
	};
	
	public String generateConstraints() {
		String constraints = "";
		
		constraints += "ooasp_cv(I,u_comfort_control(Comfort)):-"
				+ "ooasp_configuration(\"v1\",I),"
				+ "ooasp_attribute_value(I,\"UserRequirements_isComfortControlNeeded\",Comfort,\""
				+ Boolean.toString(!isComfortControlNeeded) + "\").\n";
		constraints += "ooasp_cv(I,u_energy_saving(Energy)):-"
				+ "ooasp_configuration(\"v1\",I),"
				+ "ooasp_attribute_value(I,\"UserRequirements_isEnergySavingNeeded\",Energy,\""
				+ Boolean.toString(!isEnergySavingNeeded) + "\").\n";
		constraints += "ooasp_cv(I,u_health_support(Support)):-"
				+ "ooasp_configuration(\"v1\",I),"
				+ "ooasp_attribute_value(I,\"UserRequirements_isHealthSupportNeeded\",Support,\""
				+ Boolean.toString(!isHealthSupportNeeded) + "\").\n";
		constraints += "ooasp_cv(I,u_safety_security(Security)):-"
				+ "ooasp_configuration(\"v1\",I),"
				+ "ooasp_attribute_value(I,\"UserRequirements_isSafetySecurityNeeded\",Security,\""
				+ Boolean.toString(!isSafetySecurityNeeded) + "\").\n";
		constraints += "ooasp_cv(I,u_cost_important(Cost)):-"
				+ "ooasp_configuration(\"v1\",I),"
				+ "ooasp_attribute_value(I,\"UserRequirements_isCostImportant\",Cost,\""
				+ Boolean.toString(!isCostImportant) + "\").\n";
		constraints += "ooasp_cv(I,u_stability_needed(Stability)):-"
				+ "ooasp_configuration(\"v1\",I),"
				+ "ooasp_attribute_value(I,\"UserRequirements_isStabilityNeeded\",Stability,\""
				+ Boolean.toString(!isStabilityNeeded) + "\").\n";
		constraints += "ooasp_cv(I,u_esmog_sensible(ESmog)):-"
				+ "ooasp_configuration(\"v1\",I),"
				+ "ooasp_attribute_value(I,\"UserRequirements_isSensibleToElectricSmog\",ESmog,\""
				+ Boolean.toString(!isSensibleToElectricSmog) + "\").\n";
		if (smartHome != null) {
			constraints += "ooasp_cv(I,u_tubes_installed(Tubes)):-" + "ooasp_configuration(\"v1\",I),"
					+ "ooasp_attribute_value(I,\"SmartHome_areTubesEnabled\",Tubes,\""
					+ Boolean.toString(!smartHome.areTubesInstalled()) + "\").\n";
		}
		// only show communication types selected by user
		if (smartHome != null && smartHome.getCommunication() != null) {
			constraints += "ooasp_cv(I,u_communication(Communication)):-" + "ooasp_configuration(\"v1\",I),";
			for (String communication : smartHome.getCommunicationList()) {
				if (!communication.equals(smartHome.getCommunication())) {
					constraints += "ooasp_attribute_value(I,\"SmartHome_communication\",Communication,\""
							+ communication + "\"),";
				}
			}
			constraints = constraints.substring(0, constraints.length() - 1);
			constraints += ".\n";
		}
		
		// only show materials selected by user
		if (smartHome != null && smartHome.getBuiltWith() != null) {
			constraints += "ooasp_cv(I,u_built_with(Built)):-" + "ooasp_configuration(\"v1\",I),";
			for (String builtWith : smartHome.getBuiltWithList()) {
				if (!builtWith.equals(smartHome.getBuiltWith())) {
					constraints += "ooasp_attribute_value(I,\"SmartHome_builtWith\",Built,\"" + builtWith + "\"),";
				}
			}
			constraints = constraints.substring(0, constraints.length() - 1);
			constraints += ".\n";
		}
		
		// TODO smart home age
		
		// TODO rooms
		
		// TODO home appliances
		
		// constraint for installation type
		constraints += "ooasp_cv(I,u_installation(Installation)):-"
				+ "ooasp_configuration(\"v1\",I),";
		for (String installationType : installationTypeList) {
			if (!installationType.equals(installation)) {
				constraints += "ooasp_attribute_value(I,\"UserRequirements_installation\",Installation,\""
						+ installationType + "\"),";
			}
		}
		constraints = constraints.substring(0, constraints.length() - 1);
		constraints += ".\n";
		
		return constraints;
	}
}
