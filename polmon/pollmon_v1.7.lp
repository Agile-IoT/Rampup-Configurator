% Copyright (c) 2017-11-28 Seda Polat-Erdeniz & Christoph Uran (TU Graz)
%
% This program and the accompanying materials are made
% available under the terms of the Eclipse Public License 2.0
% which is available at https://www.eclipse.org/legal/epl-2.0/
%
% SPDX-License-Identifier: EPL-2.0

% to execute the program please use Clingo from http://potassco.sourceforge.net/
% which is combines Gringo and Claps in a monolithic way;
% use the following command line:  clingo.exe pollmon_v1.7.lp
% implemented by Seda Polat Erdeniz
% last edit 02.06.2017 by SPE

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% types of CLASSES
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% configuration for 1 station
type_monitoringstation(1).

% 1 station has many deployment environments : max 1
#const max_deploymentenvironments = 1.
#const number_deploymentenvironments = max_deploymentenvironments.
potentialinstance_deploymentenvironment(2..number_deploymentenvironments+1).
0{type_deploymentenvironment(X)}1 :- potentialinstance_deploymentenvironment(X).


% 1 deployment environment has many areas: max 1
#const max_areas = 1.
#const number_areas = number_deploymentenvironments * max_areas.
#const s2 = 3.
potentialinstance_area(s2..s2+number_areas-1).
0{type_area(X)}1 :- potentialinstance_area(X).


% 1 area has many environmental conditions : max 1
#const max_environmetalconditions = 1.
#const number_environmetalconditions = number_areas * max_environmetalconditions.
#const s1 = 1000.
potentialinstance_environmetalconditions(s1..s1+number_environmetalconditions-1).
0{type_environmetalconditions(X)}1 :- potentialinstance_environmetalconditions(X).


% 1 area may have many wall types : max 2
#const max_walltype = 2.
#const number_walltypes = number_areas * max_walltype.
#const s3 = 2000.
potentialinstance_walltype(s3..s3+number_walltypes-1).
0{type_walltype(X)}1 :- potentialinstance_walltype(X).

% 1 deployment environment has many pollutants : max 5
#const max_pollutant = 5.
#const number_pollutant = number_deploymentenvironments*max_pollutant.
#const s4 = 3000.
potentialinstance_pollutant(s4..s4+number_pollutant-1).
0{type_pollutant(X)}1 :- potentialinstance_pollutant(X).

% 1 pollutant  has many Inffering pollutant : max 5
#const max_infpollutant = 5.
#const number_infpollutant = number_pollutant*max_infpollutant.
#const s5 = 4000.
potentialinstance_infferingpollutant(s5..s5+number_infpollutant-1).
0{type_infferingpollutant(X)}1 :- potentialinstance_infferingpollutant(X).


% 1 pollutant has many sensors : max 3
#const max_sensor = 3.
#const number_sensor = max_sensor*number_pollutant.
#const s6 = 5000.
potentialinstance_sensor(s6..s6+number_sensor-1).
0{type_sensor(X)}1 :- potentialinstance_sensor(X).

% 1 Sensor uses many reference models : max 2
#const max_refmodel = 2.
#const number_refmodels = number_sensor*max_refmodel.
#const s7 = 6000.
potentialinstance_referencemodel(s7..s7+number_refmodels-1).
0{type_referencemodel(X)}1 :- potentialinstance_referencemodel(X).

% 1 Sensor adopts 1 monitoring policy
#const max_monpolicy = 1.
#const number_monpolicy = number_sensor*max_monpolicy.
#const s8 = 7000.
potentialinstance_monitoringpolicy(s8..s8+number_monpolicy-1).
0{type_monitoringpolicy(X)}1 :- potentialinstance_monitoringpolicy(X).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% ATTRIBUTE DEFINITIONS (DOMAINS:attributedomain and VALUES:attributevalue)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%
% Class:  type_monitoringstation(X)
% Attribute Domains
attributedomain_type_monitoringstation_communication(wired;wireless).
attributedomain_type_monitoringstation_localstorage(yes;no).
attributedomain_type_monitoringstation_cloudstorage(yes;no).
attributedomain_type_monitoringstation_enclosure(strugged;standard).

% Attribute Values
1{attributevalue_type_monitoringstation_communication(X,Y): attributedomain_type_monitoringstation_communication(Y)}1 :- type_monitoringstation(X).
1{attributevalue_type_monitoringstation_localstorage(X,Y): attributedomain_type_monitoringstation_localstorage(Y)}1 :- type_monitoringstation(X).
1{attributevalue_type_monitoringstation_cloudstorage(X,Y): attributedomain_type_monitoringstation_cloudstorage(Y)}1 :- type_monitoringstation(X).
1{attributevalue_type_monitoringstation_enclosure(X,Y): attributedomain_type_monitoringstation_enclosure(Y)}1 :- type_monitoringstation(X).
%%%%%%%%%

%%%%%%%%%
% Class:  type_deploymentenvironment(X)
% Attribute Domains
attributedomain_type_deploymentenvironment_type(indoor;outdoor;mixed).
attributedomain_type_deploymentenvironment_context(seaside;country;tropicalarea;aridarea;other).
attributedomain_type_deploymentenvironment_locationtype(urban;suburban;countryside;residential;industrial;other).

% Attribute Values
1{attributevalue_type_deploymentenvironment_type(X,Y): attributedomain_type_deploymentenvironment_type(Y)}1 :- type_deploymentenvironment(X).
1{attributevalue_type_deploymentenvironment_context(X,Y): attributedomain_type_deploymentenvironment_context(Y)}1 :- type_deploymentenvironment(X).
1{attributevalue_type_deploymentenvironment_locationtype(X,Y): attributedomain_type_deploymentenvironment_locationtype(Y)}1 :- type_deploymentenvironment(X).
%%%%%%%%%

%%%%%%%%%
% Class:  type_environmetalconditions(X)
% Attribute Domains
attributedomain_type_environmetalconditions_humidity(0..10).
attributedomain_type_environmetalconditions_windspeed(0..10).
attributedomain_type_environmetalconditions_rain(yes;no).
attributedomain_type_environmetalconditions_dust(yes;no).
attributedomain_type_environmetalconditions_averagetemperature(-50..50).
attributedomain_type_environmetalconditions_snow(yes;no).
attributedomain_type_environmetalconditions_ice(yes;no).
attributedomain_type_environmetalconditions_vibrations(yes;no).
attributedomain_type_environmetalconditions_averagepressure(0..10).

% Attribute Values
1{attributevalue_type_environmetalconditions_humidity(X,Y): attributedomain_type_environmetalconditions_humidity(Y)}1 :- type_environmetalconditions(X).
1{attributevalue_type_environmetalconditions_windspeed(X,Y): attributedomain_type_environmetalconditions_windspeed(Y)}1 :- type_environmetalconditions(X).
1{attributevalue_type_environmetalconditions_rain(X,Y): attributedomain_type_environmetalconditions_rain(Y)}1 :- type_environmetalconditions(X).
1{attributevalue_type_environmetalconditions_dust(X,Y): attributedomain_type_environmetalconditions_dust(Y)}1 :- type_environmetalconditions(X).
1{attributevalue_type_environmetalconditions_averagetemperature(X,Y): attributedomain_type_environmetalconditions_averagetemperature(Y)}1 :- type_environmetalconditions(X).
1{attributevalue_type_environmetalconditions_snow(X,Y): attributedomain_type_environmetalconditions_snow(Y)}1 :- type_environmetalconditions(X).
1{attributevalue_type_environmetalconditions_ice(X,Y): attributedomain_type_environmetalconditions_ice(Y)}1 :- type_environmetalconditions(X).
1{attributevalue_type_environmetalconditions_vibrations(X,Y): attributedomain_type_environmetalconditions_vibrations(Y)}1 :- type_environmetalconditions(X).
1{attributevalue_type_environmetalconditions_averagepressure(X,Y): attributedomain_type_environmetalconditions_averagepressure(Y)}1 :- type_environmetalconditions(X).
%%%%%%%%%

%%%%%%%%%
% Class:  type_area(X)
% Attribute Domains
attributedomain_type_area_type(indoor;outdoor;mixed).
attributedomain_type_area_category(industry;shop;gym;pub;library;hospital;residential;school;publicoffice;monument;road;countryfield;sensitivearea;childrenspace;other).
attributedomain_type_area_prefabricatedbuilding(yes;no).
attributedomain_type_area_vehicletraffic(none;light;medium;heavy).
attributedomain_type_area_industrialtype(notapplicable;chemical;siderurgic;heavy;sme).
attributedomain_type_area_pollutedsoil(yes;no).
attributedomain_type_area_floor(0..100).
attributedomain_type_area_controlledarea(yes;no).
attributedomain_type_area_airconditioning(yes;no).
attributedomain_type_area_heatingsystem(yes;no).
attributedomain_type_area_windows(yes;no).
attributedomain_type_area_smokepresence(yes;no).
attributedomain_type_area_moldpresence(yes;no).
attributedomain_type_area_dustyarea(yes;no).

% Attribute Values
1{attributevalue_type_area_type(X,Y): attributedomain_type_area_type(Y)}1 :- type_area(X).
1{attributevalue_type_area_category(X,Y): attributedomain_type_area_category(Y)}1 :- type_area(X).
1{attributevalue_type_area_prefabricatedbuilding(X,Y): attributedomain_type_area_prefabricatedbuilding(Y)}1 :- type_area(X).
1{attributevalue_type_area_vehicletraffic(X,Y): attributedomain_type_area_vehicletraffic(Y)}1 :- type_area(X).
1{attributevalue_type_area_industrialtype(X,Y): attributedomain_type_area_industrialtype(Y)}1 :- type_area(X).
1{attributevalue_type_area_pollutedsoil(X,Y): attributedomain_type_area_pollutedsoil(Y)}1 :- type_area(X).
1{attributevalue_type_area_floor(X,Y): attributedomain_type_area_floor(Y)}1 :- type_area(X).
1{attributevalue_type_area_controlledarea(X,Y): attributedomain_type_area_controlledarea(Y)}1 :- type_area(X).
1{attributevalue_type_area_airconditioning(X,Y): attributedomain_type_area_airconditioning(Y)}1 :- type_area(X).
1{attributevalue_type_area_heatingsystem(X,Y): attributedomain_type_area_heatingsystem(Y)}1 :- type_area(X).
1{attributevalue_type_area_windows(X,Y): attributedomain_type_area_windows(Y)}1 :- type_area(X).
1{attributevalue_type_area_smokepresence(X,Y): attributedomain_type_area_smokepresence(Y)}1 :- type_area(X).
1{attributevalue_type_area_moldpresence(X,Y): attributedomain_type_area_moldpresence(Y)}1 :- type_area(X).
1{attributevalue_type_area_dustyarea(X,Y): attributedomain_type_area_dustyarea(Y)}1 :- type_area(X).
%%%%%%%%%

%%%%%%%%%
% Class:  type_walltype(X)
% Attribute Domains
attributedomain_type_walltype_wallpaper(yes;no).
attributedomain_type_walltype_plasticcladding(yes;no).
attributedomain_type_walltype_woodenpanels(yes;no).
attributedomain_type_walltype_moquette(yes;no).
attributedomain_type_walltype_tiles(yes;no).
attributedomain_type_walltype_plaster(yes;no).

% Attribute Values
1{attributevalue_type_walltype_wallpaper(X,Y): attributedomain_type_walltype_wallpaper(Y)}1 :- type_walltype(X).
1{attributevalue_type_walltype_plasticcladding(X,Y): attributedomain_type_walltype_plasticcladding(Y)}1 :- type_walltype(X).
1{attributevalue_type_walltype_woodenpanels(X,Y): attributedomain_type_walltype_woodenpanels(Y)}1 :- type_walltype(X).
1{attributevalue_type_walltype_moquette(X,Y): attributedomain_type_walltype_moquette(Y)}1 :- type_walltype(X).
1{attributevalue_type_walltype_tiles(X,Y): attributedomain_type_walltype_tiles(Y)}1 :- type_walltype(X).
1{attributevalue_type_walltype_plaster(X,Y): attributedomain_type_walltype_plaster(Y)}1 :- type_walltype(X).
%%%%%%%%%


%%%%%%%%%
% Class:  type_pollutant(X)
% Attribute Domains
attributedomain_type_pollutant_type(co;co2;cov;ipa;o3;no2;no;nh3;ph3;eto;so2;cl2;ch2o;ch4;h2;h2s;hcl;hcn;pm10;pm25,pm5;pm1;pm0;a;b;g;radon).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% WARNING: pm25 means pm2.5                %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
attributedomain_type_pollutant_gas(yes;no).
attributedomain_type_pollutant_electromagnetic(yes;no).
attributedomain_type_pollutant_ionizing(yes;no).
attributedomain_type_pollutant_affectair(yes;no).
attributedomain_type_pollutant_affectwater(yes;no).
attributedomain_type_pollutant_affectsoil(yes;no).
attributedomain_type_pollutant_affectobjects(yes;no).
attributedomain_type_pollutant_ban(notapplicable;hz50;ghz800).
attributedomain_type_pollutant_specific(notapplicable;explosive;toxic;dangerous;poisonous).
attributedomain_type_pollutant_metric(ppm;celsiusdegrees;bar;percentage;particlesperminute;ugm3;countsperminute;dbvonm;militesla).
attributedomain_type_pollutant_rawmetric(yes;no).

% Attribute Values
1{attributevalue_type_pollutant_type(X,Y): attributedomain_type_pollutant_type(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_gas(X,Y): attributedomain_type_pollutant_gas(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_electromagnetic(X,Y): attributedomain_type_pollutant_electromagnetic(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_ionizing(X,Y): attributedomain_type_pollutant_ionizing(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_affectair(X,Y): attributedomain_type_pollutant_affectair(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_affectwater(X,Y): attributedomain_type_pollutant_affectwater(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_affectsoil(X,Y): attributedomain_type_pollutant_affectsoil(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_affectobjects(X,Y): attributedomain_type_pollutant_affectobjects(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_ban(X,Y): attributedomain_type_pollutant_ban(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_specific(X,Y): attributedomain_type_pollutant_specific(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_metric(X,Y): attributedomain_type_pollutant_metric(Y)}1 :- type_pollutant(X).
1{attributevalue_type_pollutant_rawmetric(X,Y): attributedomain_type_pollutant_rawmetric(Y)}1 :- type_pollutant(X).
%%%%%%%%%


%%%%%%%%%
% Class:  type_infferingpollutant(X)
% Attribute Domains
attributedomain_type_infferingpollutant_type(co;co2;cov;ipa;o3;no2;no;ch2o;so2;h2;h2s;pm10;pm25,pm5;pm1;pm0;a;b;g;radon).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% WARNING: pm25 means pm2.5                %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
attributedomain_type_infferingpollutant_gas(yes;no).
attributedomain_type_infferingpollutant_electromagnetic(yes;no).
attributedomain_type_infferingpollutant_ionizing(yes;no).
attributedomain_type_infferingpollutant_affectair(yes;no).
attributedomain_type_infferingpollutant_affectwater(yes;no).
attributedomain_type_infferingpollutant_affectsoil(yes;no).
attributedomain_type_infferingpollutant_affectobjects(yes;no).
attributedomain_type_infferingpollutant_ban(notapplicable;hz50;ghz800).
attributedomain_type_infferingpollutant_explosive(yes;no).
attributedomain_type_infferingpollutant_toxic(yes;no).
attributedomain_type_infferingpollutant_dangerous(yes;no).
attributedomain_type_infferingpollutant_poisonous(yes;no).
attributedomain_type_infferingpollutant_metric(ppm;celsiusdegrees;bar;percentage;particlesperminute;ugm3;countsperminute;dbvonm;militesla).

% Attribute Values
1{attributevalue_type_infferingpollutant_type(X,Y): attributedomain_type_infferingpollutant_type(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_gas(X,Y): attributedomain_type_infferingpollutant_gas(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_electromagnetic(X,Y): attributedomain_type_infferingpollutant_electromagnetic(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_ionizing(X,Y): attributedomain_type_infferingpollutant_ionizing(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_affectair(X,Y): attributedomain_type_infferingpollutant_affectair(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_affectwater(X,Y): attributedomain_type_infferingpollutant_affectwater(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_affectsoil(X,Y): attributedomain_type_infferingpollutant_affectsoil(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_affectobjects(X,Y): attributedomain_type_infferingpollutant_affectobjects(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_ban(X,Y): attributedomain_type_infferingpollutant_ban(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_explosive(X,Y): attributedomain_type_infferingpollutant_explosive(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_toxic(X,Y): attributedomain_type_infferingpollutant_toxic(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_dangerous(X,Y): attributedomain_type_infferingpollutant_dangerous(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_poisonous(X,Y): attributedomain_type_infferingpollutant_poisonous(Y)}1 :- type_infferingpollutant(X).
1{attributevalue_type_infferingpollutant_metric(X,Y): attributedomain_type_infferingpollutant_metric(Y)}1 :- type_infferingpollutant(X).
%%%%%%%%%


%%%%%%%%%
% Class:  type_sensor(X)
% Attribute Domains
attributedomain_type_sensor_type(co;co2;cov;ipa;o3;no2;no;nh3;ph3;eto;so2;cl2;ch2o;ch4;h2;h2s;hcl;hcn;pm10;pm25,pm5;pm1;pm0;a;b;g;radon).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% WARNING: pm25 means pm2.5                %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
attributedomain_type_sensor_technology(electrochemical;infrared;infrredthermop;infraredpyro;photoionisation;geiger).
attributedomain_type_sensor_sensitivity(0.. 100000).
attributedomain_type_sensor_pod(0..100).
attributedomain_type_sensor_range(0.. 100000).
attributedomain_type_sensor_resolution(0.. 100000).
attributedomain_type_sensor_responsetime(0..100).
attributedomain_type_sensor_expectedoperationlife(0..100).
attributedomain_type_sensor_minoptemperature(-50..50).
attributedomain_type_sensor_maxoptemperature(-50..50).
attributedomain_type_sensor_minophumidity(0..10).
attributedomain_type_sensor_maxophumidity(0..100).
attributedomain_type_sensor_minoppressure(0..100).
attributedomain_type_sensor_maxoppressure(0..100).
attributedomain_type_sensor_upperlimit(0..50000).
attributedomain_type_sensor_dualsensor(yes;no).
attributedomain_type_sensor_noise(-1..100000).
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% WARNING: Sensor.noise= realvalue x 100   %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% Attribute Values
1{attributevalue_type_sensor_type(X,Y): attributedomain_type_sensor_type(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_technology(X,Y): attributedomain_type_sensor_technology(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_sensitivity(X,Y): attributedomain_type_sensor_sensitivity(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_pod(X,Y): attributedomain_type_sensor_pod(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_range(X,Y): attributedomain_type_sensor_range(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_resolution(X,Y): attributedomain_type_sensor_resolution(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_responsetime(X,Y): attributedomain_type_sensor_responsetime(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_expectedoperationlife(X,Y): attributedomain_type_sensor_expectedoperationlife(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_minoptemperature(X,Y): attributedomain_type_sensor_minoptemperature(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_maxoptemperature(X,Y): attributedomain_type_sensor_maxoptemperature(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_minophumidity(X,Y): attributedomain_type_sensor_minophumidity(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_maxophumidity(X,Y): attributedomain_type_sensor_maxophumidity(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_minoppressure(X,Y): attributedomain_type_sensor_minoppressure(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_maxoppressure(X,Y): attributedomain_type_sensor_maxoppressure(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_upperlimit(X,Y): attributedomain_type_sensor_upperlimit(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_dualsensor(X,Y): attributedomain_type_sensor_dualsensor(Y)}1 :- type_sensor(X).
1{attributevalue_type_sensor_noise(X,Y): attributedomain_type_sensor_noise(Y)}1 :- type_sensor(X).
%%%%%%%%%


%%%%%%%%%
% Class:  type_referencemodel(X)
% Attribute Domains
attributedomain_type_referencemodel_monitoringalgorithm(notneeded;countbased;manufacturerspecific;geigerformula).
% Attribute Values
1{attributevalue_type_referencemodel_monitoringalgorithm(X,Y): attributedomain_type_referencemodel_monitoringalgorithm(Y)}1 :- type_referencemodel(X).
%%%%%%%%%

%%%%%%%%%
% Class:  type_monitoringpolicy(X)
% Attribute Domains
attributedomain_type_monitoringpolicy_autosampling(yes;no).
attributedomain_type_monitoringpolicy_samplingtype(active;passive).
attributedomain_type_monitoringpolicy_duration(0..1000).
attributedomain_type_monitoringpolicy_publishrate(0..1000).
% Attribute Values
1{attributevalue_type_monitoringpolicy_autosampling(X,Y): attributedomain_type_monitoringpolicy_autosampling(Y)}1 :- type_monitoringpolicy(X).
1{attributevalue_type_monitoringpolicy_samplingtype(X,Y): attributedomain_type_monitoringpolicy_samplingtype(Y)}1 :- type_monitoringpolicy(X).
1{attributevalue_type_monitoringpolicy_duration(X,Y): attributedomain_type_monitoringpolicy_duration(Y)}1 :- type_monitoringpolicy(X).
1{attributevalue_type_monitoringpolicy_publishrate(X,Y): attributedomain_type_monitoringpolicy_publishrate(Y)}1 :- type_monitoringpolicy(X).
%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% ASSOCIATIONS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% ASSOC1: monitoringstation - deploymentenvironment
1{assoc_type_monitoringstation_and_type_deploymentenvironment(X,Y): type_deploymentenvironment(Y)} max_deploymentenvironments :- type_monitoringstation(X).
1{assoc_type_monitoringstation_and_type_deploymentenvironment(Y,X): type_monitoringstation(Y)} 1 :- type_deploymentenvironment(X).

% ASSOC2: deploymentenvironment - area
1{assoc_type_deploymentenvironment_and_type_area(X,Y): type_area(Y)} max_areas :- type_deploymentenvironment(X).
1{assoc_type_deploymentenvironment_and_type_area(Y,X): type_deploymentenvironment(Y)} 1 :- type_area(X).

% ASSOC3: area - type_environmetalconditions
1{assoc_type_area_and_type_environmetalconditions(X,Y): type_environmetalconditions(Y)} max_environmetalconditions :- type_area(X).
1{assoc_type_area_and_type_environmetalconditions(Y,X): type_area(Y)} 1 :- type_environmetalconditions(X).

% ASSOC4: area - type_walltype
1{assoc_type_area_and_type_walltype(X,Y): type_walltype(Y)} max_walltype :- type_area(X).
1{assoc_type_area_and_type_walltype(Y,X): type_area(Y)} 1 :- type_walltype(X).

% ASSOC5: deploymentenvironment - type_pollutant
1{assoc_type_deploymentenvironment_and_type_pollutant(X,Y): type_pollutant(Y)} max_pollutant :- type_deploymentenvironment(X).
1{assoc_type_deploymentenvironment_and_type_pollutant(Y,X): type_deploymentenvironment(Y)} 1 :- type_pollutant(X).

% ASSOC6: type_pollutant - type_infferingpollutant
1{assoc_type_pollutant_and_type_infferingpollutant(X,Y): type_infferingpollutant(Y)} max_infpollutant :- type_pollutant(X).
1{assoc_type_pollutant_and_type_infferingpollutant(Y,X): type_pollutant(Y)} 1 :- type_infferingpollutant(X).

% ASSOC7: monitoringstation - type_sensor
1{assoc_type_monitoringstation_and_type_sensor(X,Y): type_sensor(Y)} number_sensor :- type_monitoringstation(X).
1{assoc_type_monitoringstation_and_type_sensor(Y,X): type_monitoringstation(Y)} 1 :- type_sensor(X).

% ASSOC8: type_sensor - type_monitoringpolicy
1{assoc_type_sensor_and_type_monitoringpolicy(X,Y): type_monitoringpolicy(Y)} max_monpolicy :- type_sensor(X).
1{assoc_type_sensor_and_type_monitoringpolicy(Y,X): type_sensor(Y)} 1 :- type_monitoringpolicy(X).

% ASSOC9: sensor - ref model
1{assoc_type_sensor_and_type_referencemodel(X,Y): type_referencemodel(Y)} max_refmodel :- type_sensor(X).
1{assoc_type_sensor_and_type_referencemodel(Y,X): type_sensor(Y)} 1 :- type_referencemodel(X).

% ASSOC10: pollutant - sensor
1{assoc_type_pollutant_and_type_sensor(X,Y): type_sensor(Y)} max_sensor :- type_pollutant(X).
1{assoc_type_pollutant_and_type_sensor(Y,X): type_pollutant(Y)} 1 :- type_sensor(X).

% PARSER_UserRequirements

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% RULES
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Additional Rules From UML Diagram

% R1: Each sensor adopts 1 monitoring policy
1{type_monitoringpolicy(M) : assoc_type_sensor_and_type_monitoringpolicy(S, M)}1 :- type_sensor(S).

% R2: Each area has 1 environmental condition
1{type_environmetalconditions(E) : assoc_type_area_and_type_environmetalconditions(A, E)}1 :- type_area(A).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CONSTRAINTS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% C1: DeploymentEnvironment.locationType=â€�Urbanâ€� CONFLICTS Area.Category=â€�Countryfieldâ€�
% ----->  VALIDATED
:- attributevalue_type_deploymentenvironment_locationtype(E,urban), attributevalue_type_area_category(A,countryfield), assoc_type_deploymentenvironment_and_type_area(E,A).

% validate C1
% type_deploymentenvironment(2).
% type_area(3).
% attributevalue_type_deploymentenvironment_locationtype(2,urban).
% attributevalue_type_area_category(3,hospital).
% assoc_type_deploymentenvironment_and_type_area(2,3).


% C2: DeploymentEnvironment.type="Indoor" CONFLICTS Area.Category="Road"
% -----> SAME WITH VALIDATED (C1)
:- attributevalue_type_deploymentenvironment_type(E,indoor), attributevalue_type_area_category(A,road), assoc_type_deploymentenvironment_and_type_area(E,A).

% C3: DeploymentEnvironment.type="Mixed" CONFLICTS Area.Category="Road"
% -----> SAME WITH VALIDATED (C1)
:- attributevalue_type_deploymentenvironment_type(E,mixed), attributevalue_type_area_category(A,road), assoc_type_deploymentenvironment_and_type_area(E,A).


% C4: IF Area.type="Outdoor" THEN DeploymentEnvironment HAS
%  Pollutant.type="CO" AND
%  Pollutant.type="CO2" AND
%  Pollutant.type="NO" AND
%  Pollutant.type="NO2" AND
%  Pollutant.type="O3" AND
%  Pollutant.type="COV"
% ----->  VALIDATED
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NEW_CONSTRAINT_TYPE: C4
% if (x or y)  then has (z and w)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,outdoor), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,outdoor), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, cov), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,outdoor), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, no), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,outdoor), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, no2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,outdoor), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, o3), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,outdoor), assoc_type_deploymentenvironment_and_type_area(E,A).

% validate C4
% type_deploymentenvironment(2).
% type_area(4).
% attributevalue_type_area_type(4,outdoor).
% assoc_type_deploymentenvironment_and_type_area(2,4).


% C5: IF Area.type="Indoor" THEN DeploymentEnvironment HAS
%  Pollutant.type="CO" AND
%  Pollutant.type="CO2" AND
%  Pollutant.type="COV" AND
%  Pollutant.type="CH2O"
% -----> SAME WITH VALIDATED (C4)


1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,indoor), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,indoor), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, cov), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,indoor), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, ch2o), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_type(A,indoor), assoc_type_deploymentenvironment_and_type_area(E,A).


% C6: IF Area.Category="Road" THEN DeploymentEnvironment HAS
%    Pollutant.type="CO" AND
%    Pollutant.type="PM10" AND
%    Pollutant.type="NO2"
% -----> SAME WITH VALIDATED (C4)

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,road), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, pm10), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,road), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, no2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,road), assoc_type_deploymentenvironment_and_type_area(E,A).


% C7: IF Area.Category="Industry" THEN DeploymentEnvironment HAS
%  Pollutant.type="SO2" AND
%  Pollutant.type="H2S"
% -----> SAME WITH VALIDATED (C4)

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, so2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,industry), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, h2s), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,industry), assoc_type_deploymentenvironment_and_type_area(E,A).


% C8: IF Area.Category="Childreanspace" OR Area.Category="Shop" OR
%  Area.Category="Gym" OR Area.Category="Pub" OR Area.Category="Library" OR
%  Area.Category="School" THEN DeploymentEnvironment HAS
%  Pollutant.type="CO" AND
%  Pollutant.type="CO2" AND
%  Pollutant.type="CH2O"
% -----> SAME WITH VALIDATED (C4)

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,childrenspace), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,childrenspace), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, ch2o), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,childrenspace), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,shop), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,shop), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, ch2o), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,shop), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,gym), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,gym), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, ch2o), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,gym), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,library), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,library), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, ch2o), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,library), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,school), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,school), assoc_type_deploymentenvironment_and_type_area(E,A).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, ch2o), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,school), assoc_type_deploymentenvironment_and_type_area(E,A).


% C9: IF Area.Category="Monument" THEN DeploymentEnvironment HAS
%  Pollutant.type="SO2"
% -----> SAME WITH VALIDATED (C4)
1{type_pollutant(P) : attributevalue_type_pollutant_type(P, so2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- attributevalue_type_area_category(A,monument), assoc_type_deploymentenvironment_and_type_area(E,A).

% C10: IF Pollutant.type=""CO"" OR
%  Pollutant.type=""CO2"" OR
%  Pollutant.type=""COV"" OR
%  Pollutant.type=""IPA"" OR --> avoid now
%  Pollutant.type=""O3"" OR
%  Pollutant.type=""NO2"" OR
%  Pollutant.type=""NO"" OR
%  Pollutant.type=""NH3"" OR
%  Pollutant.type=""CH2O"" OR
%  Pollutant.type=""CH4"" OR
%  Pollutant.type=""H2"" OR
%  Pollutant.type=""H2S"" OR
%  Pollutant.type=""HCL"" OR
%  Pollutant.type=""HCN"" OR
%  Pollutant.type=""SO2"" OR
%  Pollutant.type=""PM10"" OR
%  Pollutant.type=""PM2.5"" OR
%  Pollutant.type=""PM1"" OR
%  Pollutant.type=""PM0"" OR
%  Pollutant.type=""A"" OR
%  Pollutant.type=""B"" OR
%  Pollutant.type=""G"" OR
%  Pollutant.type=""Radon"" THEN
%  Pollutant.affectairs=""true"
% -----> VALIDATED
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NEW_CONSTRAINT_TYPE: C10
% if (x or y)   then (z)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

:- attributevalue_type_pollutant_type(P,co), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,co2), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,cov), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,ipa), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,o3), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,no2), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,no), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,nh3), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,ch2o), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,ch4), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,h2), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,h2s), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,hcl), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,hcn), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,so2), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,pm10), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,pm25), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,pm0), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,a), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,b), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,g), not attributevalue_type_pollutant_affectair(P,yes).
:- attributevalue_type_pollutant_type(P,radon), not attributevalue_type_pollutant_affectair(P,yes).

% validate C10
% type_pollutant(6).
% attributevalue_type_pollutant_type(6,co).
% attributevalue_type_pollutant_affectair(6,yes).


% C11: IF DeploymentEnvironment HAS Pollutant.type="O3" THEN
%  DeploymentEnvironment HAS Pollutant.type="NO2" AND
%  DeploymentEnvironment HAS Pollutant.type="NO"
% -----> SAME WITH VALIDATED (C4)
1{type_pollutant(P) : attributevalue_type_pollutant_type(P, no2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- assoc_type_deploymentenvironment_and_type_area(E,A),assoc_type_deploymentenvironment_and_type_pollutant(E,P1),attributevalue_type_pollutant_type(P1, o3).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, no), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- assoc_type_deploymentenvironment_and_type_area(E,A),assoc_type_deploymentenvironment_and_type_pollutant(E,P1),attributevalue_type_pollutant_type(P1, o3).


% C12: IF DeploymentEnvironment HAS Pollutant.type="NO2" THEN
%  DeploymentEnvironment HAS Pollutant.type="O3" AND
%  DeploymentEnvironment HAS Pollutant.type="NO"
% -----> SAME WITH VALIDATED (C4)
1{type_pollutant(P) : attributevalue_type_pollutant_type(P, o3), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- assoc_type_deploymentenvironment_and_type_area(E,A),assoc_type_deploymentenvironment_and_type_pollutant(E,P1),attributevalue_type_pollutant_type(P1, no2).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, no), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- assoc_type_deploymentenvironment_and_type_area(E,A),assoc_type_deploymentenvironment_and_type_pollutant(E,P1),attributevalue_type_pollutant_type(P1, no2).


% C13: IF DeploymentEnvironment HAS Pollutant.type="NO" THEN
%  DeploymentEnvironment HAS Pollutant.type="O3" AND
%  DeploymentEnvironment HAS Pollutant.type="NO2"
% -----> SAME WITH VALIDATED (C4)
1{type_pollutant(P) : attributevalue_type_pollutant_type(P, no2), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- assoc_type_deploymentenvironment_and_type_area(E,A),assoc_type_deploymentenvironment_and_type_pollutant(E,P1),attributevalue_type_pollutant_type(P1, no).

1{type_pollutant(P) : attributevalue_type_pollutant_type(P, o3), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- assoc_type_deploymentenvironment_and_type_area(E,A),assoc_type_deploymentenvironment_and_type_pollutant(E,P1),attributevalue_type_pollutant_type(P1, no).


% C14: IF Pollutant.type"CO" THEN Sensor.technology="Electrochemical"
% -----> VALIDATED
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NEW_CONSTRAINT_TYPE: C14
% if (x)   then (y)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- attributevalue_type_pollutant_type(P,co),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).

% validate C14
%type_pollutant(300).
%type_sensor(500).
%attributevalue_type_pollutant_type(300,co).
%attributevalue_type_sensor_technology(500,infrared).
%assoc_type_pollutant_and_type_sensor(300,500).


% C15: IF Pollutant.type"CO2" THEN Sensor.technology="Infrared" OR
%  Sensor.technology="InfraredThermop" OR
%  Sensor.technology="InfraredPyro"
% -----> VALIDATED
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NEW_CONSTRAINT_TYPE: C15
% if (x)   then (y or z)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- attributevalue_type_pollutant_type(P,co2),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,infrared),
not attributevalue_type_sensor_technology(S,infraredthermop),
not attributevalue_type_sensor_technology(S,infraredpyro).


% C16: IF Pollutant.type"COV" THEN Sensor.technology="Photoionisation"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,cov),  assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,photoionisation).

% C17: IF Pollutant.type"NO2" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,no2),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).

% C18: IF Pollutant.type"O3" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,o3),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).


% C19: IF Pollutant.type"NO" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,no),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).


% C20: IF Pollutant.type"NH3" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,nh3),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).


% C21: IF Pollutant.type"PH3" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,ph3),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).


% C22: IF Pollutant.type"ETO" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,ph3),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).


% C23: IF Pollutant.type"CH2O" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,ch20),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).


% C24: IF Pollutant.type"SO2" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,so2),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).


% C25: IF Pollutant.type"H2" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,h2),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).

% C26: IF Pollutant.type"H2S" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,h2s),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).

% C27: IF Pollutant.type"HCL" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,hcl),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).

% C28: IF Pollutant.type"HCN" THEN Sensor.technology="Electrochemical"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,hcn),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,electrochemical).

% C29: IF Pollutant.type"PM10" THEN Sensor.technology="Infrared"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,pm10),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,infrared).

% C30: IF Pollutant.type"PM2.5" THEN Sensor.technology="Infrared"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,pm25),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,infrared).

% C31: IF Pollutant.type"PM1" THEN Sensor.technology="Infrared"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,pm1),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,infrared).

% C32: IF Pollutant.type"PM0" THEN Sensor.technology="Infrared"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,pm0),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,infrared).

% C33: IF Pollutant.type"A" THEN Sensor.technology="Geiger"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,a),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,geiger).

% C34: IF Pollutant.type"B" THEN Sensor.technology="Geiger"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,b),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,geiger).

% C35: IF Pollutant.type"G" THEN Sensor.technology="Geiger"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,g),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,geiger).


% C36: IF Pollutant.type"CH4" THEN Sensor.technology="Infrared" OR Sensor.technology="InfraredThermop"
% -----> SAME WITH VALIDATED (C15)
:- attributevalue_type_pollutant_type(P,ch4),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_technology(S,infrared),
not attributevalue_type_sensor_technology(S,infraredthermop).


% C37: IF Pollutant.type"CH2O" THEN Pollutant HAS InterferringPollutant.type="CO"
% -----> SAME WITH VALIDATED (C4)
1{type_infferingpollutant(I) : attributevalue_type_infferingpollutant_type(I, co), assoc_type_pollutant_and_type_infferingpollutant(P,I)}1
:- attributevalue_type_pollutant_type(P,ch2o).

% C38: IF Pollutant.type"CH2O" THEN Pollutant HAS InterferringPollutant.type="H2"
% -----> SAME WITH VALIDATED (C4)
1{type_infferingpollutant(I) : attributevalue_type_infferingpollutant_type(I, h2), assoc_type_pollutant_and_type_infferingpollutant(P,I)}1
:- attributevalue_type_pollutant_type(P,ch2o).

% C39: IF DeploymentEnvironment HAS Pollutant.type="CH2O" THEN DeploymentEnvironment HAS Pollutant.type="CO"
% -----> SAME WITH VALIDATED (C4)
1{type_pollutant(P) : attributevalue_type_pollutant_type(P, co), assoc_type_deploymentenvironment_and_type_pollutant(E,P)}1
:- assoc_type_deploymentenvironment_and_type_pollutant(E,P1),attributevalue_type_pollutant_type(P1, ch2o).

% C40: IF Pollutant.type"COV" THEN Sensor.pod="1"
% -----> SAME WITH VALIDATED (C14)
:- attributevalue_type_pollutant_type(P,cov),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_pod(S,1).


% C41: IF Pollutant.type="O3" OR
%   Pollutant.type="H2" OR
%   Pollutant.type="ETO" OR
%   Pollutant.type="HCL" OR
%   Pollutant.type="NH3" OR
%   Pollutant.type="PH3" THEN Sensor.pod="2"
% -----> SAME WITH VALIDATED (C10)

:- attributevalue_type_pollutant_type(P,o3), attributevalue_type_sensor_pod(S,2),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,h2), attributevalue_type_sensor_pod(S,2),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,eto), attributevalue_type_sensor_pod(S,2),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,hcl), attributevalue_type_sensor_pod(S,2),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,nh3), attributevalue_type_sensor_pod(S,2),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,ph3), attributevalue_type_sensor_pod(S,2),
not assoc_type_pollutant_and_type_sensor(P,S).

% C42: IF Pollutant.type="PM10" OR
%  Pollutant.type="PM2.5" OR
%  Pollutant.type="PM1" OR
%  Pollutant.type="PM0" THEN Sensor.pod="3"
% -----> SAME WITH VALIDATED (C10)

:- attributevalue_type_pollutant_type(P,pm10), attributevalue_type_sensor_pod(S,3),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,pm25), attributevalue_type_sensor_pod(S,3),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,pm1), attributevalue_type_sensor_pod(S,3),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,pm0), attributevalue_type_sensor_pod(S,3),
not assoc_type_pollutant_and_type_sensor(P,S).


% C43: IF Pollutant.type="A" OR
%  Pollutant.type="B" OR
%  Pollutant.type="G" OR
%  Pollutant.type="Radon" THEN Sensor.pod="4"
% -----> SAME WITH VALIDATED (C10)

:- attributevalue_type_pollutant_type(P,a), attributevalue_type_sensor_pod(S,4),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,b), attributevalue_type_sensor_pod(S,4),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,g), attributevalue_type_sensor_pod(S,4),
not assoc_type_pollutant_and_type_sensor(P,S).


% C44: IF Pollutant.type="CO" OR
% Pollutant.type="CL2" OR
% Pollutant.type="HCN" OR
% Pollutant.type="H2S" OR
% Pollutant.type="NO" OR
% Pollutant.type="NO2" OR
% Pollutant.type="SO2" THEN Sensor.pod="2" OR Sensor.pod="5"
% -----> SAME WITH VALIDATED (C15)
:- attributevalue_type_pollutant_type(P,co),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_pod(S,2),
not attributevalue_type_sensor_pod(S,5).


:- attributevalue_type_pollutant_type(P,cl2),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_pod(S,2),
not attributevalue_type_sensor_pod(S,5).


:- attributevalue_type_pollutant_type(P,hcn),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_pod(S,2),
not attributevalue_type_sensor_pod(S,5).

:- attributevalue_type_pollutant_type(P,h2s),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_pod(S,2),
not attributevalue_type_sensor_pod(S,5).

:- attributevalue_type_pollutant_type(P,no),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_pod(S,2),
not attributevalue_type_sensor_pod(S,5).

:- attributevalue_type_pollutant_type(P,no2),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_pod(S,2),
not attributevalue_type_sensor_pod(S,5).

:- attributevalue_type_pollutant_type(P,so2),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_pod(S,2),
not attributevalue_type_sensor_pod(S,5).

% C45: IF Pollutant.type="CO2" OR
%  Pollutant.type="CH4" THEN Sensor.pod="6"
% -----> SAME WITH VALIDATED (C10)

:- attributevalue_type_pollutant_type(P,co2), attributevalue_type_sensor_pod(S,6),
not assoc_type_pollutant_and_type_sensor(P,S).

:- attributevalue_type_pollutant_type(P,ch4), attributevalue_type_sensor_pod(S,6),
not assoc_type_pollutant_and_type_sensor(P,S).



% C46: IF DeploymentEnvironment HAS Pollutant.type="COV" THEN
%   Pollutant HAS Sensor.type="COV" AND
%                 Sensor.pod="1" AND
%                 Sensor.noise="5" AND
%                 Sensor.upperlimit="50"
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S,cov),
attributevalue_type_sensor_pod(S,1),
attributevalue_type_sensor_noise(S,500),
attributevalue_type_sensor_upperlimit(S,50)}1
:- attributevalue_type_pollutant_type(P,cov).


% C47: IF DeploymentEnvironment HAS Pollutant.type="CO" THEN
%  Pollutant HAS Sensor.type="CO"
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, co), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,co).



% C48: IF Sensor.type="CO" AND Sensor.pod="2" THEN
%  (Sensor.noise="4" AND Sensor.upperlimit="1000") OR
%  (Sensor.noise="0,5" AND Sensor.upperlimit="5000") OR
%  (Sensor.noise="0,05" AND Sensor.upperlimit="2000")
% -----> SAME WITH VALIDATED (C61)

sensor_noise400_and_upperlimit1000(S) :- attributevalue_type_sensor_noise(S,400), attributevalue_type_sensor_upperlimit(S,1000).
sensor_noise50_and_upperlimit5000(S) :- attributevalue_type_sensor_noise(S,50), attributevalue_type_sensor_upperlimit(S,5000).
sensor_noise5_and_upperlimit2000(S) :- attributevalue_type_sensor_noise(S,5), attributevalue_type_sensor_upperlimit(S,2000).

:- attributevalue_type_sensor_type(S,co),
 attributevalue_type_sensor_pod(S,2),
not sensor_noise400_and_upperlimit1000(S),
not sensor_noise50_and_upperlimit5000(S),
not sensor_noise5_and_upperlimit2000(S).



% C49: IF Sensor.type="CO" AND Sensor.pod="5" AND Sensor.dualsensor="false" THEN
%  (Sensor.noise="1,5" AND Sensor.upperlimit="1000")
% -----> SAME WITH VALIDATED (C54)

:- attributevalue_type_sensor_type(S,co),
attributevalue_type_sensor_pod(S,5),
attributevalue_type_sensor_dualsensor(S,no),
not attributevalue_type_sensor_noise(S,150).

:- attributevalue_type_sensor_type(S,co),
attributevalue_type_sensor_pod(S,5),
attributevalue_type_sensor_dualsensor(S,no),
not attributevalue_type_sensor_upperlimit(S,1000).



% C50: "IF DeploymentEnvironment HAS Pollutant.type=""O3"" THEN
%  Pollutant HAS Sensor.type="O3"
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, o3), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,o3).


% C51: "IF Sensor.type=""O3"" AND Sensor.dualsensor=""false"" THEN
%  (Sensor.noise=""4"" AND Sensor.upperlimit=""5"")"
% -----> SAME WITH VALIDATED (C54)
:- attributevalue_type_sensor_type(S,o3),
attributevalue_type_sensor_dualsensor(S,no),
not attributevalue_type_sensor_noise(S,400).

:- attributevalue_type_sensor_type(S,o3),
attributevalue_type_sensor_dualsensor(S,no),
not attributevalue_type_sensor_upperlimit(S,5).


% C52: "IF DeploymentEnvironment HAS Pollutant.type=""NO2"" THEN
%  Pollutant HAS Sensor.type=""NO2"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, no2), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,no2).


% C53: IF Sensor.type="NO2" AND Sensor.pod="2" AND Sensor.dualsensor="false" THEN
%   (Sensor.noise="12" AND Sensor.upperlimit="20") OR
%   (Sensor.noise="0,02" AND Sensor.upperlimit="20")
% -----> SAME WITH VALIDATED (C61)

sensor_noise1200_and_upperlimit20(S) :- attributevalue_type_sensor_noise(S,1200), attributevalue_type_sensor_upperlimit(S,20).
sensor_noise2_and_upperlimit20(S) :- attributevalue_type_sensor_noise(S,2), attributevalue_type_sensor_upperlimit(S,20).

:- attributevalue_type_sensor_type(S,no2),
 attributevalue_type_sensor_pod(S,2),
  attributevalue_type_sensor_dualsensor(S,no),
not sensor_noise1200_and_upperlimit20(S),
not sensor_noise2_and_upperlimit20(S).



% C54: IF Sensor.type="NO2" AND Sensor.pod="5" THEN
%  (Sensor.noise="0,1" AND Sensor.upperlimit="20")
% -----> VALIDATED
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NEW_CONSTRAINT_TYPE: C54
% if (x)   then (y and z)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
:- attributevalue_type_sensor_pod(S,5), attributedomain_type_sensor_type(S,no2),
not attributevalue_type_sensor_upperlimit(S,20).

:- attributevalue_type_sensor_pod(S,5), attributedomain_type_sensor_type(S,no2),
not attributevalue_type_sensor_noise(S,10).


% validate C54
% type_sensor(8).
%attributedomain_type_sensor_type(8,no2).
%attributevalue_type_sensor_pod(8,5).
%attributevalue_type_sensor_noise(8,2).
%attributevalue_type_sensor_upperlimit(8,20).


% C55: "IF DeploymentEnvironment HAS Pollutant.type=""O3"" AND
%  DeploymentEnvironment HAS Pollutant.type=""NO2"" THEN
%  Sensor.noise=""15"" AND Sensor.upperlimit=""20"" AND Sensor.dualsensor=""true"""
% -----> SAME WITH VALIDATED (C54)

:- attributevalue_type_pollutant_type(P1,o3), attributevalue_type_pollutant_type(P2,no2),
type_sensor(S),  type_pollutant(P1),  type_pollutant(P2),
not attributevalue_type_sensor_noise(S,1500).

:- attributevalue_type_pollutant_type(P1,o3), attributevalue_type_pollutant_type(P2,no2),
type_sensor(S),  type_pollutant(P1),  type_pollutant(P2),
not attributevalue_type_sensor_upperlimit(S,20).

:- attributevalue_type_pollutant_type(P1,o3), attributevalue_type_pollutant_type(P2,no2),
type_sensor(S),  type_pollutant(P1),  type_pollutant(P2),
not attributevalue_type_sensor_dualsensor(S,yes).


% C56: "IF DeploymentEnvironment HAS Pollutant.type=""H2S"" THEN
%  Polutant HAS Sensor.type=""H2S"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, h2s), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,h2s), type_pollutant(P).


% C57: "IF Sensor.type=""H2S"" AND Sensor.pod=""2"" THEN
%  (Sensor.noise=""1"" AND Sensor.upperlimit=""100"") OR
%  (Sensor.noise=""0,05"" AND Sensor.upperlimit=""200"") OR
%  (Sensor.noise=""0,5"" AND Sensor.upperlimit=""2000"") OR
%  (Sensor.noise=""0,02"" AND Sensor.upperlimit=""50"")"
% -----> SAME WITH VALIDATED (C61)

sensor_noise100_and_upperlimit100(S) :- attributevalue_type_sensor_noise(S,100), attributevalue_type_sensor_upperlimit(S,100).
sensor_noise5_and_upperlimit200(S) :- attributevalue_type_sensor_noise(S,5), attributevalue_type_sensor_upperlimit(S,200).
sensor_noise50_and_upperlimit2000(S) :- attributevalue_type_sensor_noise(S,50), attributevalue_type_sensor_upperlimit(S,2000).
sensor_noise2_and_upperlimit50(S) :- attributevalue_type_sensor_noise(S,2), attributevalue_type_sensor_upperlimit(S,50).

:- attributevalue_type_sensor_type(S,h2s),
 attributevalue_type_sensor_pod(S,2),
not sensor_noise100_and_upperlimit100(S),
not sensor_noise5_and_upperlimit200(S),
not sensor_noise50_and_upperlimit2000(S),
not sensor_noise2_and_upperlimit50(S).


% C58: "IF Sensor.type=""H2S"" AND Sensor.pod=""5"" AND Sensor.dualsensor=""false"" THEN
%  (Sensor.noise=""0,2"" AND Sensor.upperlimit=""100"")"
% -----> SAME WITH VALIDATED (C54)

:- attributevalue_type_sensor_type(S,h2s),
attributevalue_type_sensor_pod(S,5),
attributevalue_type_sensor_dualsensor(S,no),
not attributevalue_type_sensor_upperlimit(S,100).

:- attributevalue_type_sensor_type(S,h2s),
attributevalue_type_sensor_pod(S,5),
attributevalue_type_sensor_dualsensor(S,no),
not attributevalue_type_sensor_noise(S,20).


% C59: "IF DeploymentEnvironment HAS Pollutant.type=""CO"" AND
%  DeploymentEnvironment HAS Pollutant.type=""H2S"" THEN
%  Sensor.noise=""0,25"" AND Sensor.upperlimit=""100"" AND Sensor.dualsensor=""true"""
% -----> SAME WITH VALIDATED (C54)
:- attributevalue_type_pollutant_type(P1,co),
attributevalue_type_pollutant_type(P2,h2s),
assoc_type_pollutant_and_type_sensor(P1,S1),
assoc_type_pollutant_and_type_sensor(P2,S2),
not attributevalue_type_sensor_noise(S1,25).

:- attributevalue_type_pollutant_type(P1,co),
attributevalue_type_pollutant_type(P2,h2s),
assoc_type_pollutant_and_type_sensor(P1,S1),
assoc_type_pollutant_and_type_sensor(P2,S2),
not attributevalue_type_sensor_noise(S2,25).


:- attributevalue_type_pollutant_type(P1,co),
attributevalue_type_pollutant_type(P2,h2s),
assoc_type_pollutant_and_type_sensor(P1,S1),
assoc_type_pollutant_and_type_sensor(P2,S2),
not attributevalue_type_sensor_upperlimit(S1,100).

:- attributevalue_type_pollutant_type(P1,co),
attributevalue_type_pollutant_type(P2,h2s),
assoc_type_pollutant_and_type_sensor(P1,S1),
assoc_type_pollutant_and_type_sensor(P2,S2),
not attributevalue_type_sensor_upperlimit(S2,100).


:- attributevalue_type_pollutant_type(P1,co),
attributevalue_type_pollutant_type(P2,h2s),
assoc_type_pollutant_and_type_sensor(P1,S1),
assoc_type_pollutant_and_type_sensor(P2,S2),
not attributevalue_type_sensor_dualsensor(S1,yes).

:- attributevalue_type_pollutant_type(P1,co),
attributevalue_type_pollutant_type(P2,h2s),
assoc_type_pollutant_and_type_sensor(P1,S1),
assoc_type_pollutant_and_type_sensor(P2,S2),
not attributevalue_type_sensor_dualsensor(S2,yes).


% C60: "IF DeploymentEnvironment HAS Pollutant.type=""NO"" THEN
%  Pollutant HAS Sensor.type=""NO"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, no), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,no).



% C61: "IF Sensor.type=""NO"" AND Sensor.pod=""2"" THEN
%  (Sensor.noise=""15"" AND Sensor.upperlimit=""20"") OR
%  (Sensor.noise=""0,15"" AND Sensor.upperlimit=""250"")"
% ----->  VALIDATED
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NEW_CONSTRAINT_TYPE: C61
% if (x) then (y and z) or (w and r)   %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

sensor_noise1500_and_upperlimit20(S) :- attributevalue_type_sensor_noise(S,1500), attributevalue_type_sensor_upperlimit(S,20).
sensor_noise15_and_upperlimit250(S) :- attributevalue_type_sensor_noise(S,15), attributevalue_type_sensor_upperlimit(S,250).

:- attributevalue_type_sensor_type(S,no),
attributevalue_type_sensor_pod(S,2),
not sensor_noise1500_and_upperlimit20(S),
not sensor_noise15_and_upperlimit250(S).

%validate
%type_sensor(8).
%attributevalue_type_sensor_type(8,no).
%attributevalue_type_sensor_pod(8,2).
%attributevalue_type_sensor_noise(8,1500).


% C62: "IF Pollutant.type=""NO"" AND Sensor.pod=""5"" THEN
%  (Sensor.noise=""0,1"" AND Sensor.upperlimit=""100"")"
% -----> SAME WITH VALIDATED (C54)
:- attributevalue_type_pollutant_type(P1,no),
attributevalue_type_sensor_pod(S,5),
assoc_type_pollutant_and_type_sensor(P1,S),
not attributevalue_type_sensor_noise(S,10).

:- attributevalue_type_pollutant_type(P1,no),
attributevalue_type_sensor_pod(S,5),
assoc_type_pollutant_and_type_sensor(P1,S),
not attributevalue_type_sensor_upperlimit(S,100).

% C63: "IF DeploymentEnvironment HAS Pollutant.type=""SO2"" THEN
%  Pollutant HAS Sensor.type=""SO2"""
% -----> SAME WITH VALIDATED (C4)

1{type_sensor(S) : attributevalue_type_sensor_type(S, so2), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,so2).


% C64: IF Sensor.type="SO2" AND Sensor.pod="2" THEN -> A
%  (Sensor.noise="5" AND Sensor.upperlimit="100") OR -> B
%  (Sensor.noise="2" AND Sensor.upperlimit="2000") OR -> C
%  (Sensor.noise="0,1" AND Sensor.upperlimit="100") -> D
% -----> SAME WITH VALIDATED (C61)

sensor_noise500_and_upperlimit100(S) :- attributevalue_type_sensor_noise(S,500), attributevalue_type_sensor_upperlimit(S,100).
sensor_noise200_and_upperlimit2000(S) :- attributevalue_type_sensor_noise(S,200), attributevalue_type_sensor_upperlimit(S,2000).
sensor_noise10_and_upperlimit100(S) :- attributevalue_type_sensor_noise(S,10), attributevalue_type_sensor_upperlimit(S,100).

:- attributevalue_type_sensor_type(S,so2),
 attributevalue_type_sensor_pod(S,2),
not sensor_noise500_and_upperlimit100(S),
not sensor_noise200_and_upperlimit2000(S),
not sensor_noise10_and_upperlimit100(S).



% C65: "IF Pollutant.type=""SO2"" AND Sensor.pod=""5"" THEN
%  (Sensor.noise=""0,2"" AND Sensor.upperlimit=""20"")"
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_pollutant_type(P,so2),
attributevalue_type_sensor_pod(S,5),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,20).

  :- attributevalue_type_pollutant_type(P,so2),
attributevalue_type_sensor_pod(S,5),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,20).


% C66: "IF DeploymentEnvironment HAS Pollutant.type=""CL2"" THEN
%  Pollutant HAS Sensor.type=""CL2"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, cl2), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,cl2).


% C67: "IF Sensor.type=""CL2"" AND Sensor.pod=""2""
% THEN Sensor.noise=""0,02"" AND Sensor.upperlimit=""20"""
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_pollutant_type(P,cl2),
attributevalue_type_sensor_pod(S,2),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,2).

  :- attributevalue_type_pollutant_type(P,cl2),
attributevalue_type_sensor_pod(S,2),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,20).



% C68: "IF Pollutant.type=""CL2"" AND Sensor.pod=""5""
%  THEN Sensor.noise=""0,1"" AND Sensor.upperlimit=""20"""
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_pollutant_type(P,cl2),
attributevalue_type_sensor_pod(S,5),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,10).

  :- attributevalue_type_pollutant_type(P,cl2),
attributevalue_type_sensor_pod(S,5),
 assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,20).


% C69: "IF DeploymentEnvironment HAS Pollutant.type=""H2"" THEN
%  Pollutant HAS Sensor.type=""H2"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, h2), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,h2).

% C70: IF Sensor.type="H2" THEN Sensor.noise="0,8" AND Sensor.upperlimit="5000"
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_sensor_type(S,h2),
not attributevalue_type_sensor_noise(S,80).

  :- attributevalue_type_sensor_type(S,h2),
not attributevalue_type_sensor_upperlimit(S,5000).


% C71: "IF DeploymentEnvironment HAS Pollutant.type=""ETO"" THEN
%  Pollutant HAS Sensor.type=""ETO"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, h2), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,h2).


% C72: IF Sensor.type="ETO" THEN Sensor.noise="0,1" AND Sensor.upperlimit="100"
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_sensor_type(S,eto), type_sensor(S),
not attributevalue_type_sensor_noise(S,10).

  :- attributevalue_type_sensor_type(S,eto), type_sensor(S),
not attributevalue_type_sensor_upperlimit(S,100).

% C73: "IF DeploymentEnvironment HAS Pollutant.type=""HCL"" THEN
%  Pollutant HAS Sensor.type=""HCL"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, hcl), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,hcl).


% C74: IF Sensor.type="HCL" THEN Sensor.noise="0,1" AND Sensor.upperlimit="20"
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_sensor_type(S,hcl), type_sensor(S),
not attributevalue_type_sensor_noise(S,10).

  :- attributevalue_type_sensor_type(S,hcl), type_sensor(S),
not attributevalue_type_sensor_upperlimit(S,20).


% C75: "IF DeploymentEnvironment HAS Pollutant.type=""NH3"" THEN
%  Pollutant HAS Sensor.type=""NH3"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, nh3), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,nh3).

% C76: IF Sensor.type="NH3" THEN Sensor.noise="0,3" AND Sensor.upperlimit="100"
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_sensor_type(S,nh3), type_sensor(S),
not attributevalue_type_sensor_noise(S,30).

  :- attributevalue_type_sensor_type(S,nh3), type_sensor(S),
not attributevalue_type_sensor_upperlimit(S,100).

% C77: "IF DeploymentEnvironment HAS Pollutant.type=""HCN"" THEN
%  Pollutant HAS Sensor.type=""HCN"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, hcn), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,hcn).


% C78: "IF Sensor.type=""HCN"" AND Sensor.pod=""2""
%  THEN Sensor.noise=""5"" AND Sensor.upperlimit=""50"""
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_sensor_type(S,hcn), type_sensor(S),
attributevalue_type_sensor_pod(S,2),
not attributevalue_type_sensor_noise(S,500).

  :- attributevalue_type_sensor_type(S,hcn), type_sensor(S),
attributevalue_type_sensor_pod(S,2),
not attributevalue_type_sensor_upperlimit(S,50).


% C79: "IF Pollutant.type=""HCN"" AND Sensor.pod=""5""
%  THEN Sensor.noise=""0,3"" AND Sensor.upperlimit=""50"""
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_sensor_type(S,hcn), type_sensor(S),
attributevalue_type_sensor_pod(S,5),
not attributevalue_type_sensor_noise(S,30).

  :- attributevalue_type_sensor_type(S,hcn), type_sensor(S),
attributevalue_type_sensor_pod(S,5),
not attributevalue_type_sensor_upperlimit(S,50).

% C80: "IF DeploymentEnvironment HAS Pollutant.type=""PH3"" THEN
%  Pollutant HAS Sensor.type=""PH3"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, ph3), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,ph3).


% C81: "IF Sensor.type=""PH3"" THEN
%  (Sensor.noise=""0,03"" AND Sensor.upperlimit=""10"") OR
%  (Sensor.noise=""2"" AND Sensor.upperlimit=""2000"")"
% -----> SAME WITH VALIDATED (C61)

sensor_noise3_and_upperlimit10(S) :- attributevalue_type_sensor_noise(S,3), attributevalue_type_sensor_upperlimit(S,10).
sensor_noise200_and_upperlimit2000(S) :- attributevalue_type_sensor_noise(S,200), attributevalue_type_sensor_upperlimit(S,2000).

:- attributevalue_type_sensor_type(S,ph3),
not sensor_noise3_and_upperlimit10(S),
not sensor_noise200_and_upperlimit2000(S).


% C82: "IF DeploymentEnvironment HAS Pollutant.type=""PM10"" THEN
%  Pollutant HAS Sensor.type=""PM10""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, pm10), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,pm10).


% C83: IF DeploymentEnvironment HAS Pollutant.type=""PM2.5"" THEN
%  Pollutant HAS Sensor.type=""PM2.5""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, pm25), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,pm25).


% C84: IF DeploymentEnvironment HAS Pollutant.type=""PM1"" THEN
%  Pollutant HAS Sensor.type=""PM1""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, pm1), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,pm1).


% C85: IF DeploymentEnvironment HAS Pollutant.type=""PM0"" THEN
%  Pollutant HAS Sensor.type=""PM0"""
% -----> SAME WITH VALIDATED (C4)
1{type_sensor(S) : attributevalue_type_sensor_type(S, pm0), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,pm0).



% C86: "IF Pollutant.type=""PM10"" OR
%  Pollutant.type=""PM2.5"" OR
%  Pollutant.type=""PM1"" OR
%  Pollutant.type=""PM0"" THEN
%  Sensor.noise=""-1"" AND Sensor.upperlimit=""-1"""
% -----> SAME WITH VALIDATED (C10)

:- attributevalue_type_pollutant_type(P,pm10),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,-1).

:- attributevalue_type_pollutant_type(P,pm10),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,0).

:- attributevalue_type_pollutant_type(P,pm25),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,-1).

:- attributevalue_type_pollutant_type(P,pm25),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,0).

:- attributevalue_type_pollutant_type(P,pm1),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,-1).

:- attributevalue_type_pollutant_type(P,pm1),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,0).

:- attributevalue_type_pollutant_type(P,pm0),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,-1).

:- attributevalue_type_pollutant_type(P,pm0),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,0).


% C87: "IF DeploymentEnvironment HAS Pollutant.type=""A"" THEN
%  Pollutant HAS Sensor.type=""A"""
% -----> SAME WITH VALIDATED (C4)
  1{type_sensor(S) : attributevalue_type_sensor_type(S, a), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,a).

% C88: "IF DeploymentEnvironment HAS Pollutant.type=""B"" THEN
%  Pollutant HAS Sensor.type=""B"""
% -----> SAME WITH VALIDATED (C4)
  1{type_sensor(S) : attributevalue_type_sensor_type(S, b), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,b).


% C89: "IF DeploymentEnvironment HAS Pollutant.type=""G"" THEN
%  Pollutant HAS Sensor.type=""G"""
% -----> SAME WITH VALIDATED (C4)
  1{type_sensor(S) : attributevalue_type_sensor_type(S, g), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,g).


% C90: "IF DeploymentEnvironment HAS Pollutant.type=""Radon"" THEN
%  Pollutant HAS Sensor.type=""Radon"""
% -----> SAME WITH VALIDATED (C4)
  1{type_sensor(S) : attributevalue_type_sensor_type(S, radon), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,radon).


% C91: "IF Pollutant.type=""A"" OR
%  Pollutant.type=""B"" OR
%  Pollutant.type=""G"" OR
%  Pollutant.type=""Radon"" THEN
%  Sensor.noise=""-1"" AND Sensor.upperlimit=""-1"""
% -----> SAME WITH VALIDATED (C10)

:- attributevalue_type_pollutant_type(P,a),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,0).

:- attributevalue_type_pollutant_type(P,a),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,0).

:- attributevalue_type_pollutant_type(P,b),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,0).

:- attributevalue_type_pollutant_type(P,b),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,0).

:- attributevalue_type_pollutant_type(P,g),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,0).

:- attributevalue_type_pollutant_type(P,g),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,0).

:- attributevalue_type_pollutant_type(P,radon),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_noise(S,0).

:- attributevalue_type_pollutant_type(P,radon),
assoc_type_pollutant_and_type_sensor(P,S),
not attributevalue_type_sensor_upperlimit(S,0).


% C92: "IF DeploymentEnvironment HAS Pollutant.type=""CO2"" THEN
%  Pollutant HAS Sensor.type=""CO2"""
% -----> SAME WITH VALIDATED (C4)
  1{type_sensor(S) : attributevalue_type_sensor_type(S, co2), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,co2).


% C93: IF Sensor.type="CO2" THEN Sensor.noise="5" AND Sensor.upperlimit="5000"
% -----> SAME WITH VALIDATED (C54)
  :- attributevalue_type_sensor_type(S,co2),
not attributevalue_type_sensor_upperlimit(S,5000).

  :- attributevalue_type_sensor_type(S,co2),
not attributevalue_type_sensor_noise(S,500).


% C94: "IF DeploymentEnvironment HAS Pollutant.type=""CH4"" THEN
%  Pollutant HAS Sensor.type=""CH4"""
% -----> SAME WITH VALIDATED (C4)
  1{type_sensor(S) : attributevalue_type_sensor_type(S, ch4), assoc_type_pollutant_and_type_sensor(P,S)}1
:- attributevalue_type_pollutant_type(P,ch4).



% C95: "IF Pollutant.type=""CH4"" THEN
%  (Sensor.noise=""200"" AND Sensor.upperlimit=""2,5"") OR
%  (Sensor.noise=""300"" AND Sensor.upperlimit=""100"")"
% -----> SAME WITH VALIDATED (C61)

sensor_noise20000_and_upperlimit2(S) :- attributevalue_type_sensor_noise(S,20000), attributevalue_type_sensor_upperlimit(S,2).
sensor_noise30000_and_upperlimit100(S) :- attributevalue_type_sensor_noise(S,30000), attributevalue_type_sensor_upperlimit(S,100).

:- attributevalue_type_pollutant_type(P,ch4),
assoc_type_pollutant_and_type_sensor(P,S),
not sensor_noise20000_and_upperlimit2(S),
not sensor_noise30000_and_upperlimit100(S).


% C96: "IF Pollutant.type=""PM10"" OR Pollutant.type=""PM2.5"" OR
%  Pollutant.type=""PM1"" OR Pollutant.type=""PM0"" THEN
%  Pollutant.metric=""Ppm"" AND
%  POllutant.rawmetric=""true"" AND
%  Sensor HAS MonitoringPolicy.automaticsampling=""true"" AND
%             MonitoringPolicy.publishrate=""1"""
% -----> VALIDATED
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% NEW_CONSTRAINT_TYPE: C96
% if (x or y)  then (y and has z and w) %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% validate c96
%type_pollutant(3000).
%attributevalue_type_pollutant_type(3000,pm10).
%attributevalue_type_pollutant_metric(3000,ppm).

%type_sensor(5000).
%assoc_type_pollutant_and_type_sensor(3000,5000).
%type_monitoringpolicy(7000).
%assoc_type_sensor_and_type_monitoringpolicy(5000,7000).
%attributevalue_type_monitoringpolicy_autosampling(7000,yes).


:- attributevalue_type_pollutant_type(P,pm10),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,pm10),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,pm10),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,pm10),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).


:- attributevalue_type_pollutant_type(P,pm25),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,pm25),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,pm25),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,pm25),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).



:- attributevalue_type_pollutant_type(P,pm1),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,pm1),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,pm1),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,pm1),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).


:- attributevalue_type_pollutant_type(P,pm0),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,pm0),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,pm0),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,pm0),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).



% C97:"IF Pollutant.type=""CO2"" OR Pollutant.type=""COV"" THEN
%   Pollutant.metric=""Ppm"" AND
%   POllutant.rawmetric=""true"" AND
%   Sensor HAS MonitoringPolicy.automaticsampling=""true"" AND
%             MonitoringPolicy.publishrate=""1"""
% -----> SAME WITH VALIDATED (C96)

:- attributevalue_type_pollutant_type(P,co2),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,co2),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,co2),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,co2),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).


:- attributevalue_type_pollutant_type(P,cov),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,cov),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,cov),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,cov),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).


% C98:"IF Pollutant.type=""CO"" OR Pollutant.type=""H2S"" OR Pollutant.type=""NO2"" OR
%   Pollutant.type=""NO"" OR Pollutant.type=""O3"" OR Pollutant.type=""SO2"" THEN
%   Pollutant.metric=""Ppm"" AND
%   POllutant.rawmetric=""false"" AND
%   Sensor HAS MonitoringPolicy.automaticsampling=""true"" AND
%             MonitoringPolicy.publishrate=""1"""
% -----> SAME WITH VALIDATED (C96)

:- attributevalue_type_pollutant_type(P,co),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,co),
not attributevalue_type_pollutant_rawmetric(P,false).

:- attributevalue_type_pollutant_type(P,co),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,co),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).



:- attributevalue_type_pollutant_type(P,h2s),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,h2s),
not attributevalue_type_pollutant_rawmetric(P,false).

:- attributevalue_type_pollutant_type(P,h2s),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,h2s),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).



:- attributevalue_type_pollutant_type(P,no2),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,no2),
not attributevalue_type_pollutant_rawmetric(P,false).

:- attributevalue_type_pollutant_type(P,no2),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,no2),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).



:- attributevalue_type_pollutant_type(P,no),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,no),
not attributevalue_type_pollutant_rawmetric(P,false).

:- attributevalue_type_pollutant_type(P,no),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,no),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).



:- attributevalue_type_pollutant_type(P,o3),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,o3),
not attributevalue_type_pollutant_rawmetric(P,false).

:- attributevalue_type_pollutant_type(P,o3),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,o3),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).



:- attributevalue_type_pollutant_type(P,so2),
not attributevalue_type_pollutant_metric(P,ppm).

:- attributevalue_type_pollutant_type(P,so2),
not attributevalue_type_pollutant_rawmetric(P,false).

:- attributevalue_type_pollutant_type(P,so2),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,so2),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).



% C99:"IF Pollutant.type=""A"" OR Pollutant.type=""B"" OR Pollutant.type=""G"" OR
%  Pollutant.type=""Radon"" THEN
%  Pollutant.metric=""Countsperminute"" AND
%  POllutant.rawmetric=""true"" AND
%  Sensor HAS MonitoringPolicy.automaticsampling=""true"" AND
%            MonitoringPolicy.publishrate=""1"""
% -----> SAME WITH VALIDATED (C96)

:- attributevalue_type_pollutant_type(P,a),
not attributevalue_type_pollutant_metric(P,countsperminute).

:- attributevalue_type_pollutant_type(P,a),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,a),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,a),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).


:- attributevalue_type_pollutant_type(P,b),
not attributevalue_type_pollutant_metric(P,countsperminute).

:- attributevalue_type_pollutant_type(P,b),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,b),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,b),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).


:- attributevalue_type_pollutant_type(P,g),
not attributevalue_type_pollutant_metric(P,countsperminute).

:- attributevalue_type_pollutant_type(P,g),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,g),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,g),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).


:- attributevalue_type_pollutant_type(P,radon),
not attributevalue_type_pollutant_metric(P,countsperminute).

:- attributevalue_type_pollutant_type(P,radon),
not attributevalue_type_pollutant_rawmetric(P,yes).

:- attributevalue_type_pollutant_type(P,radon),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_autosampling(M,yes).

:- attributevalue_type_pollutant_type(P,radon),
assoc_type_pollutant_and_type_sensor(P,S),
assoc_type_sensor_and_type_monitoringpolicy(S,M),
not attributevalue_type_monitoringpolicy_publishrate(M,1).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CUSTOMER REQUIREMENTS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% WILL BE TAKEN FROM THE UI


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% OUTPUTS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% for program
#show type_monitoringstation/1.
#show attributevalue_type_monitoringstation_communication/2.
#show attributevalue_type_monitoringstation_localstorage/2.
#show attributevalue_type_monitoringstation_cloudstorage/2.
#show attributevalue_type_monitoringstation_enclosure/2.

#show assoc_type_monitoringstation_and_type_deploymentenvironment/2.

#show type_deploymentenvironment/1.
#show attributevalue_type_deploymentenvironment_type/2.
#show attributevalue_type_deploymentenvironment_context/2.
#show attributevalue_type_deploymentenvironment_locationtype/2.

#show assoc_type_deploymentenvironment_and_type_area/2.
#show assoc_type_deploymentenvironment_and_type_pollutant/2.

#show type_area/1.
#show attributevalue_type_area_type/2.
#show attributevalue_type_area_category/2.
#show attributevalue_type_area_prefabricatedbuilding/2.
#show attributevalue_type_area_vehicletraffic/2.
#show attributevalue_type_area_industrialtype/2.
#show attributevalue_type_area_pollutedsoil/2.
#show attributevalue_type_area_floor/2.
#show attributevalue_type_area_controlledarea/2.
#show attributevalue_type_area_airconditioning/2.
#show attributevalue_type_area_heatingsystem/2.
#show attributevalue_type_area_windows/2.
#show attributevalue_type_area_smokepresence/2.
#show attributevalue_type_area_moldpresence/2.
#show attributevalue_type_area_dustyarea/2.

#show assoc_type_area_and_type_environmetalconditions/2.
#show assoc_type_area_and_type_walltype/2.

#show type_environmetalconditions/1.
#show attributevalue_type_environmetalconditions_humidity/2.
#show attributevalue_type_environmetalconditions_windspeed/2.
#show attributevalue_type_environmetalconditions_rain/2.
#show attributevalue_type_environmetalconditions_dust/2.
#show attributevalue_type_environmetalconditions_averagetemperature/2.
#show attributevalue_type_environmetalconditions_snow/2.
#show attributevalue_type_environmetalconditions_ice/2.
#show attributevalue_type_environmetalconditions_vibrations/2.
#show attributevalue_type_environmetalconditions_averagepressure/2.

#show type_walltype/1.
#show attributevalue_type_walltype_wallpaper/2.
#show attributevalue_type_walltype_plasticcladding/2.
#show attributevalue_type_walltype_woodenpanels/2.
#show attributevalue_type_walltype_moquette/2.
#show attributevalue_type_walltype_tiles/2.
#show attributevalue_type_walltype_plaster/2.

#show type_pollutant/1.
#show attributevalue_type_pollutant_type/2.
#show attributevalue_type_pollutant_gas/2.
#show attributevalue_type_pollutant_electromagnetic/2.
#show attributevalue_type_pollutant_ionizing/2.
#show attributevalue_type_pollutant_affectair/2.
#show attributevalue_type_pollutant_affectwater/2.
#show attributevalue_type_pollutant_affectsoil/2.
#show attributevalue_type_pollutant_affectobjects/2.
#show attributevalue_type_pollutant_ban/2.
#show attributevalue_type_pollutant_specific/2.
#show attributevalue_type_pollutant_metric/2.
#show attributevalue_type_pollutant_rawmetric/2.

#show assoc_type_pollutant_and_type_infferingpollutant/2.
#show assoc_type_pollutant_and_type_sensor/2.

#show type_infferingpollutant/1.
#show attributevalue_type_infferingpollutant_type/2.
#show attributevalue_type_infferingpollutant_gas/2.
#show attributevalue_type_infferingpollutant_electromagnetic/2.
#show attributevalue_type_infferingpollutant_ionizing/2.
#show attributevalue_type_infferingpollutant_affectair/2.
#show attributevalue_type_infferingpollutant_affectwater/2.
#show attributevalue_type_infferingpollutant_affectsoil/2.
#show attributevalue_type_infferingpollutant_affectobjects/2.
#show attributevalue_type_infferingpollutant_ban/2.
#show attributevalue_type_infferingpollutant_explosive/2.
#show attributevalue_type_infferingpollutant_toxic/2.
#show attributevalue_type_infferingpollutant_dangerous/2.
#show attributevalue_type_infferingpollutant_poisonous/2.
#show attributevalue_type_infferingpollutant_metric/2.

#show type_sensor/1.
#show attributevalue_type_sensor_type/2.
#show attributevalue_type_sensor_technology/2.
#show attributevalue_type_sensor_sensitivity/2.
#show attributevalue_type_sensor_pod/2.
#show attributevalue_type_sensor_range/2.
#show attributevalue_type_sensor_resolution/2.
#show attributevalue_type_sensor_responsetime/2.
#show attributevalue_type_sensor_expectedoperationlife/2.
#show attributevalue_type_sensor_minoptemperature/2.
#show attributevalue_type_sensor_maxoptemperature/2.
#show attributevalue_type_sensor_minophumidity/2.
#show attributevalue_type_sensor_maxophumidity/2.
#show attributevalue_type_sensor_minoppressure/2.
#show attributevalue_type_sensor_maxoppressure/2.
#show attributevalue_type_sensor_upperlimit/2.
#show attributevalue_type_sensor_dualsensor/2.
#show attributevalue_type_sensor_noise/2.

#show assoc_type_sensor_and_type_monitoringpolicy/2.
#show assoc_type_sensor_and_type_referencemodel/2.

#show type_referencemodel/1.
#show attributevalue_type_referencemodel_monitoringalgorithm/2.

#show type_monitoringpolicy/1.
#show attributevalue_type_monitoringpolicy_autosampling/2.
#show attributevalue_type_monitoringpolicy_samplingtype/2.
#show attributevalue_type_monitoringpolicy_duration/2.
#show attributevalue_type_monitoringpolicy_publishrate/2.
