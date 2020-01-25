package com.example.kafka_streams_examples.util;

public class PlaneDelays {

	public String model;
	public String manufacturer;
	public Long numberOfFlights;
	public Double delayedMoreThan15Min;
	public Double cancelled;
	public Double diverted;
	
	public PlaneDelays() {

	}

	public PlaneDelays(String model, String manufacturer, Long numberOfFlights, Double delayedMoreThan15Min,
			Double cancelled, Double diverted) {
		this.model = model;
		this.manufacturer = manufacturer;
		this.numberOfFlights = numberOfFlights;
		this.delayedMoreThan15Min = delayedMoreThan15Min;
		this.cancelled = cancelled;
		this.diverted = diverted;
	}
	
	
	
	
}
