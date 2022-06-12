package com.kat.cloudstream.streamprocessor.service;

import com.kat.cloudstream.streamprocessor.config.TopicsProperties;
import com.kat.cloudstream.streamprocessor.model.PosInvoice;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.kat.cloudstream.streamprocessor.TestHelper.generatePosInvoice;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@Slf4j
public class ShipmentProcessorServiceIntegrationTest {

    @Autowired
    private TopicsProperties topicsProperties;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<String, PosInvoice> consumer;
    private Producer<String, PosInvoice> producer;

    @BeforeEach
    public void init() {
        consumer = configureConsumer();
        consumer.subscribe(Collections.singleton(topicsProperties.getUkShipmentTopic()));
        producer = configureProducer();
    }

    @DisplayName("Test posInvoice with delivery type 'delivery' and country == 'uk' is sent to uk_shipment_test_topic")
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void posInvoiceWithUkDeliveryAddress_endsToUkShipmentTopic() {

        // Given
        Producer<String, PosInvoice> producer = configureProducer();
        PosInvoice posInvoiceForUk = generatePosInvoice("delivery", "standard", "uk");
        PosInvoice posInvoiceForFr = generatePosInvoice("delivery", "standard", "fr");
        PosInvoice posInvoiceForAt = generatePosInvoice("delivery", "standard", "at");

        // When
        producer.send(new ProducerRecord<>(topicsProperties.getPosTopic(), posInvoiceForFr.getStoreId(), posInvoiceForUk));
        producer.send(new ProducerRecord<>(topicsProperties.getPosTopic(), posInvoiceForUk.getStoreId(), posInvoiceForFr));
        producer.send(new ProducerRecord<>(topicsProperties.getPosTopic(), posInvoiceForAt.getStoreId(), posInvoiceForAt));

        // Then
        ConsumerRecords<String, PosInvoice> records = KafkaTestUtils.getRecords(consumer);
        assertThat(records).isNotNull();
        assertThat(records).isNotEmpty();
        assertThat(records).hasSize(1);
        assertThat(records.iterator().next().value()).isEqualTo(posInvoiceForUk);
    }

    private Producer<String, PosInvoice> configureProducer() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        return new DefaultKafkaProducerFactory<String, PosInvoice>(producerProps, StringSerializer::new, JsonSerializer::new).createProducer();
    }

    private Consumer<String, PosInvoice> configureConsumer() {
        Map<String, Object> consumerProps = new HashMap<>(KafkaTestUtils.consumerProps("shipment-test-group", "false",
                embeddedKafkaBroker));
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        JsonDeserializer jsonDeserializer = new JsonDeserializer<>();
        jsonDeserializer.addTrustedPackages("*");
        Consumer<String, PosInvoice> consumer = new DefaultKafkaConsumerFactory<String, PosInvoice>(consumerProps,
                StringDeserializer::new, () -> jsonDeserializer)
                .createConsumer();
        return consumer;
    }
}
