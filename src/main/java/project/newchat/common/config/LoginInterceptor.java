package project.newchat.common.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import project.newchat.common.exception.CustomException;
import project.newchat.common.type.ErrorCode;

@Component
public class LoginInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    // 요청한 메서드에 LoginCheck 어노테이션이 있는지 확인
    if (handler instanceof HandlerMethod && ((HandlerMethod) handler).hasMethodAnnotation(LoginCheck.class)) {
      HttpSession session = request.getSession();
      if (session == null || session.getAttribute("user") == null) {
        throw new CustomException(ErrorCode.NOT_LOGIN);
      }
    }
    return true;
  }
}
