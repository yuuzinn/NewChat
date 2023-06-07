package project.newchat.friend.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.newchat.friend.dto.FriendDto;

public interface FriendService {

  void addFriend(Long fromUserId, Long toUserId);

  void receive(Long fromUserId, Long toUserId);

  void refuse(Long fromUserId, Long toUserId);

  void delete(Long fromUserId, Long toUserId);

  List<FriendDto> selectFriendList(Long fromUserId, Pageable pageable);
}
