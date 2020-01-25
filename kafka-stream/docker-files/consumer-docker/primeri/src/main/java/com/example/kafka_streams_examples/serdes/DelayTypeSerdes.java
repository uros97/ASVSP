package com.example.kafka_streams_examples.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.example.kafka_streams_examples.util.DelayTypeFields;

public class DelayTypeSerdes implements Serde<DelayTypeFields> {

	@Override
	public Serializer<DelayTypeFields> serializer() {
		return new DelayTypeSerializerDeserializer();
	}

	@Override
	public Deserializer<DelayTypeFields> deserializer() {
		return new DelayTypeSerializerDeserializer();
	}

}
