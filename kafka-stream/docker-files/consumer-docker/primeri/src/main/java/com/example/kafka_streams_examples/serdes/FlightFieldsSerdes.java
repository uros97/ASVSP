package com.example.kafka_streams_examples.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.example.kafka_streams_examples.util.FlightFields;

public class FlightFieldsSerdes implements Serde<FlightFields> {

	@Override
	public Serializer<FlightFields> serializer() {
		return new FlightFieldsSerializerDeserializer();
	}

	@Override
	public Deserializer<FlightFields> deserializer() {
		return new FlightFieldsSerializerDeserializer();
	}

}
