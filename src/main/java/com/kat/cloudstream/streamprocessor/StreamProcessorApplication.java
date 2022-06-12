package com.kat.cloudstream.streamprocessor;

import com.kat.cloudstream.streamprocessor.config.TopicsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TopicsProperties.class)
public class StreamProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamProcessorApplication.class, args);
	}

}
