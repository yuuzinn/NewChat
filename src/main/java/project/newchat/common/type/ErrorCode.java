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
  ALREADY_USER_NICKNAME("중복된 닉네임입니다."),
  REQUEST_SAME_AS_CURRENT_NICKNAME("현재 닉네임과 바꾸려는 닉네임이 같습니다."),
  REQUEST_SAME_AS_CURRENT_TITLE("현재 제목과 바꾸려는 제목이 같습니다."),
  INCONSISTENCY_USER_ID_PASSWORD("아이디와 비밀번호 불일치"),
  NOT_LOGIN("로그인이 되어 있지 않습니다."),
  NOT_FOUND_USER("유저를 찾을 수 없습니다."),
  NOT_FOUND_ROOM("이미 삭제된 방이거나 방을 찾을 수 없습니다."),
  ROOM_USER_FULL("방에 사용자가 다 차 있습니다."),
  NONE_ROOM("현재 방이 없습니다."),
  CHAT_ERROR("채팅이 전송에 오류가 있습니다."),
  NOT_ROOM_CREATOR("방 생성자가 아닙니다."),
  NOT_EXIST_CLIENT("채팅방에 클라이언트가 없습니다."),
  ALREADY_JOIN_ROOM("이미 채팅방에 입장해 있습니다."),
  FAILED_GET_LOCK("락을 획득하지 못했습니다."),
  NOT_SAME_PASSWORD("비밀번호 불일치"),
  ALREADY_FRIEND("이미 친구인 상태입니다."),
  ALREADY_REQUEST_FRIEND("이미 친구요청을 보낸 상태입니다."),
  NEEDFUL_FRIEND_RECEIVE("요청에 대한 응답을 해 주어야 합니다."),
  NOT_FOUND_RECEIVE_FRIEND_USER("친구요청을 받은 사용자를 찾을 수 없습니다."),
  FRIEND_LIST_IS_FULL("친구 목록이 꽉 차 있습니다."),
  TO_USER_FRIEND_LIST_IS_FULL("상대방의 친구 목록이 꽉 차 있습니다."),
  NOT_FOUND_REQUEST_FRIEND("친구 요청을 찾을 수 없습니다."),
  NOT_FOUND_FRIEND("친구 관계를 찾을 수 없습니다. 해당 유저와 친구가 아닙니다."),
  CAN_NOT_DELETE_FRIEND("더이상 친구를 삭제할 수 없습니다."),
  NOT_ROOM_MEMBER("채팅방에 속한 유저가 아닙니다."),
  ALREADY_HEART_TO_ROOM("이미 해당 채팅방에 좋아요를 누르셨습니다."),
  NOT_EXIST_ROOM_HEART("해당 채팅방에 좋아요가 눌러져 있지 않습니다."),
  NOT_FOUND_HEART("좋아요 누른 채팅방이 없습니다."),
  ROOM_PASSWORD_MISMATCH("방 비밀번호 불일치"),
  NEED_TO_PASSWORD("비밀번호를 입력해 주세요."),
  NEED_TO_INPUT_TITLE("검색에 필요한 채팅방 제목을 입력해 주세요.")
  ;

  private final String description;
}