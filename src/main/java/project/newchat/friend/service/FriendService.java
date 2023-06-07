package project.newchat.friend.service;

public interface FriendService {

  void addFriend(Long fromUserId, Long toUserId);

  void receive(Long fromUserId, Long toUserId);

  void refuse(Long fromUserId, Long toUserId);

  void delete(Long fromUserId, Long toUserId);
}
