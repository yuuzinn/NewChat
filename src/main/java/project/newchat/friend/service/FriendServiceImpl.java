package project.newchat.friend.service;

import static project.newchat.common.type.ErrorCode.ALREADY_FRIEND;
import static project.newchat.common.type.ErrorCode.ALREADY_REQUEST_FRIEND;
import static project.newchat.common.type.ErrorCode.CAN_NOT_DELETE_FRIEND;
import static project.newchat.common.type.ErrorCode.FAILED_GET_LOCK;
import static project.newchat.common.type.ErrorCode.FRIEND_LIST_IS_FULL;
import static project.newchat.common.type.ErrorCode.NEEDFUL_FRIEND_RECEIVE;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_FRIEND;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_RECEIVE_FRIEND_USER;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_REQUEST_FRIEND;
import static project.newchat.common.type.ErrorCode.NOT_FOUND_USER;
import static project.newchat.common.type.ErrorCode.TO_USER_FRIEND_LIST_IS_FULL;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.newchat.common.exception.CustomException;
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

  // 친구 추가 요청
  @Override
  @Transactional
  public void addFriend(Long fromUserId, Long toUserId) {
    final String lockName = toUserId + " - lock"; // key 값 변경
    final RLock lock = redissonClient.getLock(lockName);
    try {
      boolean available = lock.tryLock(1, TimeUnit.SECONDS);

      if (!available) {
        throw new CustomException(FAILED_GET_LOCK);
      }
      Optional<User> fromUser = userRepository
          .findById(fromUserId);
      Optional<User> toUser = userRepository
          .findById(toUserId);
      // 현재 친구 인원 파악
      Long currentMyFriendCnt = friendRepository
          .countByFromUserIdOrToUserIdAndAccept(fromUserId); // 자신의 친구 수
      Long currentToUserFriendCnt = friendRepository
          .countByFromUserIdOrToUserIdAndAccept(toUserId); // 상대방의 친구 수
      if (currentMyFriendCnt >= 50) {
        throw new CustomException(FRIEND_LIST_IS_FULL);
      }
      if (currentToUserFriendCnt >= 50) {
        throw new CustomException(TO_USER_FRIEND_LIST_IS_FULL);
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

  // 친구 추가 수락
  @Override
  @Transactional
  public void receive(Long fromUserId, Long toUserId) {
    final String lockName = fromUserId + " - lock";
    final RLock lock = redissonClient.getLock(lockName);
    final String worker = Thread.currentThread().getName();
    try {
      boolean available = lock.tryLock(1, TimeUnit.SECONDS);

      if (!available) {
        throw new CustomException(FAILED_GET_LOCK);
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
      Long currentToUserFriendCnt = friendRepository
          .countByFromUserIdOrToUserIdAndAccept(toUserId); // 상대방의 친구 수
      if (currentFriendCount >= 50) {
        log.info("[{}] 친구 요청을 받을 수 없습니다. 현재 인원 수 : " + worker, currentFriendCount);
        throw new CustomException(FRIEND_LIST_IS_FULL);
      }
      if (currentToUserFriendCnt >= 50) {
        throw new CustomException(TO_USER_FRIEND_LIST_IS_FULL);
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

  // 친구 추가 요청 거절
  @Override
  @Transactional
  public void refuse(Long fromUserId, Long toUserId) { // 1, 2, false -> 2의 입장에선..
    final String lockName = "lock"; // 상대방 아이디로 lock
    final RLock lock = redissonClient.getLock(lockName);
    try {
      boolean available = lock.tryLock(1, TimeUnit.SECONDS);

      if (!available) {
        throw new CustomException(FAILED_GET_LOCK);
      }
      Optional<User> fromUser = userRepository
          .findById(fromUserId);
      Optional<User> toUser = userRepository
          .findById(toUserId);
      if (fromUser.isEmpty() || toUser.isEmpty()) {
        throw new CustomException(NOT_FOUND_USER);
      }
      // 나에게 신청한 데이터 확인
      Optional<Friend> request = friendRepository
          .findByFromUserIdAndToUserId(toUserId, fromUserId); // (상대, 나)
      if (request.isEmpty()) {
        throw new CustomException(NOT_FOUND_REQUEST_FRIEND);
      }
      Long requestId = request.get().getId();
      friendRepository.deleteById(requestId);
    } catch (InterruptedException e) {
      throw new CustomException(FAILED_GET_LOCK);
    } finally {
      lock.unlock();
    }
  }

  @Override
  @Transactional
  public void delete(Long fromUserId, Long toUserId) {
    final String lockName = "lock"; //  fromUserId + toUserId
    final RLock lock = redissonClient.getLock(lockName);
    try {
      boolean available = lock.tryLock(1, TimeUnit.SECONDS);

      if (!available) {
        throw new CustomException(FAILED_GET_LOCK);
      }
      Optional<User> fromUser = userRepository
          .findById(fromUserId);
      Optional<User> toUser = userRepository
          .findById(toUserId);
      // todo : 둘 다 같이 삭제할 경우 -> 데이터 찾기 (순방향, 역방향 둘 다 찾아야함)
      Optional<Friend> forward = friendRepository
          .findByFromUserIdAndToUserIdAndAccept(fromUserId, toUserId, true);
      Optional<Friend> reverse = friendRepository
          .findByFromUserIdAndToUserIdAndAccept(toUserId, fromUserId, true);
      // todo : 현재 친구 인원 찾기
      Long currentFriendCount = friendRepository
          .countByFromUserIdOrToUserIdAndAccept(fromUserId);
      if (currentFriendCount <= 0) {
        throw new CustomException(CAN_NOT_DELETE_FRIEND);
      }
      if (forward.isEmpty() && reverse.isEmpty()) { // 둘이 친구 아님
        throw new CustomException(NOT_FOUND_FRIEND);
      } else if (forward.isPresent()) { // 순방향일 경우 있을 때
        Friend forwardFriend = forward.get();
        friendRepository.deleteById(forwardFriend.getId());
      } else { // 역방향일 경우 있을 때
        Friend reverseFriend = reverse.get();
        friendRepository.deleteById(reverseFriend.getId());
      }

      if (fromUser.isEmpty() || toUser.isEmpty()) {
        throw new CustomException(NOT_FOUND_USER);
      }

    } catch (InterruptedException e) {
      throw new CustomException(FAILED_GET_LOCK);
    } finally {
      lock.unlock();
    }
  }
}
