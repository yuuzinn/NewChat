package project.newchat.user.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    private String email;
    private String password;
    private String nickname;
}
