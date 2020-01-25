package com.example.kafka_streams_examples.util;

public class FlightFields {

	public Double isDelayed;
	public Double isCancelled;
	public Double isDiverted;

	public FlightFields() {
	}

	public FlightFields(Double isDelayed, Double isCancelled, Double isDiverted) {
		this.isDelayed = isDelayed;
		this.isCancelled = isCancelled;
		this.isDiverted = isDiverted;
	}
	
	
	
	
	
}
