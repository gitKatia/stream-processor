package com.kat.cloudstream.streamprocessor.service;

import com.kat.cloudstream.streamprocessor.model.PosInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ShipmentProcessorService {

    private static final Predicate<String, PosInvoice> UK_SHIPMENT_PREDICATE = (k, v) -> "uk".equalsIgnoreCase(v.getDeliveryAddress().getCountry());

    @Bean
    public Function<KStream<String , PosInvoice>, KStream<String, PosInvoice>> shipmentProcessor() {
        // We could also first apply some validation logic on the orders and then filtering (filter and filterNot)
        // Send invalid orders with key = Error Code and value = Record Value to an Error Topic
        return kStream -> kStream
                .filter((k, v) -> "delivery".equals(v.getDeliveryType()))
                .filter(UK_SHIPMENT_PREDICATE);
    }
}
