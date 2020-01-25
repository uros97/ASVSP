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

import com.example.kafka_streams_examples.serdes.DelayTypeAggregateSerdes;
import com.example.kafka_streams_examples.serdes.DelayTypeSerdes;
import com.example.kafka_streams_examples.util.DelayTypeAggregate;
import com.example.kafka_streams_examples.util.DelayTypeFields;
import com.example.kafka_streams_examples.util.DelayTypeResults;
import com.example.kafka_streams_examples.util.KafkaConstants;
import com.example.kafka_streams_examples.util.KafkaStreamsUtil;

public class DelayTypeAverage {

	public static void main(String[] args) {
		final Properties streamsConfiguration = KafkaStreamsUtil.getStreamsConfiguration("delay-type-average");

		// In the subsequent lines we define the processing topology of the Streams
		// application.
		final StreamsBuilder builder = new StreamsBuilder();

		// Read the input Kafka topic into a KStream instance.
		final KStream<Long, String> flights = builder.stream(KafkaConstants.TOPIC_NAME);

		flights.map((Long key, String value) -> {
			
			String[] words = value.split(KafkaConstants.SEPARATOR);
			return new KeyValue<>(words[2], new DelayTypeFields(parseDouble(words[44]),
																parseDouble(words[56]),
																parseDouble(words[57]),
																parseDouble(words[58]),
																parseDouble(words[59]),
																parseDouble(words[60])));

		})
		.groupByKey(Grouped.with(Serdes.String(), new DelayTypeSerdes()))
		.aggregate(
				// Parameter 1: The Initializer interface for creating an initial value in aggregations.
				() -> {return new DelayTypeAggregate();},
				
				// Parameter 2: The Aggregator interface for aggregating values of the given key.
				(final String key, final DelayTypeFields value, final DelayTypeAggregate aggregate) -> {
					aggregate.numberOfDelayed += value.isDelayed15Mins;
					if(value.isDelayed15Mins != 0.0) {
						aggregate.sumCarrierDelay += value.carrierDelay;
						aggregate.sumWeatherDelay += value.weatherDelay; 
						aggregate.sumNasDelay += value.nasDelay;
						aggregate.sumSecurityDelay += value.securityDelay;
						aggregate.sumLateAircraftDelay += value.lateAircraftDelay;
					}

					return aggregate;
				},
				// Parameter 3: Used to describe how a StateStore should be materialized.
				Materialized.with(Serdes.String(), new DelayTypeAggregateSerdes()))
		.mapValues((DelayTypeAggregate value) -> {
					DelayTypeResults results = new DelayTypeResults();
					results.numberOfDelayed = Math.round(value.numberOfDelayed);
					results.carrierDelay = value.sumCarrierDelay / value.numberOfDelayed;
					results.weatherDelay = value.sumWeatherDelay / value.numberOfDelayed;
					results.nasDelay = value.sumNasDelay / value.numberOfDelayed;
					results.securityDelay = value.sumSecurityDelay / value.numberOfDelayed;
					results.lateAircraftDelay = value.sumLateAircraftDelay / value.numberOfDelayed;
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
