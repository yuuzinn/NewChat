package project.newchat.heart.service;

public interface HeartService {

  /**
   * 채팅방 좋아요 누르기
   */
  void heart(Long userId, Long roomId);

  /**
   * 채팅방 좋아요 취소
   */
  void heartDelete(Long userId, Long roomId);
}
