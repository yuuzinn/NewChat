package project.newchat.chatmsg.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsgDto {
  private Long chatMsgId;
  private Long userId;
  private String nickname;
  private String message;
  private LocalDateTime sendTime;
}
