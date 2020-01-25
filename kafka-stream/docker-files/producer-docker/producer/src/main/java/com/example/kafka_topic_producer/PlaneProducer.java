package com.example.kafka_topic_producer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class PlaneProducer {

	public static void main(String[] args) {
        try (InputStream inputStream = new App().getClass().getResourceAsStream("/plane-data.csv");) {
        	readPlanesAndProduce(new InputStreamReader(inputStream), ProducerCreator.createPlaneProducer());
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
    static void readPlanesAndProduce(Reader reader, Producer<String, String> producer) {
        try (CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {        
                StringJoiner joiner = new StringJoiner("#");
                for (int i = 1; i < line.length; i++) {
                    joiner.add(line[i]);
                }
                ProducerRecord<String, String> record = new ProducerRecord<String, String>("planes-topic",
                        line[0], joiner.toString());
                try {
                    RecordMetadata metadata = producer.send(record).get();
                    System.out.println("Plane record sent with key " + line[0] + " to partition " + metadata.partition()
                            + " with offset " + metadata.offset());
                    Thread.sleep(1);
                } catch (ExecutionException e) {
                    System.out.println("Error in sending plane record");
                    System.out.println(e);
                } catch (InterruptedException e) {
                    System.out.println("Error in sending plane record");
                    System.out.println(e);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("I/O error occured");
        }
    }

}
