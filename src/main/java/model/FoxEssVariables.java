package model;

public enum FoxEssVariables {

	loadsPower, // ............. AC load power [kW]
	loads, // .................. AC load energy
	generationPower, // ........ Generation power [kW]
	generation, // ............. Generation
	feedinPower, // ............ Feed in power [kW]
	feedin, // ................. Feed in grid energy(1)
	feedin2, // ................ Feed in grid energy(2)
	input, // .................. PV input energy
	gridConsumptionPower, // ... Grid consumption power [kW]
	gridConsumption, // ........ Grid consumption energy(1)
	gridConsumption2, // ....... Grid consumption energy(2)
	RVolt, // .................. R phase voltage [V]
	RCurrent, // ............... R phase current [A]
	RFreq, // .................. R phase frequency [Hz]
	RPower, // ................. R phase power [kW]
	TVolt, // .................. T phase voltage [V]
	TCurrent, // ............... T phase current [A]
	TFreq, // .................. T phase frequency [Hz]
	TPower, // ................. T phase power [kW]
	SVolt, // .................. S phase voltage [V]
	SCurrent, // ............... S phase current [A]
	SFreq, // .................. S phase frequency [Hz]
	SPower, // ................. S phase power [kW]
	pvPower, // ................ PV total power [kW]
	pv1Volt, // ................ PV1 voltage [V]
	pv1Current, // ............. PV1 current [A]
	pv1Power, // ............... PV1 power [kW]
	pv2Volt, // ................ PV2 voltage [V]
	pv2Current, // ............. PV2 current [A]
	pv2Power, // ............... PV2 power [kW]
	pv3Volt, // ................ PV3 voltage [V]
	pv3Current, // ............. PV3 current [A]
	pv3Power, // ............... PV3 power [kW]
	pv4Volt, // ................ PV4 voltage [V]
	pv4Current, // ............. PV4 current [A]
	pv4Power, // ............... PV4 power [kW]
	epsVoltR, // ............... EPS R phase voltage [V]
	epsCurrentR, // ............ EPS R phase current [A]
	epsPowerR, // .............. EPS R phase power [kW]
	epsVoltS, // ............... EPS S phase voltage [V]
	epsCurrentS, // ............ EPS S phase current [A]
	epsPowerS, // .............. EPS S phase power [kW]
	epsVoltT, // ............... EPS T phase voltage [V]
	epsCurrentT, // ............ EPS T phase current [A]
	epsPowerT, // .............. EPS T phase power [kW]
	todayYield, // ............. Today yield
	fault1, // ................. Fault 1 (Refer to fault code table)
	fault2, // ................. Fault 2 (Refer to fault code table)
	fault3, // ................. Fault 3 (Refer to fault code table)
	fault4, // ................. Fault 4 (Refer to fault code table)
	fault5, // ................. Fault 5 (Refer to fault code table)
	fault6, // ................. Fault 6 (Refer to fault code table)
	fault7, // ................. Fault 7 (Refer to fault code table)
	fault8, // ................. Fault 8 (Refer to fault code table)
	masterState, // ............ Master state:
					// ........... 0: wait, 1: check, 2: on-grid, 3: fault, 4: permanent fault,
					// ........... 5: off-grid, 6: Idle, 7: self-checking, 8: upgrade
	batChargePower, // ......... Battery charge power [kW]
	batDischargePower, // ...... Battery discharge power [kW]
	meterPower, // ............. Meter (1) power [kW]
	meterPower2, // ............ Meter (2) power [kW]
	meterStatus, // ............ Meter (1) status
	meter2Status, // ........... Meter (2) status
	chargeTemperature, // ...... Battery charger temperature [°C]
	SoC, // .................... Battery SOC [%]
	batTemperature, // ......... Battery temperature [°C]
	batStatus, // .............. Battery status
	epsFrequency, // ........... EPS off-grid frequency
	batVolt, // ................ Battery voltage [V]
	dspTemperature, // ......... DSP kernel temperature [°C]
	operatingMode // ........... Work mode: (0: seltuse, 1: feedin, 2: backup)

}
