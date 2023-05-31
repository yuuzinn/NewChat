package project.newchat.friend.controller;

import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.common.type.ResponseMessage;
import project.newchat.common.util.ResponseUtils;
import project.newchat.friend.domain.Friend;
import project.newchat.friend.dto.FriendDto;
import project.newchat.friend.service.FriendService;

@RestController
@RequiredArgsConstructor
public class FriendController {

  private final FriendService friendService;
  @PostMapping("/friend/{toUserId}")
  public ResponseEntity<Object> addFriend(
      @PathVariable Long toUserId,
      HttpSession session) {
    Long myUserId = (Long) session.getAttribute("user");
    if (toUserId.equals(myUserId)) {
      return ResponseUtils.badRequest(ResponseMessage.NO_ADD_TO_FRIEND_MYSELF);
    }
    friendService.addFriend(toUserId, myUserId);
    return ResponseUtils.ok(ResponseMessage.ADD_TO_FRIEND_SUCCESS);
  }

  @PostMapping("/friend/receive/{toUserId}")
  public ResponseEntity<Object> receiveFriend(
      @PathVariable Long toUserId,
      HttpSession session) {
    Long myUserId = (Long) session.getAttribute("user");
    if (toUserId.equals(myUserId)) {
      return ResponseUtils.badRequest(ResponseMessage.NO_RECEIVE_TO_FRIEND_MYSELF);
    }
    friendService.receiveFriend(toUserId, myUserId);
    return ResponseUtils.ok(ResponseMessage.RECEIVE_FRIEND_SUCCESS);
  }
  // 친구요청 했던 상대방에게 친구요청 취소
  @DeleteMapping("/friend/cancel/{toUserId}")
  public ResponseEntity<Object> cancelFriend(
      @PathVariable Long toUserId,
      HttpSession session) {
    Long myUserId = (Long) session.getAttribute("user");
    friendService.cancelFriend(toUserId, myUserId);
    return ResponseUtils.ok(ResponseMessage.CANCEL_FRIEND_SUCCESS);
  }

  // 상대방에게 온 친구요청 거절
  @DeleteMapping("/friend/{toUserId}")
  public ResponseEntity<Object> refuseFriend(
      @PathVariable Long toUserId,
      HttpSession session) {
    Long myUserId = (Long) session.getAttribute("user");
    friendService.refuseFriend(toUserId, myUserId);
    return ResponseUtils.ok(ResponseMessage.REFUSE_FRIEND_SUCCESS);
  }
  // 친구 목록 조회
  @GetMapping("/friend")
  public ResponseEntity<Object> getFriendList(
      HttpSession session
      , Pageable pageable) {
    Long myUserId = (Long) session.getAttribute("user");
    List<FriendDto> friendDtoList = friendService.getFriendList(myUserId, pageable);
    Long currentFriendNum = friendService.getCurrentFriendNum(myUserId);
    if (friendDtoList.size() == 0) {
      return ResponseUtils.notFound(ResponseMessage.NOT_EXIST_FRIEND_LIST);
    }
    return ResponseUtils
        .friendSelectOk(ResponseMessage.FRIENDS_SELECT_SUCCESS, friendDtoList, currentFriendNum);
  }
  // 친구 삭제
  @DeleteMapping("/unfriend/{toUserId}")
  public ResponseEntity<Object> unfriend(
      @PathVariable Long toUserId,
      HttpSession session) {
    Long myUserId = (Long) session.getAttribute("user");
    friendService.unfriend(toUserId, myUserId);
    return ResponseUtils.ok(ResponseMessage.FRIEND_DELETE_SUCCESS);
  }
}
