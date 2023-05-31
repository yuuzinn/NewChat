package project.newchat.friend.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.newchat.friend.dto.FriendDto;
import project.newchat.user.domain.User;

public interface FriendService {

  void addFriend(Long toUserId, Long myUserId);

  void receiveFriend(Long toUserId, Long myUserId);

  void cancelFriend(Long toUserId, Long myUserId);

  void refuseFriend(Long toUserId, Long myUserId);

  List<FriendDto> getFriendList(Long myUserId, Pageable pageable);

  Long getCurrentFriendNum(Long userId);

  void unfriend(Long toUserId, Long myUserId);
}
