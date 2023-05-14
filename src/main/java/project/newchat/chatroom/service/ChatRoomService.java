package project.newchat.chatroom.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
import project.newchat.chatroom.dto.ChatRoomDto;

public interface ChatRoomService {


  void createRoom(ChatRoomRequest chatRoomRequest, Long userId);

  void joinRoom(Long roomId, Long userId);

  List<ChatRoomDto> getRoomList(Pageable pageable);


  List<ChatRoomDto> roomsByCreatorUser(Long userId, Pageable pageable);

  List<ChatRoomDto> getUserByRoomPartList(Long userId, Pageable pageable);

  void outRoom(Long userId, Long roomId);

  void deleteRoom(Long userId, Long roomId);
}
