package project.newchat.chatroom.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
import project.newchat.chatroom.controller.request.ChatRoomUpdateRequest;
import project.newchat.chatroom.domain.ChatRoom;
import project.newchat.chatroom.dto.ChatRoomDto;
import project.newchat.chatroom.dto.ChatRoomUserDto;

public interface ChatRoomService {


  ChatRoom createRoom(ChatRoomRequest chatRoomRequest, Long userId);

  void joinRoom(Long roomId, Long userId, ChatRoomRequest chatRoomRequest);

  List<ChatRoomDto> getRoomList(Pageable pageable);

  List<ChatRoomDto> getRoomHeartSortList(Pageable pageable);


  List<ChatRoomDto> roomsByCreatorUser(Long userId, Pageable pageable);

  List<ChatRoomDto> getUserByRoomPartList(Long userId, Pageable pageable);

  void outRoom(Long userId, Long roomId);

  void deleteRoom(Long userId, Long roomId);

  void updateRoom(Long roomId, ChatRoomUpdateRequest chatRoomUpdateRequest, Long userId);

  List<ChatRoomUserDto> getRoomUsers(Long roomId, Long userId);

  List<ChatRoomDto> myHeartRoomList(Long userId, Pageable pageable);

  List<ChatRoomDto> searchRoomByTitle(String roomName, Long userId, Pageable pageable);
}
