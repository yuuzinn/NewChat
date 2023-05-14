package project.newchat.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST("잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR("서버 오류입니다."),
    ALREADY_USER_ID("중복된 아이디입니다."),
    INCONSISTENCY_USER_ID_PASSWORD("아이디와 비밀번호 불일치"),
    NOT_LOGIN("로그인이 되어 있지 않습니다."),
    NOT_FOUND_USER("유저를 찾을 수 없습니다."),
    NOT_FOUND_ROOM("이미 삭제된 방이거나 방을 찾을 수 없습니다."),
    ROOM_USER_FULL("방에 사용자가 다 차 있습니다."),
    NONE_ROOM("현재 방이 없습니다."),
    CHAT_ERROR("채팅이 전송에 오류가 있습니다."),
    NOT_ROOM_CREATOR("방 생성자가 아닙니다."),
    NOT_EXIST_CLIENT("채팅방에 클라이언트가 없습니다."),
    ALREADY_JOIN_ROOM("이미 채팅방에 입장해 있습니다.")
    ;

    private final String description;
}