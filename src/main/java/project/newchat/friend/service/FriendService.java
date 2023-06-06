package project.newchat.friend.service;

public interface FriendService {

  void addFriend(Long fromUserId, Long toUserId);

  void receive(Long fromUserId, Long toUserId);
}
