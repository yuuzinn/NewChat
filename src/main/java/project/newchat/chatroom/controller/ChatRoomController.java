package project.newchat.chatroom.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
import project.newchat.chatroom.service.ChatRoomService;
import project.newchat.common.config.LoginCheck;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
  private final ChatRoomService chatRoomService;

  // 방을 만든 사람이 방장임.(roomCreator 지정해주기)
  // 방제는 2자 이상 validation 걸기
  @PostMapping("/room")
  @LoginCheck
  public ResponseEntity<Object> createRoom(
      @RequestBody @Valid ChatRoomRequest chatRoomRequest,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    chatRoomService.createRoom(chatRoomRequest,userId);
    return ResponseEntity.ok().body("success");
  }

  // 방의 key를 통해 입장할 수 있어야 함.
  // 동시성 이슈 체크
  @PostMapping("/room/join/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> joinRoom (
      @PathVariable Long roomId,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    chatRoomService.joinRoom(roomId, userId);
    return ResponseEntity.ok().body("success");
  }
}
