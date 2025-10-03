package com.salenhard.books.service.impl;

import com.salenhard.books.entity.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, Notification> kafkaTemplate;
    private static final String KAFKA_TOPIC_NAME = "my_topic";

    public void sendMessage(Notification notification) {
        log.info("Sending notification - BookID:{}", notification.getBookId());
        kafkaTemplate.send(KAFKA_TOPIC_NAME, notification);
        log.info("Notification successfully sent - BookID:{}", notification.getBookId());
    }
}
