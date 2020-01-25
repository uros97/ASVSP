package com.example.kafka_streams_examples.util;

public class Delays {

	public Long numberOfFlights;
	public Double delayedMoreThan15Min;
	public Double cancelled;
	public Double diverted;
	
	public Delays() {
		numberOfFlights = 0L;
		delayedMoreThan15Min = 0.00;
		cancelled = 0.00;
		diverted = 0.00;
	}

	public Delays(Long numberOfFlights, Double delayedMoreThan15Min, Double cancelled, Double diverted) {
		this.numberOfFlights = numberOfFlights;
		this.delayedMoreThan15Min = delayedMoreThan15Min;
		this.cancelled = cancelled;
		this.diverted = diverted;
	}

	
	
	
	
	
}
