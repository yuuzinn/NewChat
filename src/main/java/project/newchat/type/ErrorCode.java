package project.newchat.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST("잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR("서버 오류입니다."),
    ALREADY_USER_ID("중복된 아이디입니다."),
    INCONSISTENCY_USER_ID_PASSWORD("아이디와 비밀번호 불일치")
    ;

    private final String description;
}