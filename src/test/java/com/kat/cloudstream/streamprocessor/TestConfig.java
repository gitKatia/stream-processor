package com.kat.cloudstream.streamprocessor;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    public static final String POS_TEST_TOPIC = "pos_test_topic";

    @Bean
    public NewTopic personTestTopic() {
        return new NewTopic(POS_TEST_TOPIC, 1, (short)1);
    }
}
