package project.newchat.friend.service;

import static project.newchat.common.type.ErrorCode.ALREADY_FRIEND;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_FRIEND_USER;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_USER;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.common.exception.CustomException;
import project.newchat.friend.domain.Friend;
import project.newchat.friend.repository.FriendRepository;
import project.newchat.user.domain.User;
import project.newchat.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

  private final UserRepository userRepository;
  private final FriendRepository friendRepository;

  @Override
  @Transactional
  public void addFriend(Long toUserId, Long myUserId) {

    User friend = userRepository.findById(toUserId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    User mySelf = userRepository.findById(myUserId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    Friend isFriendCheck = friendRepository.findByUserIdAndToUserId(mySelf.getId(), friend.getId());
    if (isFriendCheck == null) { // 처음 친구 신청한 상태

      Friend me = Friend.builder().user(mySelf).toUserId(friend.getId()).isFriend(true).build();

      Friend fri = Friend.builder().user(friend).toUserId(mySelf.getId()).isFriend(false).build();

      friendRepository.save(me);
      friendRepository.save(fri);

    } else {
      if (isFriendCheck.getIsFriend()) {
        throw new CustomException(ALREADY_FRIEND);
      }
    }
  }

  @Override
  @Transactional
  public void receiveFriend(Long toUserId, Long myUserId) {
    findUser(toUserId, myUserId);
    Friend receiver = friendRepository // 2,   1,  false
        .findByUserIdAndToUserIdAndIsFriend(myUserId, toUserId, false)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    receiver.setIsFriend(true);
    friendRepository.save(receiver);
  }

  @Override
  @Transactional
  public void cancelFriend(Long toUserId, Long myUserId) {
    findUser(toUserId, myUserId);
    Friend mySelf = friendRepository.findByUserIdAndToUserIdAndIsFriend(
            myUserId, toUserId, true)
        .orElseThrow(() -> new CustomException(NOT_FOUND_FRIEND_USER));
    Friend friendSelf = friendRepository.findByUserIdAndToUserIdAndIsFriend(
            toUserId, myUserId, false)
        .orElseThrow(() -> new CustomException(NOT_FOUND_FRIEND_USER));
    friendRepository.deleteById(mySelf.getId());
    friendRepository.deleteById(friendSelf.getId());
  }

  @Override
  @Transactional
  public void refuseFriend(Long toUserId, Long myUserId) {
    findUser(toUserId, myUserId);
    Friend mySelf = friendRepository.findByUserIdAndToUserIdAndIsFriend(
            toUserId, myUserId, true)
        .orElseThrow(() -> new CustomException(NOT_FOUND_FRIEND_USER));
    Friend friendSelf = friendRepository.findByUserIdAndToUserIdAndIsFriend(
            myUserId, toUserId, false)
        .orElseThrow(() -> new CustomException(NOT_FOUND_FRIEND_USER));
    friendRepository.deleteById(mySelf.getId());
    friendRepository.deleteById(friendSelf.getId());
  }

  private void findUser(Long toUserId, Long myUserId) {
    userRepository.findById(toUserId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    userRepository.findById(myUserId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
  }
}
