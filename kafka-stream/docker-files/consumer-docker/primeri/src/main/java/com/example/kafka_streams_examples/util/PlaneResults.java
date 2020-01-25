package com.example.kafka_streams_examples.util;

public class PlaneResults {

	public String manufacturer;
	public String model;
	public Long numberOfFlights;
	public Double delayed;
	public Double cancelled;
	public Double diverted;

	
	public PlaneResults() {

	}

	public PlaneResults(Long numberOfFlights, Double delayed, Double cancelled, Double diverted, String model,
			String manufacturer) {
		this.numberOfFlights = numberOfFlights;
		this.delayed = delayed;
		this.cancelled = cancelled;
		this.diverted = diverted;
		this.model = model;
		this.manufacturer = manufacturer;
	}

	@Override
	public String toString() {
		return "Delay percentage by plane model [manufacturer=" + manufacturer + ", model=" + model + ", numberOfFlights="
				+ numberOfFlights + ", delayed=" + delayed + ", cancelled=" + cancelled + ", diverted=" + diverted
				+ "]";
	}


	
	
	
	
	
	
	
}
