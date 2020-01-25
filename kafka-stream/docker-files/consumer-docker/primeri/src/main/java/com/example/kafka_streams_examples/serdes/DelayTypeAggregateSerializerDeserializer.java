package com.example.kafka_streams_examples.serdes;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.example.kafka_streams_examples.util.DelayTypeAggregate;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DelayTypeAggregateSerializerDeserializer implements Deserializer<DelayTypeAggregate>, Serializer<DelayTypeAggregate> {

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public byte[] serialize(String topic, DelayTypeAggregate data) {
		if (data == null)
			return null;

		try {
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SerializationException("Error serializing JSON message", e);
		}
	}

	@Override
	public DelayTypeAggregate deserialize(String topic, byte[] bytes) {
		if (bytes == null)
			return null;

		DelayTypeAggregate data;
		try {
			data = objectMapper.readValue(bytes, DelayTypeAggregate.class);
		} catch (Exception e) {
			throw new SerializationException(e);
		}

		return data;
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@Override
	public void close() {

	}

}
