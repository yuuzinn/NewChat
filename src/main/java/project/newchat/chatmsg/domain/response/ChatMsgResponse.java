package project.newchat.chatmsg.domain.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMsgResponse {
  private Long roomId;
  private String from; // 누가? (사용자)
  private String message;
  private LocalDateTime sendTime;
}
