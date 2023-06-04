package project.newchat.friend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.friend.service.FriendService;

@RestController
@RequiredArgsConstructor
public class FriendController {

  private final FriendService friendService;

}
