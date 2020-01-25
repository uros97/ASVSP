package com.example.kafka_streams_projekat;


import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;

import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;

import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.ValueJoiner;

import com.example.kafka_streams_examples.serdes.FlightFieldsSerdes;
import com.example.kafka_streams_examples.serdes.PlaneDelaysSerdes;
import com.example.kafka_streams_examples.serdes.PlaneFlightFieldsSerdes;

import com.example.kafka_streams_examples.util.FlightFields;
import com.example.kafka_streams_examples.util.FlightPlaneFields;
import com.example.kafka_streams_examples.util.KafkaConstants;
import com.example.kafka_streams_examples.util.KafkaStreamsUtil;
import com.example.kafka_streams_examples.util.PlaneDelays;
import com.example.kafka_streams_examples.util.PlaneResults;

public class PlaneModelPercentage {

	public static void main(String[] args) {
		final Properties streamsConfiguration = KafkaStreamsUtil.getStreamsConfiguration("percentage-by-plane");
		// In the subsequent lines we define the processing topology of the Streams
		// application.
		final StreamsBuilder builder = new StreamsBuilder();
		
		//Read planes topic into KTable
		final KStream<String, String> planes = builder.stream("planes-topic");

		// Read the input Kafka topic into a KStream instance.
		final KStream<Long, String> flights = builder.stream(KafkaConstants.TOPIC_NAME);

		


		flights.map((Long key, String value) -> {
			
			String[] words = value.split(KafkaConstants.SEPARATOR);
			KeyValue<String, FlightFields> retVal = new KeyValue<>(words[9], new FlightFields(parseDouble(words[44]),
					parseDouble(words[47]), parseDouble(words[49])));
			return retVal;

		})
		.join(planes, new ValueJoiner<FlightFields, String, FlightPlaneFields>() {

			
			@Override
			public FlightPlaneFields apply(FlightFields value1, String value2) {
				System.out.println("Usao sam u join!");
				String[] planeData = value2.split("#");
				String model = planeData[1];
				String manufacturer = planeData[2];
				Integer productionYear = parseInt(planeData[5]);
				FlightPlaneFields joinResults = new FlightPlaneFields(value1.isDelayed, value1.isCancelled, value1.isDiverted, model, manufacturer, productionYear);
				System.out.println(joinResults);
				return joinResults;
			}
			
		}, JoinWindows.of(Duration.ofMinutes(10000)), Joined.with(Serdes.String(), new FlightFieldsSerdes(), Serdes.String()))
		.groupBy((String key, FlightPlaneFields value) -> value.model, Grouped.with(Serdes.String(), new PlaneFlightFieldsSerdes()))
		.aggregate(
				// Parameter 1: The Initializer interface for creating an initial value in aggregations.
				() -> {return new PlaneDelays();},
				
				// Parameter 2: The Aggregator interface for aggregating values of the given key.
				(final String key, final FlightPlaneFields value, final PlaneDelays aggregate) -> {
					++aggregate.numberOfFlights;
					aggregate.delayedMoreThan15Min += value.isDelayed;
					aggregate.cancelled += value.isCancelled;
					aggregate.diverted += value.isDiverted;
					aggregate.manufacturer = value.manufacturer;
					aggregate.model = value.model;
					return aggregate;
				},
				// Parameter 3: Used to describe how a StateStore should be materialized.
				Materialized.with(Serdes.String(), new PlaneDelaysSerdes()))
		.mapValues((PlaneDelays value) -> {
					PlaneResults results = new PlaneResults();
					results.manufacturer = value.manufacturer;
					results.model = value.model;
					results.numberOfFlights = value.numberOfFlights;
					results.delayed = value.delayedMoreThan15Min * 100 / (double) value.numberOfFlights;
					results.cancelled = value.cancelled * 100 / (double) value.numberOfFlights;
					results.diverted = value.diverted * 100 / (double) value.numberOfFlights;
					return results.toString();
				})
		.toStream().print(Printed.toSysOut());
		// .to(KafkaConstants.TOPIC_NAME + "-monthly-percentage",
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
	private static int parseInt(String s) {
	    if(s == null || s.isEmpty()) 
	        return 0;
	    else
	        return Integer.parseInt(s);
	}
}
