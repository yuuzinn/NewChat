package project.newchat.user.domain.request;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
  @NotEmpty
  @Length(min = 4, max = 20)
  private String nickname;
}
