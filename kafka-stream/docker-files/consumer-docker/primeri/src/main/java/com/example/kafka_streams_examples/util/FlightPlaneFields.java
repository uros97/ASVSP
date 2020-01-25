package com.example.kafka_streams_examples.util;

public class FlightPlaneFields {

	public Double isDelayed;
	public Double isCancelled;
	public Double isDiverted;
	public String model;
	public String manufacturer;
	public Integer productionYear;
	
	
	public FlightPlaneFields() {
	}


	public FlightPlaneFields(Double isDelayed, Double isCancelled, Double isDiverted, String model, String manufacturer,
			Integer productionYear) {
		this.isDelayed = isDelayed;
		this.isCancelled = isCancelled;
		this.isDiverted = isDiverted;
		this.model = model;
		this.manufacturer = manufacturer;
		this.productionYear = productionYear;
	}


	@Override
	public String toString() {
		return "FlightPlaneFields [isDelayed=" + isDelayed + ", isCancelled=" + isCancelled + ", isDiverted="
				+ isDiverted + ", model=" + model + ", manufacturer=" + manufacturer + ", productionYear="
				+ productionYear + "]";
	}
	
	
	
	
}
