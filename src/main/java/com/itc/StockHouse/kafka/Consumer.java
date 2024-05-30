package com.itc.StockHouse.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itc.StockHouse.kafka.dto.KafkaEvent;
import com.itc.StockHouse.kafka.handlers.KafkaEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.kafka.enabled", matchIfMissing = false)
public class Consumer {

    private final Map<String, KafkaEventHandler> handlers;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "${app.kafka.topic-name}", containerFactory = "kafkaListenerContainerFactoryByteArray")
    public void listenGroupTopic(byte[] message) {
        log.info("Received message %s".formatted(Arrays.toString(message)));
        try {
            final KafkaEvent event = objectMapper.readValue(message, KafkaEvent.class);
            if (!handlers.containsKey(event.getEvent())) {
                throw new RuntimeException("No event handler for %s".formatted(event.getEvent()));
            }
            KafkaEventHandler eventHandler = handlers.get(event.getEvent());
            eventHandler.handle(event);
        } catch (Exception ex) {
            log.warn("Could not process event: %s".formatted(ex.getMessage()));
        }
    }
}
