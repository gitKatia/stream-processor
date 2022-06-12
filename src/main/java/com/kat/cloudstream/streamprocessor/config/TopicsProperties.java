package com.kat.cloudstream.streamprocessor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = TopicsProperties.TOPICS_PREFIX)
@Data
public class TopicsProperties {
    static final String TOPICS_PREFIX = "stream-processor";
    private String posTopic;
    private String ukShipmentTopic;
}
