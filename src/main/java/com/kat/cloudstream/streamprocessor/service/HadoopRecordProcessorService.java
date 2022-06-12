package com.kat.cloudstream.streamprocessor.service;

import com.kat.cloudstream.streamprocessor.model.HadoopRecord;
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
public class HadoopRecordProcessorService {
    private final RecordBuilderService recordBuilderService;

    @Bean
    public Function<KStream<String , PosInvoice>, KStream<String , HadoopRecord>> hadoopRecordProcessor() {
        return kStream -> kStream
                .mapValues(recordBuilderService::getMaskedInvoice)
                .flatMapValues(recordBuilderService::getHadoopRecords)
                .peek((k, v) -> log.info("k: {}, v: {} for Hadoop Record Processor", k, v));
    }
}
