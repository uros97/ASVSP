package com.example.kafka_streams_examples.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.example.kafka_streams_examples.util.PlaneDelays;

public class PlaneDelaysSerdes implements Serde<PlaneDelays> {

	@Override
	public Serializer<PlaneDelays> serializer() {
		return new PlaneDelaysSerializerDeserializer();
	}

	@Override
	public Deserializer<PlaneDelays> deserializer() {
		return new PlaneDelaysSerializerDeserializer();
	}

}
