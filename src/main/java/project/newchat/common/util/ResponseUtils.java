package project.newchat.common.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
  public static ResponseEntity<Object> ok(String message, Object result) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", true);
    response.put("code", HttpStatus.OK.value());
    response.put("message", message);
    response.put("result", result);
    return ResponseEntity.ok(response);
  }

  public static ResponseEntity<Object> ok(String message) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", true);
    response.put("code", HttpStatus.OK.value());
    response.put("message", message);
    return ResponseEntity.ok(response);
  }

  public static ResponseEntity<Object> notFound(String message) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", false);
    response.put("code", HttpStatus.NOT_FOUND.value());
    response.put("message", message);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }


  public static ResponseEntity<Object> badRequest(String message) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", false);
    response.put("code", HttpStatus.BAD_REQUEST.value());
    response.put("message", message);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  public static ResponseEntity<Object> serverError(String message) {
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("isSuccess", false);
    response.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.put("message", message);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
