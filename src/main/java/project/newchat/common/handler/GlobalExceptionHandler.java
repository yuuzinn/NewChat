package project.newchat.common.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.newchat.common.exception.CustomException;
import project.newchat.dto.ErrorResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static project.newchat.common.type.ErrorCode.INTERNAL_SERVER_ERROR;
import static project.newchat.common.type.ErrorCode.INVALID_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ErrorResponse handleCustomException(CustomException e) {
    log.error("{} 가 발생하였습니다.", e.getErrorCode());

    return new ErrorResponse(e.getErrorCode(), e.getErrorMessage());
  }

  //유효성
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    log.error("유효한 값이 아닙니다.", e);
    List<FieldError> fieldError = e.getFieldErrors();
    Map<String, String> bindingResult = new HashMap<>();
    for (FieldError error : fieldError) {
      bindingResult.put(error.getField(), error.getDefaultMessage());
    }
    return new ErrorResponse(INVALID_REQUEST, INVALID_REQUEST.getDescription(), bindingResult);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException e) {
    log.error("잘못된 DATA 바인딩입니다. DATA가 잘못되었습니다.", e);

    return new ErrorResponse(INVALID_REQUEST, INVALID_REQUEST.getDescription());
  }

  @ExceptionHandler(Exception.class)
  public ErrorResponse handleException(Exception e) {
    log.error("오류가 발생하였습니다.", e);

    return new ErrorResponse(
        INTERNAL_SERVER_ERROR,
        INTERNAL_SERVER_ERROR.getDescription()
    );
  }

}