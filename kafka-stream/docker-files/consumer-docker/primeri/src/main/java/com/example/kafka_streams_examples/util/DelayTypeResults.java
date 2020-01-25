package com.example.kafka_streams_examples.util;

public class DelayTypeResults {
	
	public Long numberOfDelayed;
	public Double carrierDelay;
	public Double weatherDelay;
	public Double nasDelay;
	public Double securityDelay;
	public Double lateAircraftDelay;
	
	
	public DelayTypeResults() {
	}


	public DelayTypeResults(Long numberOfDelayed, Double carrierDelay, Double weatherDelay, Double nasDelay,
			Double securityDelay, Double lateAircraftDelay) {
		this.numberOfDelayed = numberOfDelayed;
		this.carrierDelay = carrierDelay;
		this.weatherDelay = weatherDelay;
		this.nasDelay = nasDelay;
		this.securityDelay = securityDelay;
		this.lateAircraftDelay = lateAircraftDelay;
	}


	@Override
	public String toString() {
		return "Average delay time by delay type [numberOfDelayed=" + numberOfDelayed + ", carrierDelay=" + carrierDelay
				+ ", weatherDelay=" + weatherDelay + ", nasDelay=" + nasDelay + ", securityDelay=" + securityDelay
				+ ", lateAircraftDelay=" + lateAircraftDelay + "]";
	}
	
	
	
	
	
	

}
