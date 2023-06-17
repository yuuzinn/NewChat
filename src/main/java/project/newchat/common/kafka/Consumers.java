package project.newchat.common.kafka;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import project.newchat.chatmsg.domain.ChatMsg;

@Component
@RequiredArgsConstructor
public class Consumers {
  private final Logger logger = LoggerFactory.getLogger(Consumers.class);
  @KafkaListener(topics = "${spring.kafka.template.default-topic}",
      groupId = "${spring.kafka.consumer.group-id}")
  public void consume(@Payload ChatMsg chatMsg) throws Exception {
    logger.info("Consume msg : roomId : '{}', nickname :'{}', sender : '{}' ",
        chatMsg.getId(), chatMsg.getUser().getNickname(), chatMsg.getMessage());
    Map<String, String> msg = new HashMap<>();
    msg.put("roomNum", String.valueOf(chatMsg.getChatRoom().getId()));
    msg.put("message", chatMsg.getMessage());
    msg.put("sender", chatMsg.getUser().getNickname());
  }
}
