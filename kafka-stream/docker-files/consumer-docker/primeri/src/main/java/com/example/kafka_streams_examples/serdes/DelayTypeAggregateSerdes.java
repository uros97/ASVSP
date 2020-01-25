package com.example.kafka_streams_examples.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.example.kafka_streams_examples.util.DelayTypeAggregate;

public class DelayTypeAggregateSerdes implements Serde<DelayTypeAggregate> {

	@Override
	public Serializer<DelayTypeAggregate> serializer() {
		return new DelayTypeAggregateSerializerDeserializer();
	}

	@Override
	public Deserializer<DelayTypeAggregate> deserializer() {
		return new DelayTypeAggregateSerializerDeserializer();
	}

}
