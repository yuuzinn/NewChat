package project.newchat.userchatroom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.user.domain.User;

import javax.persistence.*;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChatRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_chat_room_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chat_room_id")
  private ChatRoom chatRoom;
}
