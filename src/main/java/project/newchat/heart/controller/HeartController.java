package project.newchat.heart.controller;

import static project.newchat.common.type.ResponseMessage.HEART_TO_ROOM_CANCELED;
import static project.newchat.common.type.ResponseMessage.HEART_TO_ROOM_SUCCESS;

import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.common.config.LoginCheck;
import project.newchat.common.util.ResponseUtils;
import project.newchat.heart.service.HeartService;

@RestController
@RequiredArgsConstructor
public class HeartController {

  private final HeartService heartService;

  @PostMapping("/heart/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> heart(
      @PathVariable Long roomId,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    heartService.heart(userId, roomId);
    return ResponseUtils.ok(HEART_TO_ROOM_SUCCESS);
  }

  @DeleteMapping("/heart/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> heartDelete(
      @PathVariable Long roomId,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    heartService.heartDelete(userId, roomId);
    return ResponseUtils.ok(HEART_TO_ROOM_CANCELED);
  }
}
