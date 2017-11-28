% Copyright (c) 2017-11-28 Müslüm Atas & Seda Polat-Erdeniz & Christoph Uran (TU Graz)
%
% This program and the accompanying materials are made
% available under the terms of the Eclipse Public License 2.0
% which is available at https://www.eclipse.org/legal/epl-2.0/
%
% SPDX-License-Identifier: EPL-2.0

% to execute the program please use Clingo from http://potassco.sourceforge.net/
% which is combines Gringo and Claps in a monolithic way;
% use the following command line:  clingo.exe SmartHOMEmodelASP.lp

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Components (instances) - for the corresponding component types see
%                          the follow-up definitions
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% one smarthome instance is defined here (since we plan to configure one smarthome)
% smarthome instance id is "1".
potentialinstance_smarthome(1).

% max. 5 rooms per smarthome -> max. 5 instances are possible (and defined here)
% room instance ids start with "11".
potentialinstance_room(11;12;13;14;15).

% max. 2 homeappliances per room (i.e., max. 10 homeapplicance instances)
% home appliance ids start with "21!
potentialinstance_homeappliance(21;22;23;24;25;26;27;28;29;30).

% one userrequirements instance
% userrequirements instance id is "2"
potentialinstance_userrequirements(2).

% PARSER_UserRequirements

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Component types
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Component type "smarthome"
0{ type_smarthome(X) }1 :- potentialinstance_smarthome(X).

% Component type "room"
0{ type_room(X) }1 :- potentialinstance_room(X).

% Component type "homeappliance"
0{ type_homeappliance(X) }1 :- potentialinstance_homeappliance(X).

% Component type "userrequirements"
0{ type_userrequirements(X) }1 :- potentialinstance_userrequirements(X).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Component type attribute domains
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Component type "smarthome"
% just examples ... needs to be completed
attributedomain_type_smarthome_builtwith(steel;wood;other).
attributedomain_type_smarthome_communication(wired;wireless).
attributedomain_type_smarthome_aretubesenabled(yes;no).

1{attributevalue_type_smarthome_builtwith(X,Y): attributedomain_type_smarthome_builtwith(Y)}1 :- type_smarthome(X).
1{attributevalue_type_smarthome_communication(X,Y): attributedomain_type_smarthome_communication(Y)}1 :- type_smarthome(X).
1{attributevalue_type_smarthome_aretubesenabled(X,Y): attributedomain_type_smarthome_aretubesenabled(Y)}1 :- type_smarthome(X).

% Component type "room"
attributedomain_type_room_roomtype(kitchen;bathroom;livingroom;bedroom;corridor;other).

1{attributevalue_type_room_roomtype(X,Y): attributedomain_type_room_roomtype(Y)}1 :- type_room(X).

% Component type "homeappliances"
attributedomain_type_homeappliance_homeappliancetype(oven;stove;microwave;dishwasher;fridge;radio;freezer;tv;console;router;computer;server;washingmachine).
attributedomain_type_homeappliance_isalwayson(yes;no).
attributedomain_type_homeappliance_isdangerous(yes;no).

1{attributevalue_type_homeappliance_homeappliancetype(X,Y): attributedomain_type_homeappliance_homeappliancetype(Y)}1 :- type_homeappliance(X).
1{attributevalue_type_homeappliance_isalwayson(X,Y): attributedomain_type_homeappliance_isalwayson(Y)}1 :- type_homeappliance(X).
1{attributevalue_type_homeappliance_isdangerous(X,Y): attributedomain_type_homeappliance_isdangerous(Y)}1 :- type_homeappliance(X).

% Component type "userrequirements"
attributedomain_type_userrequirements_iscomfortcontrolneeded(yes;no).
attributedomain_type_userrequirements_isenenergysavingneeded(yes;no).
attributedomain_type_userrequirements_ishealthsupportneeded(yes;no).
attributedomain_type_userrequirements_issafetysecurityneeded(yes;no).
attributedomain_type_userrequirements_iscostimportant(yes;no).
attributedomain_type_userrequirements_isstabilityneeded(yes;no).
attributedomain_type_userrequirements_issensibletoelectricsmog(yes;no).
attributedomain_type_userrequirements_installation(diy;professional).

1{attributevalue_type_userrequirements_iscomfortcontrolneeded(X,Y): attributedomain_type_userrequirements_iscomfortcontrolneeded(Y)}1 :- type_userrequirements(X).
1{attributevalue_type_userrequirements_isenenergysavingneeded(X,Y): attributedomain_type_userrequirements_isenenergysavingneeded(Y)}1 :- type_userrequirements(X).
1{attributevalue_type_userrequirements_ishealthsupportneeded(X,Y): attributedomain_type_userrequirements_ishealthsupportneeded(Y)}1 :- type_userrequirements(X).
1{attributevalue_type_userrequirements_issafetysecurityneeded(X,Y): attributedomain_type_userrequirements_issafetysecurityneeded(Y)}1 :- type_userrequirements(X).
1{attributevalue_type_userrequirements_iscostimportant(X,Y): attributedomain_type_userrequirements_iscostimportant(Y)}1 :- type_userrequirements(X).
1{attributevalue_type_userrequirements_isstabilityneeded(X,Y): attributedomain_type_userrequirements_isstabilityneeded(Y)}1 :- type_userrequirements(X).
1{attributevalue_type_userrequirements_issensibletoelectricsmog(X,Y): attributedomain_type_userrequirements_issensibletoelectricsmog(Y)}1 :- type_userrequirements(X).
1{attributevalue_type_userrequirements_installation(X,Y): attributedomain_type_userrequirements_installation(Y)}1 :- type_userrequirements(X).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Component type generalization hierarchies
% no generalization is used in the smarthome example
% however: it could be used for room types (instead of the user enumeration
%          of room types
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Partof relationships (associations between classes)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% Each userrequirement instance is connected to one smarthome instance
% and vice-versa
1{assoc_type_smarthome_and_type_userrequirements(Y,X): type_smarthome(Y)}1 :- type_userrequirements(X).
1{assoc_type_smarthome_and_type_userrequirements(X,Y): type_userrequirements(Y)}1 :- type_smarthome(X).

% Each room is connected to exactly one smarthome instance
% Each smarthome instance is connected to max.5 rooms
1{assoc_type_smarthome_and_type_room(Y,X): type_smarthome(Y)}1 :- type_room(X).
1{assoc_type_smarthome_and_type_room(X,Y): type_room(Y)}5 :- type_smarthome(X).

% Each homeappliance is connected to exactly one room
% Each room instance is connected to max.2 homeappliances
1{assoc_type_room_and_type_homeappliance(Y,X): type_room(Y)}1 :- type_homeappliance(X).
1{assoc_type_room_and_type_homeappliance(X,Y): type_homeappliance(Y)}2 :- type_room(X).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Customer requirements (should then be collected via the user interface ...)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
type_smarthome(1).
type_userrequirements(2).
attributevalue_type_userrequirements_issensibletoelectricsmog(2,yes).

% in the smarthome there should be only one washing machine
%0{attributevalue_type_homeappliance_homeappliancetype(X,washingmachine): type_homeappliance(X)}1.
:- attributevalue_type_homeappliance_homeappliancetype(A1, washingmachine),
   A1 != A2,
   attributevalue_type_homeappliance_homeappliancetype(A2, washingmachine).

% in the smarthome there should be only one server
%0{attributevalue_type_homeappliance_homeappliancetype(X,server): type_homeappliance(X)}1.
:- attributevalue_type_homeappliance_homeappliancetype(A1, server),
   A1 != A2,
   attributevalue_type_homeappliance_homeappliancetype(A2, server).

% in the smarthome there should be only one kitchen
%1{attributevalue_type_room_roomtype(X,kitchen): type_room(X)}1.
:- attributevalue_type_room_roomtype(R1, kitchen),
   assoc_type_smarthome_and_type_room(H,R1),
   R1 != R2,
   assoc_type_smarthome_and_type_room(H,R2),
   attributevalue_type_room_roomtype(R2, kitchen).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Constraints (Configuration Knowledge Base)
% only examples, not complete
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

% C1 : Full Comfort/Control is NOT compatible with energy saving - because infrastructure has to run permanently
:- attributevalue_type_userrequirements_iscomfortcontrolneeded(X,yes), attributevalue_type_userrequirements_isenenergysavingneeded(X,yes).

%C2 : Full Support NOT compatible to energy/saving â€“ because parts of the infrastructure have to run permanently, in some cases even redundant subsystems make sense
:- attributevalue_type_userrequirements_ishealthsupportneeded(X,yes), attributevalue_type_userrequirements_isenenergysavingneeded(X,yes).

% C3 : Full Security/Safety NOT compatible to energy/saving
:- attributevalue_type_userrequirements_issafetysecurityneeded(X,yes), attributevalue_type_userrequirements_isenenergysavingneeded(X,yes).

%C4 : Full comfort NOT compatible to full safety/security  - due to the possibility of remote access/ hacking
:- attributevalue_type_userrequirements_iscomfortcontrolneeded(X,yes), attributevalue_type_userrequirements_issafetysecurityneeded(X,yes).

%age is not defined at the moment! Do we need this constraint?
%omitted: C5 : If the building is more than ~ 30 years old, the chance to be able to retrofit bus wires is low. In this case a radio based system is probably the better solution.

%C6 : If costs play an important role, a wireless system is also indicated, but this contradicts (in general) stability, and security or if the people interested are sensible to electric smog.
:- assoc_type_smarthome_and_type_userrequirements(X,Y), attributevalue_type_userrequirements_iscostimportant(Y,yes), attributevalue_type_smarthome_communication(X,wired).
:- attributevalue_type_userrequirements_iscostimportant(X,yes), attributevalue_type_userrequirements_issafetysecurityneeded(X,yes).
:- attributevalue_type_userrequirements_iscostimportant(X,yes), attributevalue_type_userrequirements_isstabilityneeded(X,yes).

%C7 : If the building is established with steel concrete, a wireless system may also cause problems.
:- attributevalue_type_smarthome_builtwith(X,steel), attributevalue_type_smarthome_communication(X,wireless).

%C8 : If the plan is to follow a do it yourself (DIY) approach, wired systems are not appropriate due to limitations in programmability and installation possibilities for lay persons.  Wireless systems typically offer DIY.
:- attributevalue_type_userrequirements_installation(Y,diy), attributevalue_type_smarthome_communication(X,wired).

%switch is not available yet.
%C9 :  If the goal is saving energy, all devices in a home should be switched off when they are not needed. The exceptions are, for example, refrigerators or freezers, which should not be switched off.

% C10: An oven must not be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,oven), attributevalue_type_homeappliance_isalwayson(X,yes).

% C11: A stove must not be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,stove), attributevalue_type_homeappliance_isalwayson(X,yes).

% C12: A microwave must not be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,microwave), attributevalue_type_homeappliance_isalwayson(X,yes).

% C13: A dishwasher must be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,dishwasher), attributevalue_type_homeappliance_isalwayson(X,no).

% C14: A fridge must always be on
:- attributevalue_type_homeappliance_homeappliancetype(X,fridge), attributevalue_type_homeappliance_isalwayson(X,no).

%C15: A radio must not be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,radio), attributevalue_type_homeappliance_isalwayson(X,yes).

%C16: A freezer must be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,freezer), attributevalue_type_homeappliance_isalwayson(X,no).

%C17: A TV must not be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,tv), attributevalue_type_homeappliance_isalwayson(X,yes).

%C18: A console must not be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,console), attributevalue_type_homeappliance_isalwayson(X,yes).

%C19: A router must be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,router), attributevalue_type_homeappliance_isalwayson(X,no).

%C20: A computer must not be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,computer), attributevalue_type_homeappliance_isalwayson(X,yes).

%C21: A server must be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,server), attributevalue_type_homeappliance_isalwayson(X,no).

%C22: A washing machine must be always on
:- attributevalue_type_homeappliance_homeappliancetype(X,washingmachine), attributevalue_type_homeappliance_isalwayson(X,no).

%C23: An oven is dangerous (which means an oven must have the value isDangerous = yes).
:- attributevalue_type_homeappliance_homeappliancetype(X,oven), attributevalue_type_homeappliance_isdangerous(X,no).

%C24: A stove is dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,stove), attributevalue_type_homeappliance_isdangerous(X,no).

%C25: A microwave is dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,microwave), attributevalue_type_homeappliance_isdangerous(X,no).

%C26: A dishwasher is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,dishwasher), attributevalue_type_homeappliance_isdangerous(X,yes).

%C27: A fridge is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,fridge), attributevalue_type_homeappliance_isdangerous(X,yes).

%C28: A radio is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,radio), attributevalue_type_homeappliance_isdangerous(X,yes).

%C29: A freezer is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,freezer), attributevalue_type_homeappliance_isdangerous(X,yes).

%C30: A TV is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,tv), attributevalue_type_homeappliance_isdangerous(X,yes).

%C31: A console is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,console), attributevalue_type_homeappliance_isdangerous(X,yes).

%C32: A router is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,router), attributevalue_type_homeappliance_isdangerous(X,yes).

%C33: A computer is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,computer), attributevalue_type_homeappliance_isdangerous(X,yes).

%C34: A server is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,server), attributevalue_type_homeappliance_isdangerous(X,yes).

%C35: A washing machine is not dangerous
:- attributevalue_type_homeappliance_homeappliancetype(X,washingmachine), attributevalue_type_homeappliance_isdangerous(X,yes).

%C36: Oven only in Kitchen
:- assoc_type_room_and_type_homeappliance(Y,X), attributevalue_type_homeappliance_homeappliancetype(X,oven), not attributevalue_type_room_roomtype(Y,kitchen).

%C37: Stove only in Kitchen
:- assoc_type_room_and_type_homeappliance(Y,X), attributevalue_type_homeappliance_homeappliancetype(X,stove), not attributevalue_type_room_roomtype(Y,kitchen).

%C38: Microwave only in Kitchen
:- assoc_type_room_and_type_homeappliance(Y,X), attributevalue_type_homeappliance_homeappliancetype(X,microwave), not attributevalue_type_room_roomtype(Y,kitchen).

%C39: Dishwasher only in Kitchen
:- assoc_type_room_and_type_homeappliance(Y,X), attributevalue_type_homeappliance_homeappliancetype(X,dishwasher), not attributevalue_type_room_roomtype(Y,kitchen).

%C40: Freezer only in Kitchen
:- assoc_type_room_and_type_homeappliance(Y,X), attributevalue_type_homeappliance_homeappliancetype(X,freezer), not attributevalue_type_room_roomtype(Y,kitchen).

%C41: TV not in Bathroom
:- assoc_type_room_and_type_homeappliance(X,Y), attributevalue_type_room_roomtype(X,bathroom), attributevalue_type_homeappliance_homeappliancetype(Y,tv).

%C42: TV not in Corridor
:- assoc_type_room_and_type_homeappliance(X,Y), attributevalue_type_room_roomtype(X,corridor), attributevalue_type_homeappliance_homeappliancetype(Y,tv).

%C43: Console only in the same room as a TV (console requires TV)
%:- assoc_type_room_and_type_homeappliance(X,Y),not assoc_type_room_and_type_homeappliance(X,Z), attributevalue_type_homeappliance_homeappliancetype(Y,console), attributevalue_type_homeappliance_homeappliancetype(Z,tv).
%:- assoc_type_room_and_type_homeappliance(X1,Y), assoc_type_room_and_type_homeappliance(X2,Z), not X1==X2, attributevalue_type_homeappliance_homeappliancetype(Y,console), attributevalue_type_homeappliance_homeappliancetype(Z,tv).
:- assoc_type_room_and_type_homeappliance(X1,Y), attributevalue_type_homeappliance_homeappliancetype(Y,console), assoc_type_room_and_type_homeappliance(X2,Z), attributevalue_type_homeappliance_homeappliancetype(Z,tv), X1!=X2.

% C44: Router not in Bathroom
:- assoc_type_room_and_type_homeappliance(X,Y), attributevalue_type_room_roomtype(X,bathroom), attributevalue_type_homeappliance_homeappliancetype(Y,router).

%C45: Computer not in Bathroom
:- assoc_type_room_and_type_homeappliance(X,Y), attributevalue_type_room_roomtype(X,bathroom), attributevalue_type_homeappliance_homeappliancetype(Y,computer).

%C46: Computer not in Corridor
:- assoc_type_room_and_type_homeappliance(X,Y), attributevalue_type_room_roomtype(X,corridor), attributevalue_type_homeappliance_homeappliancetype(Y,computer).

%C47: Server not in Bathroom
:- assoc_type_room_and_type_homeappliance(X,Y), attributevalue_type_room_roomtype(X,bathroom), attributevalue_type_homeappliance_homeappliancetype(Y,server).

%C48: Server not in Corridor
:- assoc_type_room_and_type_homeappliance(X,Y), attributevalue_type_room_roomtype(X,corridor), attributevalue_type_homeappliance_homeappliancetype(Y,server).

%C49: WashingMachine not in Livingroom
:- assoc_type_room_and_type_homeappliance(X,Y), attributevalue_type_room_roomtype(X,livingroom), attributevalue_type_homeappliance_homeappliancetype(Y,washingmachine).

%C50: WashingMachine not in Bedroom
:- assoc_type_room_and_type_homeappliance(X,Y), attributevalue_type_room_roomtype(X,bedroom), attributevalue_type_homeappliance_homeappliancetype(Y,washingmachine).

%Implementation is optional because in the current model there are no sensors and actuators.
%C51: If a room has a Stove, do not put smoke sensors in it.

%C52 If a device is not always on, put an electricity switch to it.
%switch is not available yet
%:- attributevalue_type_homeappliance_isalwayson(X, yes), attributevalue_type_XX_switch(X, no).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Configuration results that should be shown
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
#show type_smarthome/1.
#show attributevalue_type_smarthome_aretubesenabled/2.
#show attributevalue_type_smarthome_builtwith/2.
#show attributevalue_type_smarthome_communication/2.
#show type_userrequirements/1.
#show assoc_type_userrequirements_and_type_smarthome/2.
#show attributevalue_type_userrequirements_iscomfortcontrolneeded/2.
#show attributevalue_type_userrequirements_isenenergysavingneeded/2.
#show attributevalue_type_userrequirements_ishealthsupportneeded/2.
#show attributevalue_type_userrequirements_issafetysecurityneeded/2.
#show attributevalue_type_userrequirements_iscostimportant/2.
#show attributevalue_type_userrequirements_isstabilityneeded/2.
#show attributevalue_type_userrequirements_issensibletoelectricsmog/2.
#show attributevalue_type_userrequirements_installation/2.
#show type_room/1.
#show attributevalue_type_room_roomtype/2.
#show assoc_type_room_and_type_smarthome/2.
#show type_homeappliance/1.
#show attributevalue_type_homeappliance_homeappliancetype/2.
#show attributevalue_type_homeappliance_isalwayson/2.
#show attributevalue_type_homeappliance_isdangerous/2.
#show assoc_type_homeappliance_and_type_room/2.

