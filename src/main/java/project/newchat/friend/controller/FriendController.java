package project.newchat.friend.controller;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.common.config.LoginCheck;
import project.newchat.common.type.ResponseMessage;
import project.newchat.common.util.ResponseUtils;
import project.newchat.friend.service.FriendService;

@RestController
@RequiredArgsConstructor
public class FriendController {

  private final FriendService friendService;

  @PostMapping("/friend/{toUserId}")
  @LoginCheck
  public ResponseEntity<Object> addFriend(
      @PathVariable Long toUserId,
      HttpSession session){
    Long fromUserId = (Long) session.getAttribute("user");
    friendService.addFriend(fromUserId, toUserId);
    if (toUserId.equals(fromUserId)) {
      return ResponseUtils.badRequest(ResponseMessage.NO_ADD_TO_FRIEND_MYSELF);
    }
    return ResponseUtils.ok(ResponseMessage.ADD_TO_FRIEND_SUCCESS);
  }
  @PostMapping("/receive/{toUserId}")
  @LoginCheck
  public ResponseEntity<Object> receive(
      @PathVariable Long toUserId,
      HttpSession session) {
    Long fromUserId = (Long) session.getAttribute("user");
    friendService.receive(fromUserId, toUserId);
    return ResponseUtils.ok(ResponseMessage.RECEIVE_FRIEND_SUCCESS);
  }
}
