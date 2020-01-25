package com.example.kafka_streams_examples.util;

public class PercentageResults {

	public Long numberOfFlights;
	public Double delayed;
	public Double cancelled;
	public Double diverted;
	
	
	
	public PercentageResults() {
	}



	public PercentageResults(Long numberOfFlights, Double delayed, Double cancelled, Double diverted) {
		this.numberOfFlights = numberOfFlights;
		this.delayed = delayed;
		this.cancelled = cancelled;
		this.diverted = diverted;
	}



	@Override
	public String toString() {
		return " [Number of flights=" + numberOfFlights + ", delayed=" + delayed + ", cancelled="
				+ cancelled + ", diverted=" + diverted + "]";
	}
	
	
	
	
}
