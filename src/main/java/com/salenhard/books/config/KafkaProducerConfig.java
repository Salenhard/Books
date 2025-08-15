package com.salenhard.books.config;

import com.salenhard.books.entity.DTO.BookWithAuthorDto;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, BookWithAuthorDto> producerFactory() {
        Map<String, Object> props = new HashMap<>();

        JsonSerializer<BookWithAuthorDto> serializer = new JsonSerializer<>();
        serializer.setAddTypeInfo(false);
        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        return new DefaultKafkaProducerFactory<>(props,
                new StringSerializer(),
                serializer);
    }

    @Bean
    public KafkaTemplate<String, BookWithAuthorDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}