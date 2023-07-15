package project.newchat.chatroom.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.newchat.chatmsg.domain.ChatMsg;
import project.newchat.heart.domain.Heart;
import project.newchat.user.domain.User;
import project.newchat.userchatroom.domain.UserChatRoom;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    private String title;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long roomCreator;

    private Integer userCountMax; // 최대 인원 8명

    private String password;

    private Boolean isPrivate;


    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<UserChatRoom> userChatRooms;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<ChatMsg> chatMsgs;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<Heart> hearts;

    @Override
    public String toString() {
        return "ChatRoom{" +
            "id=" + id +
            ", title='" + title + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", roomCreator=" + roomCreator +
            ", userCountMax=" + userCountMax +
            '}';
    }
    public void update(String title, LocalDateTime updatedAt) {
        this.setTitle(title);
        this.setUpdatedAt(updatedAt);
    }
}
