package project.newchat.chatroom.service;

import org.springframework.stereotype.Service;
import project.newchat.chatroom.controller.request.ChatRoomRequest;

@Service
public interface ChatRoomService {


  void createRoom(ChatRoomRequest chatRoomRequest, Long userId);

  void joinRoom(Long roomId, Long userId);
}
