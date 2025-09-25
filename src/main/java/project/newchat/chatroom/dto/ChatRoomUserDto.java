package project.newchat.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.newchat.userchatroom.domain.UserChatRoom;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomUserDto {
  private String nickname;
  private Boolean status;

  public static ChatRoomUserDto of(UserChatRoom userChatRoom) {
    return ChatRoomUserDto.builder()
            .nickname(userChatRoom.getUser().getNickName())
            .status(userChatRoom.getUser().getStatus())
            .build();
  }
}
