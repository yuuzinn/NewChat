package project.newchat.dto;

import lombok.*;
import project.newchat.type.ErrorCode;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
