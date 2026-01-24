package com.example.demo.infra.messaging.transfer;

import com.example.demo.application.port.out.event.EventPublisher;
import com.example.demo.domain.event.TransferEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransferEventKafkaPublisher implements EventPublisher<TransferEvent> {

  @Autowired
  @Qualifier("transferKafkaTemplate")
  private KafkaTemplate<String, TransferEvent> kafkaTemplate;

  @Value("${app.kafka.topics.transfer-events.name}")
  private String topic;

  @Override
  public void publish(TransferEvent event) {
    kafkaTemplate.send(topic, event.getKey().key(), event);
  }

}
