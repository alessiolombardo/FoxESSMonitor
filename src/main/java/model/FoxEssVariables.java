package model;

public enum FoxEssVariables {

	todayYield, // ............. Today’s power generation [kW]
	pvPower, // ................ PVPower [kW]
	pv1Volt, // ................ PV1Volt [V]
	pv1Current, // ............. PV1Current [A]
	pv1Power, // ............... PV1Power [kW]
	pv2Volt, // ................ PV2Volt [V]
	pv2Current, // ............. PV2Current [A]
	pv2Power, // ............... PV2Power [kW]
	pv3Volt, // ................ PV3Volt [V]
	pv3Current, // ............. PV3Current [A]
	pv3Power, // ............... PV3Power [kW]
	pv4Volt, // ................ PV3Volt [V]
	pv4Current, // ............. PV3Current [A]
	pv4Power, // ............... PV3Power [kW]
	epsPower, // ............... EPSPower [kW]
	epsCurrentR, // ............ EPS-RCurrent [A]
	epsVoltR, // ............... EPS-RVolt [V]
	epsPowerR, // .............. EPS-RPower [kW]
	epsCurrentS, // ............ EPS-SCurrent [A]
	epsVoltS, // ............... EPS-SVolt [V]
	epsPowerS, // .............. EPS-SPower [kW]
	epsCurrentT, // ............ EPS-TCurrent [A]
	epsVoltT, // ............... EPS-TVolt [V]
	epsPowerT, // .............. EPS-TPower [kW]
	RCurrent, // ............... RCurrent [A]
	RVolt, // .................. RVolt [V]
	RFreq, // .................. RFreq [Hz]
	RPower, // ................. RPower [kW]
	SCurrent, // ............... SCurrent [A]
	SVolt, // .................. SVolt [V]
	SFreq, // .................. SFreq [Hz]
	SPower, // ................. SPower [kW]
	TCurrent, // ............... TCurrent [A]
	TVolt, // .................. TVolt [V]
	TFreq, // .................. TFreq [Hz]
	TPower, // ................. TPower [kW]
	ambientTemperation, // ..... Ambient Temperature [°C]
	boostTemperation, // ....... Boost Temperature [°C]
	invTemperation, // ......... Inverter Temperature [°C]
	chargeTemperature, // ...... Charge Temperature [°C]
	batTemperature, // ......... Battery Temperature [°C]
	dspTemperature, // ......... DSP Temperature [°C]
	loadsPower, // ............. Load Power [kW]
	loadsPowerR, // ............ LoadsRPower [kW]
	loadsPowerS, // ............ LoadsSPower [kW]
	loadsPowerT, // ............ LoadsTPower [kW]
	generationPower, // ........ GenerationPower [kW]
	feedinPower, // ............ Feed-in Power [kW]
	gridConsumptionPower, // ... GridConsumption Power [kW]
	invBatVolt, // ............. Inverter Battery Volt [V]
	invBatCurrent, // .......... Inverter Battery Current [A]
	invBatPower, // ............ Inverter Battery Power [kW]
	batChargePower, // ......... Battery Charge Power [kW]
	batDischargePower, // ...... Battery Discharge Power [kW]
	batVolt, // ................ Battery Volt [V]
	batCurrent, // ............. Battery Current [A]
	meterPower, // ............. Meter Power [kW]
	meterPower2, // ............ Meter2 Power [kW]
	meterPowerR, // ............ MeterR Power [kW]
	meterPowerS, // ............ MeterS Power [kW]
	meterPowerT, // ............ MeterT Power [kW]
	SoC, // .................... State of Charge [%]
	ReactivePower, // .......... Reactive Power [kVar]
	PowerFactor, // ............ Power Factor
	generation, // ............. Cumulative power generation [kWh]
	ResidualEnergy, // ......... Battery Residual Energy [10Wh]
	runningState, // ........... Running State
					// 160: self-test
					// 161: waiting
					// 162: checking
					// 163: on-grid
					// 164: off-grid
					// 165: fault
					// 166: permanent-fault
					// 167: standby
					// 168: upgrading
					// 169: fct
					// 170: illegal
	// batStatus, // .............. Battery Status
	// batStatusV2, // ............ Battery Status Name
	// currentFault, // ........... The current error code is reported
	// currentFaultCount // ....... The number of errors

}
