package com.example.kafka_streams_examples.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.example.kafka_streams_examples.util.Delays;

public class DelaysSerdes implements Serde<Delays> {

	@Override
	public Serializer<Delays> serializer() {
		return new DelaysSerializerDeserializer();
	}

	@Override
	public Deserializer<Delays> deserializer() {
		return new DelaysSerializerDeserializer();
	}

}
