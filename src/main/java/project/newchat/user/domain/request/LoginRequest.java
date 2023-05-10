package project.newchat.user.domain.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
  @Email // email 형식
  @NotEmpty // valid 유효성(String 은 Empty, 다른 타입은 NotNull)
  @Length(min = 4, max = 20) // 길이 제한 (입력값이 포함하지 않는 경우 오류)
  private String email;
  @NotEmpty
  @Length(min = 4, max = 20)
  private String password;
}
