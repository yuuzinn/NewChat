package project.newchat.friend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.friend.repository.FriendRepository;
import project.newchat.user.repository.UserRepository;
import project.newchat.user.service.UserService;

@SpringBootTest
@Transactional
class FriendServiceImplTest {
  @Autowired
  private FriendService friendService;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private FriendRepository friendRepository;

  @Autowired
  private UserService userService;

}