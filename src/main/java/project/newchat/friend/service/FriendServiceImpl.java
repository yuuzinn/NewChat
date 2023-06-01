package project.newchat.friend.service;

import static project.newchat.common.type.ErrorCode.ALREADY_FRIEND;
import static project.newchat.common.type.ErrorCode.NEEDFUL_FRIEND_RECEIVE;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_RECEIVE_FRIEND_USER;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_USER;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
import project.newchat.friend.domain.Friend;
import project.newchat.friend.dto.FriendDto;
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

    User friendSelf = userRepository.findById(toUserId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    User mySelf = userRepository.findById(myUserId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    Optional<Friend> friend = friendRepository
        .findByUserIdAndToUserId(mySelf.getId(), friendSelf.getId());

    Long currentFriendNum = getCurrentFriendNum(myUserId);
    if (currentFriendNum >= 50) {
      throw new CustomException(ErrorCode.FRIEND_LIST_IS_FULL);
    }
    if (friend.isPresent()) { // 처음 친구 신청한 상태
      // !null
      if (friend.get().getIsFriend()) {
        throw new CustomException(ALREADY_FRIEND);
      } else {
        throw new CustomException(NEEDFUL_FRIEND_RECEIVE);
      }

    } else {
      //friend == null
      Friend me = Friend.builder().user(mySelf).toUserId(friendSelf.getId()).isFriend(true).build();

      Friend fri = Friend.builder().user(friendSelf).toUserId(mySelf.getId()).isFriend(false).build();

      friendRepository.save(me);
      friendRepository.save(fri);
    }
  }

  @Override
  @Transactional
  public void receiveFriend(Long toUserId, Long myUserId) {
    findUser(toUserId, myUserId);
    Long currentFriendNum = getCurrentFriendNum(myUserId);
    if (currentFriendNum >= 50) {
      throw new CustomException(ErrorCode.FRIEND_LIST_IS_FULL);
    }
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
        .orElseThrow(() -> new CustomException(NOT_FOUND_RECEIVE_FRIEND_USER));
    Friend friendSelf = friendRepository.findByUserIdAndToUserIdAndIsFriend(
            toUserId, myUserId, false)
        .orElseThrow(() -> new CustomException(NOT_FOUND_RECEIVE_FRIEND_USER));
    friendRepository.deleteById(mySelf.getId());
    friendRepository.deleteById(friendSelf.getId());
  }

  @Override
  @Transactional
  public void refuseFriend(Long toUserId, Long myUserId) {
    findUser(toUserId, myUserId);
    Friend friendSelf = friendRepository.findByUserIdAndToUserIdAndIsFriend(
            toUserId, myUserId, true)
        .orElseThrow(() -> new CustomException(NOT_FOUND_RECEIVE_FRIEND_USER));
    Friend mySelf = friendRepository.findByUserIdAndToUserIdAndIsFriend(
            myUserId, toUserId, false)
        .orElseThrow(() -> new CustomException(NOT_FOUND_RECEIVE_FRIEND_USER));
    friendRepository.deleteById(mySelf.getId());
    friendRepository.deleteById(friendSelf.getId());
  }

  @Override
  @Transactional(readOnly = true)
  public List<FriendDto> getFriendList(Long myUserId, Pageable pageable) {
    userRepository.findById(myUserId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    List<Friend> myFriendList = friendRepository.findFriendByUserId(myUserId, pageable);
    List<FriendDto> friendDtoList = new ArrayList<>();
    for (Friend friend : myFriendList) {

      User fri = userRepository
          .findById(friend.getUser().getId())
          .orElseThrow(() -> new CustomException(NOT_FOUND_USER));

      friendDtoList.add(FriendDto.builder()
          .nickname(fri.getNickname())
          .build());
    }
    return friendDtoList;
  }

  @Override
  @Transactional(readOnly = true)
  public Long getCurrentFriendNum(Long userId) {
    return friendRepository.countByUserId(userId);
  }

  @Override
  @Transactional
  public void unfriend(Long toUserId, Long myUserId) {
    findUser(toUserId, myUserId);

    Optional<Friend> me = friendRepository
        .findByUserIdAndToUserId(myUserId, toUserId);
    Optional<Friend> friend = friendRepository
        .findByUserIdAndToUserId(toUserId, myUserId);

    if (!me.isPresent() || !friend.isPresent()) {
      throw new CustomException(NOT_FOUND_USER);
    }

    friendRepository.deleteById(me.get().getId());
    friendRepository.deleteById(friend.get().getId());
  }

  private void findUser(Long toUserId, Long myUserId) {
    userRepository.findById(toUserId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    userRepository.findById(myUserId)
        .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
  }
}
