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
import project.newchat.common.type.ResponseMessage;
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
      return ResponseUtils.ok(ResponseMessage.SEND_CHAT_MSG_SUCCESS, response);
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
      return ResponseUtils
          .ok(ResponseMessage.CHAT_ROOM_MSG_LIST_SELECT_SUCCESS, roomChatMsgList);
    } else {
      return ResponseUtils
          .notFound(ResponseMessage.NOT_EXIST_CHAT_ROOM_MSG_LIST);
    }
  }
}