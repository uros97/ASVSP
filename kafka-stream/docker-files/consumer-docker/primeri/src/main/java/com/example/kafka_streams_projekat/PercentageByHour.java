package com.example.kafka_streams_projekat;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;

import com.example.kafka_streams_examples.serdes.DelaysSerdes;
import com.example.kafka_streams_examples.serdes.FlightFieldsSerdes;
import com.example.kafka_streams_examples.util.Delays;
import com.example.kafka_streams_examples.util.FlightFields;
import com.example.kafka_streams_examples.util.KafkaConstants;
import com.example.kafka_streams_examples.util.KafkaStreamsUtil;
import com.example.kafka_streams_examples.util.PercentageResults;

public class PercentageByHour {

	public static void main(String[] args) {
		final Properties streamsConfiguration = KafkaStreamsUtil.getStreamsConfiguration("percentage-by-hour");

		// In the subsequent lines we define the processing topology of the Streams
		// application.
		final StreamsBuilder builder = new StreamsBuilder();

		// Read the input Kafka topic into a KStream instance.
		final KStream<Long, String> flights = builder.stream(KafkaConstants.TOPIC_NAME);

		flights.map((Long key, String value) -> {
			
			String[] words = value.split(KafkaConstants.SEPARATOR);
			return new KeyValue<>(words[35], new FlightFields(parseDouble(words[44]),
					parseDouble(words[47]), parseDouble(words[49])));

		})
		.groupByKey(Grouped.with(Serdes.String(), new FlightFieldsSerdes()))
		.aggregate(
				// Parameter 1: The Initializer interface for creating an initial value in aggregations.
				() -> {return new Delays();},
				
				// Parameter 2: The Aggregator interface for aggregating values of the given key.
				(final String key, final FlightFields value, final Delays aggregate) -> {
					++aggregate.numberOfFlights;
					aggregate.delayedMoreThan15Min += value.isDelayed;
					aggregate.cancelled += value.isCancelled;
					aggregate.diverted += value.isDiverted;
					return aggregate;
				},
				// Parameter 3: Used to describe how a StateStore should be materialized.
				Materialized.with(Serdes.String(), new DelaysSerdes()))
		.mapValues((Delays value) -> {
					PercentageResults results = new PercentageResults();
					results.numberOfFlights = value.numberOfFlights;
					results.delayed = value.delayedMoreThan15Min * 100 / (double) value.numberOfFlights;
					results.cancelled = value.cancelled * 100 / (double) value.numberOfFlights;
					results.diverted = value.diverted * 100 / (double) value.numberOfFlights;
					return results.toString();
				})
		.toStream().print(Printed.toSysOut());
		// .to(KafkaConstants.TOPIC_NAME + "-hourly-percentage",
		// Produced.with(Serdes.String(), Serdes.Double()));

		@SuppressWarnings("resource")
		final KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfiguration);
		streams.cleanUp();
		streams.start();

		// Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	}
	private static double parseDouble(String s){
	    if(s == null || s.isEmpty()) 
	        return 0.0;
	    else
	        return Double.parseDouble(s);
	}

}
