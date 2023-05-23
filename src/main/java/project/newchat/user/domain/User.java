package project.newchat.user.domain;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.newchat.chatmsg.domain.ChatMsg;
import project.newchat.userchatroom.domain.UserChatRoom;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  @Column(unique = true)
  private String email;

  private String password;

  private String nickname;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<UserChatRoom> userChatRooms;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<ChatMsg> chatMsgs;
}
