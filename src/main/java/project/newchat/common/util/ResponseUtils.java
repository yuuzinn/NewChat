package project.newchat.common.util;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.newchat.common.type.ResponseMessage;

public class ResponseUtils {
  public static ResponseEntity<Object> ok(ResponseMessage message, Object result) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", true);
    response.put("status", HttpStatus.OK.value());
    response.put("message", message);
    response.put("result", result);
    return ResponseEntity.ok(response);
  }

  public static ResponseEntity<Object> friendSelectOk(
      ResponseMessage message, Object result, Long currentNum) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", true);
    response.put("status", HttpStatus.OK.value());
    response.put("message", message);
    response.put("currentNum", currentNum);
    response.put("maxNum", 50);
    response.put("result", result);
    return ResponseEntity.ok(response);
  }

  public static ResponseEntity<Object> ok(ResponseMessage message) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", true);
    response.put("status", HttpStatus.OK.value());
    response.put("message", message);
    return ResponseEntity.ok(response);
  }

  public static ResponseEntity<Object> notFound(ResponseMessage message) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", false);
    response.put("status", HttpStatus.NOT_FOUND.value());
    response.put("message", message);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  public static ResponseEntity<Object> badRequest(ResponseMessage message) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", false);
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("message", message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }
}
