package project.newchat.chatroom.controller;

import static project.newchat.common.type.ResponseMessage.CHAT_ROOM_ALL_BY_LIST_SELECT_SUCCESS;
import static project.newchat.common.type.ResponseMessage.CHAT_ROOM_USERS_SELECT_SUCCESS;
import static project.newchat.common.type.ResponseMessage.CHAT_ROOM_USER_SELF_BY_LIST_SELECT_SUCCESS;
import static project.newchat.common.type.ResponseMessage.CHAT_ROOM_USER_SELF_PART_BY_LIST_SELECT_SUCCESS;
import static project.newchat.common.type.ResponseMessage.CREATE_CHAT_ROOM_SUCCESS;
import static project.newchat.common.type.ResponseMessage.DELETE_CHAT_ROOM_SUCCESS;
import static project.newchat.common.type.ResponseMessage.JOIN_CHAT_ROOM_SUCCESS;
import static project.newchat.common.type.ResponseMessage.NOT_EXIST_CHAT_ROOM;
import static project.newchat.common.type.ResponseMessage.NOT_EXIST_CHAT_ROOM_BY_USER_SELF;
import static project.newchat.common.type.ResponseMessage.NOT_EXIST_CHAT_ROOM_BY_USER_SELF_PART;
import static project.newchat.common.type.ResponseMessage.OUT_CHAT_ROOM_SUCCESS;
import static project.newchat.common.type.ResponseMessage.ROOM_UPDATE_SUCCESS;

import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.newchat.chatroom.controller.request.ChatRoomRequest;
import project.newchat.chatroom.controller.request.ChatRoomUpdateRequest;
import project.newchat.chatroom.dto.ChatRoomDto;
import project.newchat.chatroom.dto.ChatRoomUserDto;
import project.newchat.chatroom.service.ChatRoomService;
import project.newchat.common.config.LoginCheck;
import project.newchat.common.util.ResponseUtils;


@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  // 방을 만든 사람이 방장임.(roomCreator 지정해주기)
  // 방제는 2자 이상 validation 걸기
  @PostMapping("/room")
  @LoginCheck
  public ResponseEntity<Object> createRoom(
      @RequestBody @Valid ChatRoomRequest chatRoomRequest,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    chatRoomService.createRoom(chatRoomRequest, userId);
    return ResponseUtils.ok(CREATE_CHAT_ROOM_SUCCESS);
  }

  // 방의 key를 통해 입장할 수 있어야 함.
  // 동시성 이슈 체크
  @PostMapping("/room/join/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> joinRoom(
      @PathVariable Long roomId,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    chatRoomService.joinRoom(roomId, userId);
    return ResponseUtils.ok(JOIN_CHAT_ROOM_SUCCESS);
  }

  // 전체 리스트
  @GetMapping("/room")
  @LoginCheck
  public ResponseEntity<Object> roomList(
      Pageable pageable) {
    List<ChatRoomDto> roomList = chatRoomService.getRoomList(pageable);
    if (roomList.size() == 0) {
      return ResponseUtils.notFound(NOT_EXIST_CHAT_ROOM);
    } else {
      return ResponseUtils
          .ok(CHAT_ROOM_ALL_BY_LIST_SELECT_SUCCESS, roomList);
    }
  }
  // 전체 리스트에서 좋아요 순으로 정렬
  @GetMapping("/room/heart")
  @LoginCheck
  public ResponseEntity<Object> roomHeartSort(Pageable pageable) {
    List<ChatRoomDto> roomList = chatRoomService.getRoomHeartSortList(pageable);
    if (roomList.size() == 0) {
      return ResponseUtils.notFound(NOT_EXIST_CHAT_ROOM);
    } else {
      return ResponseUtils.ok(CHAT_ROOM_ALL_BY_LIST_SELECT_SUCCESS, roomList);
    }
  }

  // 사용자(자신)가 생성한 방 리스트 조회
  @GetMapping("/room/creator")
  @LoginCheck
  public ResponseEntity<Object> getByUserRoomList(
      HttpSession session, Pageable pageable) {

    Long userId = (Long) session.getAttribute("user");
    List<ChatRoomDto> userByRoomList = chatRoomService.roomsByCreatorUser(userId, pageable);
    if (userByRoomList.size() == 0) {
      return ResponseUtils
          .notFound(NOT_EXIST_CHAT_ROOM_BY_USER_SELF);
    } else {
      return ResponseUtils
          .ok(CHAT_ROOM_USER_SELF_BY_LIST_SELECT_SUCCESS, userByRoomList);
    }
  }

  // 사용자(자신)가 들어가 있는 방 리스트 조회
  @GetMapping("/room/part")
  @LoginCheck
  public ResponseEntity<Object> getByUserRoomPartList(
      HttpSession session, Pageable pageable) {
    Long userId = (Long) session.getAttribute("user");
    List<ChatRoomDto> userByRoomPartList = chatRoomService
        .getUserByRoomPartList(userId, pageable);
    if (userByRoomPartList.size() == 0) {
      return ResponseUtils
          .notFound(NOT_EXIST_CHAT_ROOM_BY_USER_SELF_PART);
    } else {
      return ResponseUtils
          .ok(CHAT_ROOM_USER_SELF_PART_BY_LIST_SELECT_SUCCESS, userByRoomPartList);
    }
  }

  // 채팅방 제목 수정
  @PatchMapping("/room/update/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> updateRoom(
      @PathVariable Long roomId,
      @RequestBody @Valid ChatRoomUpdateRequest chatRoomUpdateRequest,
      HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    chatRoomService.updateRoom(roomId, chatRoomUpdateRequest, userId);
    return ResponseUtils.ok(ROOM_UPDATE_SUCCESS);
  }

  // 채팅방 나가기
  @DeleteMapping("/room/out/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> outRoom(
      @PathVariable Long roomId, HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    chatRoomService.outRoom(userId, roomId);
    return ResponseUtils.ok(OUT_CHAT_ROOM_SUCCESS);
  }

  // 채팅방 삭제
  @DeleteMapping("/room/delete/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> deleteRoom(
      @PathVariable Long roomId, HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    chatRoomService.deleteRoom(userId, roomId);
    return ResponseUtils.ok(DELETE_CHAT_ROOM_SUCCESS);
  }
  // 최대 8명 제한이니, 페이징 처리는 X
  @GetMapping("/room/users/{roomId}")
  @LoginCheck
  public ResponseEntity<Object> getRoomUsers(
      @PathVariable Long roomId, HttpSession session) {
    Long userId = (Long) session.getAttribute("user");
    List<ChatRoomUserDto> roomUsers = chatRoomService.getRoomUsers(roomId, userId);
    return ResponseUtils.ok(CHAT_ROOM_USERS_SELECT_SUCCESS, roomUsers);
  }
}
