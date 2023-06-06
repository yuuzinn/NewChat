package project.newchat.friend.service;

import static project.newchat.common.type.ErrorCode.ALREADY_FRIEND;
import static project.newchat.common.type.ErrorCode.ALREADY_REQUEST_FRIEND;
import static project.newchat.common.type.ErrorCode.FAILED_GET_LOCK;
import static project.newchat.common.type.ErrorCode.FRIEND_LIST_IS_FULL;
import static project.newchat.common.type.ErrorCode.NEEDFUL_FRIEND_RECEIVE;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_RECEIVE_FRIEND_USER;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_USER;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;
import project.newchat.friend.domain.Friend;
import project.newchat.friend.repository.FriendRepository;
import project.newchat.user.domain.User;
import project.newchat.user.repository.UserRepository;
@Slf4j
@Service
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService {

  private final UserRepository userRepository;

  private final FriendRepository friendRepository;

  private final RedissonClient redissonClient;


  @Override
  @Transactional
  public void addFriend(Long fromUserId, Long toUserId) {
    final String lockName = toUserId + " - lock";
    final RLock lock = redissonClient.getLock(lockName);
    try {
      boolean available = lock.tryLock(1, TimeUnit.SECONDS);

      if (!available) {
        throw new CustomException(ErrorCode.FAILED_GET_LOCK);
      }
      Optional<User> fromUser = userRepository
          .findById(fromUserId);
      Optional<User> toUser = userRepository
          .findById(toUserId);
      // 현재 친구 인원 파악
      Long currentFriendCount = friendRepository
          .countByFromUserIdOrToUserIdAndAccept(fromUserId);
      if (currentFriendCount >= 50) {
        throw new CustomException(FRIEND_LIST_IS_FULL);
      }

      if (fromUser.isEmpty() || toUser.isEmpty()) {
        throw new CustomException(NOT_FOUND_USER);
      }
      // 이미 친구신청 중인 상태 or 친구인 상태 throw
      Optional<Friend> fromUserFriend = friendRepository
          .findByFromUserIdAndToUserId(fromUserId, toUserId);
      if (fromUserFriend.isPresent()) {
        if (fromUserFriend.get().getAccept()) { // 친구인지 확인
          throw new CustomException(ALREADY_FRIEND);
        } else {
          throw new CustomException(ALREADY_REQUEST_FRIEND);
        }
      }
      // B가 A에게 이미 보냈는데, A가 B에게 보내는 경우 throw
      Optional<Friend> toUserFriend = friendRepository
          .findByFromUserIdAndToUserId(toUserId, fromUserId); // 역순으로 데이터 찾기
      if (toUserFriend.isPresent()) { // null이 아니라면  == 데이터가 있다면
        if (toUserFriend.get().getAccept()) {
          throw new CustomException(ALREADY_FRIEND);
        } else {
          throw new CustomException(NEEDFUL_FRIEND_RECEIVE);
        }
      }
      Friend build = Friend.builder()
          .fromUserId(fromUserId)
          .toUserId(toUserId)
          .accept(false)
          .build();
      friendRepository.save(build);
    } catch (InterruptedException e) {
      throw new CustomException(FAILED_GET_LOCK);
    } finally {
      lock.unlock();
    }
  }

  @Override
  @Transactional
  public void receive(Long fromUserId, Long toUserId) {
    final String lockName = fromUserId + " - lock";
    final RLock lock = redissonClient.getLock(lockName);
    final String worker = Thread.currentThread().getName();
    try {
      boolean available = lock.tryLock(1,  TimeUnit.SECONDS);

      if (!available) {
        throw new CustomException(ErrorCode.FAILED_GET_LOCK);
      }
    Optional<User> fromUser = userRepository
        .findById(fromUserId);
    Optional<User> toUser = userRepository
        .findById(toUserId);
    if (fromUser.isEmpty() || toUser.isEmpty()) {
      throw new CustomException(NOT_FOUND_USER);
    }
    // 현재 친구 인원 파악
    Long currentFriendCount = friendRepository
        .countByFromUserIdOrToUserIdAndAccept(fromUserId);
      System.out.println("currentFriendCount = " + currentFriendCount);
    if (currentFriendCount >= 50) {
      log.info("[{}] 친구 요청을 받을 수 없습니다. 현재 인원 수 : " + worker, currentFriendCount);
      throw new CustomException(FRIEND_LIST_IS_FULL);
    }
    log.info("현재 진행중인 사람 : {} & 현재 인원 수: " + worker, currentFriendCount);
    Optional<Friend> requester = friendRepository
        .findByFromUserIdAndToUserId(toUserId, fromUserId); // 주체가 상대이기에 순서 변경
    if (requester.isEmpty()) {
      throw new CustomException(NOT_FOUND_RECEIVE_FRIEND_USER);
    }
    Friend friend = requester.get();
    friend.setAccept(true);
    friendRepository.save(friend);
    } catch (InterruptedException e) {
      throw new CustomException(FAILED_GET_LOCK);
    } finally {
        lock.unlock();
    }
  }
}
