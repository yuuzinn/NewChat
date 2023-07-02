package project.newchat.friend.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.newchat.friend.dto.FriendDto;

public interface FriendService {

  /**
   * 친구 추가
   */
  void addFriend(Long fromUserId, Long toUserId);

  /**
   * 친구 요청 수락
   */
  void receive(Long fromUserId, Long toUserId);

  /**
   * 친구 요청 거절
   */
  void refuse(Long fromUserId, Long toUserId);

  /**
   * 친구 삭제
   */
  void delete(Long fromUserId, Long toUserId);

  /**
   * 친구 목록 조회
   */
  List<FriendDto> selectFriendList(Long fromUserId, Pageable pageable);
}
