package com.example.kafka_streams_examples.serdes;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serializer;

import com.example.kafka_streams_examples.util.Delays;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DelaysSerializerDeserializer implements Serializer<Delays>, Deserializer<Delays> {

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	public DelaysSerializerDeserializer() {

	}
	@Override
	public Delays deserialize(String topic, byte[] bytes) {
		if (bytes == null)
			return null;

		Delays data;
		try {
			data = objectMapper.readValue(bytes, Delays.class);
		} catch (Exception e) {
			throw new SerializationException(e);
		}

		return data;
	}

	@Override
	public byte[] serialize(String topic, Delays data) {
		if (data == null)
			return null;

		try {
			return objectMapper.writeValueAsBytes(data);
		} catch (Exception e) {
			throw new SerializationException("Error serializing JSON message", e);
		}
	}

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// TODO Auto-generated method stub
		//Deserializer.super.configure(configs, isKey);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		//Deserializer.super.close();
	}

}
