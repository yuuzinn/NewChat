package project.newchat.exception;

import lombok.*;
import project.newchat.type.ErrorCode;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class CustomException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public CustomException(ErrorCode errorCode,String message) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription() + " : " + message;
    }

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription() ;
    }
}