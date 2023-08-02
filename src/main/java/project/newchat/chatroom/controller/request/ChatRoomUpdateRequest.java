package project.newchat.chatroom.controller.request;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomUpdateRequest {
  @NotEmpty
  @Length(min = 2, max = 20)
  private String title;
}
