package com.example.kafka_streams_examples.serdes;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.example.kafka_streams_examples.util.FlightFields;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FlightFieldsSerializerDeserializer implements Deserializer<FlightFields>, Serializer<FlightFields> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public byte[] serialize(String topic, FlightFields data) {
		if (data == null)
			return null;

		try {
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SerializationException("Error serializing JSON message", e);
		}
	}

	@Override
	public FlightFields deserialize(String topic, byte[] bytes) {
		if (bytes == null)
			return null;

		FlightFields data;
		try {
			data = objectMapper.readValue(bytes, FlightFields.class);
		} catch (Exception e) {
			throw new SerializationException(e);
		}

		return data;
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		Deserializer.super.configure(configs, isKey);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		Deserializer.super.close();
	}

}
