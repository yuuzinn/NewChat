package project.newchat.common.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
@Slf4j
public class ChatWebSocketHandler implements WebSocketHandler {

  private List<WebSocketSession> list = new ArrayList<>();

  // 연결이 되었을 때
  @Override
  public void afterConnectionEstablished(WebSocketSession session)
      throws Exception {
    list.add(session);
    log.info(session + "의 클라이언트 접속");
  }

  // 클라이언트로부터 받은 메시지를 처리하는 로직
  @Override
  public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
      throws Exception {
    // 메시지 처리 로직
    String payload = message.getPayload().toString();
    log.info("전송 메시지 : " + payload);
    // 받은 메시지 다른 client에게 전달
    for (WebSocketSession s : list) {
      try {
        s.sendMessage(message);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  //오류 처리 로직을 구현 (네트워크 오류, 프로토콜 오류, 처리 오류... 생각 중)
  @Override
  public void handleTransportError(WebSocketSession session, Throwable exception)
      throws Exception {
    log.error(exception.getMessage());
  }

  // 연결 종료되었을 때
  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
      throws Exception {
    list.remove(session);
    log.info(session + "의 클라이언트 접속 해제");
  }

  //부분 메시지를 지원하는지 여부를 반환 (아직까지는 필요 없으니 false)
  //대용량(사진이나 동영상 등)이 필요한 경우에는 따로 구현할 필요가 있음.
  @Override
  public boolean supportsPartialMessages() {
    return false;
  }
}
