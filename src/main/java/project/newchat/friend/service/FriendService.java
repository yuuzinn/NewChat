package project.newchat.friend.service;

public interface FriendService {

  void addFriend(Long toUserId, Long myUserId);

  void receiveFriend(Long toUserId, Long myUserId);

  void cancelFriend(Long toUserId, Long myUserId);

  void refuseFriend(Long toUserId, Long myUserId);

  Long getCurrentFriendNum(Long userId);
}
