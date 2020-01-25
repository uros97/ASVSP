package com.example.kafka_streams_examples.util;

public class DelayTypeFields {

	public Double isDelayed15Mins;
	public Double carrierDelay;
	public Double weatherDelay;
	public Double nasDelay;
	public Double securityDelay;
	public Double lateAircraftDelay;
	
	public DelayTypeFields() {

	}

	public DelayTypeFields(Double isDelayed15Mins, Double carrierDelay, Double weatherDelay, Double nasDelay,
			Double securityDelay, Double lateAircraftDelay) {
		this.isDelayed15Mins = isDelayed15Mins;
		this.carrierDelay = carrierDelay;
		this.weatherDelay = weatherDelay;
		this.nasDelay = nasDelay;
		this.securityDelay = securityDelay;
		this.lateAircraftDelay = lateAircraftDelay;
	}
	
	
	
	
}
