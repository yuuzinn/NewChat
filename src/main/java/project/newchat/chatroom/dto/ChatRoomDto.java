package project.newchat.chatroom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.newchat.chatroom.domain.ChatRoom;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {
  private Long id;

  private String title;

  private Long currentUserCount;

  private Integer userCountMax;

  public static ChatRoomDto of(ChatRoom chatRoom) {
    return ChatRoomDto.builder()
        .id(chatRoom.getId())
        .title(chatRoom.getTitle())
        .currentUserCount((long) chatRoom.getUserChatRooms().size())
        .userCountMax(chatRoom.getUserCountMax())
        .build();
  }
}
