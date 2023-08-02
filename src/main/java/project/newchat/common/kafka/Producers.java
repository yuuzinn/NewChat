package project.newchat.common.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Producers {
  private final Logger logger = LoggerFactory.getLogger(Producers.class);
  private final KafkaTemplate<String, String> kafkaTemplate;

  public void produceMessage(String topic, String payload) {
    logger.info("Topic : '{}' to Payload : '{}'", topic, payload);
    kafkaTemplate.send(topic, payload);
  }
}
