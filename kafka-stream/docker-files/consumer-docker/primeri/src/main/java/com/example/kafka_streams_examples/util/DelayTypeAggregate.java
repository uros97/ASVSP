package com.example.kafka_streams_examples.util;

public class DelayTypeAggregate {

	public Double numberOfDelayed;
	public Double sumCarrierDelay;
	public Double sumWeatherDelay;
	public Double sumNasDelay;
	public Double sumSecurityDelay;
	public Double sumLateAircraftDelay;
	
	public DelayTypeAggregate() {
		this.numberOfDelayed = 0.00;
		this.sumCarrierDelay = 0.00;
		this.sumWeatherDelay = 0.00;
		this.sumNasDelay = 0.00;
		this.sumSecurityDelay = 0.00;
		this.sumLateAircraftDelay = 0.00;
		
	}

	public DelayTypeAggregate(Double numberOfDelayed, Double sumCarrierDelay, Double sumWeatherDelay,
			Double sumNasDelay, Double sumSecurityDelay, Double sumLateAircraftDelay) {
		this.numberOfDelayed = numberOfDelayed;
		this.sumCarrierDelay = sumCarrierDelay;
		this.sumWeatherDelay = sumWeatherDelay;
		this.sumNasDelay = sumNasDelay;
		this.sumSecurityDelay = sumSecurityDelay;
		this.sumLateAircraftDelay = sumLateAircraftDelay;
	}
	
	
	
	
	
}
