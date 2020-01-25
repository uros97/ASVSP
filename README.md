# ASVSP
Projekat iz ASVSP

Batch obrada - Pokrenuti sve kontejnere sa docker-compose up --build. Potrebno je test.csv i test2.csv staviti na HDFS u namenode kontejner na putanju test/test.csv. i flights.py skriptu je potrebno prebaciti u spark-master kontejner, a zatim je submit-ovati na spark pomocu komande spark/bin/spark-submit flights.py

Stream obrada - samo pokrenuti sve kontejnere pomocu docker-compose up --build i mogu se naknadno pokretati procesi u okviru consumer i producer kontejnera radi vrsenja razlicitih obrada (primer na consumeru: java -cp /usr/consumers/kafka-streams-examples-0.0.1-SNAPSHOT.jar com.example.kafka_streams_projekat.PercentageByHour)
