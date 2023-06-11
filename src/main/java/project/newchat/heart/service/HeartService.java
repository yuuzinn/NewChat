package project.newchat.heart.service;

public interface HeartService {

  void heart(Long userId, Long roomId);

  void heartDelete(Long userId, Long roomId);
}
