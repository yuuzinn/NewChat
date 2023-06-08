package project.newchat.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {
  /**
   * USER
   */
  CREATE_USER,
  LOGIN_SUCCESS,
  LOGOUT_SUCCESS,
  USER_UPDATE_SUCCESS,
  /**
   * CHAT_ROOM
   */
  CREATE_CHAT_ROOM_SUCCESS,
  JOIN_CHAT_ROOM_SUCCESS,
  OUT_CHAT_ROOM_SUCCESS,
  DELETE_CHAT_ROOM_SUCCESS,
  NOT_EXIST_CHAT_ROOM,
  CHAT_ROOM_ALL_BY_LIST_SELECT_SUCCESS,
  NOT_EXIST_CHAT_ROOM_BY_USER_SELF,
  CHAT_ROOM_USER_SELF_BY_LIST_SELECT_SUCCESS,
  NOT_EXIST_CHAT_ROOM_BY_USER_SELF_PART,
  CHAT_ROOM_USER_SELF_PART_BY_LIST_SELECT_SUCCESS,
  ROOM_UPDATE_SUCCESS,
  CHAT_ROOM_USERS_SELECT_SUCCESS,
  /**
   * CHAT_MSG
   */
  SEND_CHAT_MSG_SUCCESS,
  CHAT_ROOM_MSG_LIST_SELECT_SUCCESS,
  NOT_EXIST_CHAT_ROOM_MSG_LIST,
  /**
   * FRIEND
   */
  ADD_TO_FRIEND_SUCCESS,
  NO_ADD_TO_FRIEND_MYSELF,
  NO_RECEIVE_TO_FRIEND_MYSELF,
  RECEIVE_FRIEND_SUCCESS,
  REFUSE_FRIEND_SUCCESS,
  FRIENDS_SELECT_SUCCESS,
  NOT_EXIST_FRIEND_LIST,
  FRIEND_DELETE_SUCCESS
}
