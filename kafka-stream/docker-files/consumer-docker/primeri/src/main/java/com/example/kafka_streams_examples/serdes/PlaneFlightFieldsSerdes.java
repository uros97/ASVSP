package com.example.kafka_streams_examples.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.example.kafka_streams_examples.util.FlightPlaneFields;

public class PlaneFlightFieldsSerdes implements Serde<FlightPlaneFields> {

	@Override
	public Serializer<FlightPlaneFields> serializer() {
		return new PlaneFlightFieldsSerializerDeserializer();
	}

	@Override
	public Deserializer<FlightPlaneFields> deserializer() {
		return new PlaneFlightFieldsSerializerDeserializer();
	}

}
