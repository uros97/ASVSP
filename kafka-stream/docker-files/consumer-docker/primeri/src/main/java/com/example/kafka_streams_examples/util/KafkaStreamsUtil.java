package com.example.kafka_streams_examples.util;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;

public class KafkaStreamsUtil {

	public static Properties getStreamsConfiguration(String exampleName) {
		final Properties streamsConfiguration = new Properties();
		// Give the Streams application a unique name. The name must be unique in the
		// Kafka cluster against which the application is run.
		streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, KafkaConstants.TOPIC_NAME + "-streams-projekat-" + exampleName);
		streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, KafkaConstants.TOPIC_NAME + "-streams-projekat-client-" + exampleName);
		// Where to find Kafka broker(s).
		streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKERS);
		// Specify default (de)serializers for record keys and for record values.
		streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
		streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		//streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		//streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, new FlightFieldsSerdes().getClass().getName());
		streamsConfiguration.put("default.deserialization.exception.handler", LogAndContinueExceptionHandler.class);
		
		return streamsConfiguration;
	}
}
