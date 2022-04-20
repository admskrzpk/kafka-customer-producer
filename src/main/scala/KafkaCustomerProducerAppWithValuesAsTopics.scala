
object KafkaCustomerProducerAppWithValuesAsTopics extends App {

  import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
  import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

  import java.time.Duration
  import java.util.Properties
  import scala.jdk.CollectionConverters._

  val propsProducer = new Properties()
  val propsConsumer = new Properties()

  propsProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  propsProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  propsProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  propsConsumer.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  propsConsumer.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  propsConsumer.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer")
  propsConsumer.put(ConsumerConfig.GROUP_ID_CONFIG, "test")

  val producer = new KafkaProducer[String, String](propsProducer)
  val consumer = new KafkaConsumer[String, String](propsConsumer)
  consumer.subscribe(Seq("initial").asJava)

  while (true) {
    val records = consumer.poll(Duration.ofMillis(100))
      .asScala
      .foreach(record => {
        producer.send(new ProducerRecord[String, String](s"${record.value}", s"${record.value.toUpperCase}"))
      })
  }
  consumer.close()
  producer.close()
}