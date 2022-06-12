package com.kat.cloudstream.streamprocessor.service;

import com.kat.cloudstream.streamprocessor.model.Notification;
import com.kat.cloudstream.streamprocessor.model.PosInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessorService {

    private final RecordBuilderService recordBuilder;

    @Bean
    public Function<KStream<String ,PosInvoice>, KStream<String , Notification>> notificationProcessor() {
        log.info("processing");
        return kStream -> kStream
                .filter((k, v) -> "prime".equals(v.getCustomerType()))
                .mapValues(recordBuilder::getNotification)
                .peek((k, v) -> log.info("k: {}, v: {} for Notification Processor", k, v));
    }
}
