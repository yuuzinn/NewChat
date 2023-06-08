package project.newchat.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FriendDto {
  private String nickname; // 친구 닉네임
  private Boolean status; // 친구 로그인 상태
}
