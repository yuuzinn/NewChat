package project.newchat.chatroom.controller.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {

  @NotEmpty
  @Length(min = 2, max = 20)
  private String title;

  @NotNull
  @Max(8)
  private Integer userCountMax;


}
