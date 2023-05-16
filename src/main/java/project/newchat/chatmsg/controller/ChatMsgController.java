package project.newchat.chatmsg.controller;


import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.chatmsg.domain.request.ChatMsgRequest;
import project.newchat.chatmsg.domain.response.ChatMsgResponse;
import project.newchat.chatmsg.dto.ChatMsgDto;
import project.newchat.chatmsg.service.ChatMsgService;
import project.newchat.common.config.LoginCheck;
import project.newchat.common.util.ResponseUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatMsgController {

  private final ChatMsgService chatMsgService;

  @PostMapping("/msg/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> sendChat(
      @PathVariable Long roomId,
      @RequestBody @Valid ChatMsgRequest message,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    ChatMsgResponse response = chatMsgService.sendMessage(message, userId, roomId);
    if (message == null) {
      return ResponseUtils.badRequest("채팅 메시지는 공백으로 보낼 수 없습니다.");
    } else {
      return ResponseUtils.ok("채팅 메시지 보내기 성공", response);
    }
  }

  @GetMapping("/msg/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> getChatList(
      @PathVariable Long roomId,
      @RequestParam(required = false) Long lastId,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    List<ChatMsgDto> roomChatMsgList = chatMsgService.getRoomChatMsgList(roomId, userId, lastId);
    if (roomChatMsgList != null) {
      return ResponseUtils.ok("해당 채팅방의 메시지들을 조회하였습니다.", roomChatMsgList);
    } else {
      return ResponseUtils.notFound("해당 채팅방의 메시지들을 찾지 못했습니다.");
    }
  }
}