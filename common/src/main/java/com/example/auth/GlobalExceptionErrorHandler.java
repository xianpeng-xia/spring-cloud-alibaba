package com.example.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @date 2020/12/17
 * @time 下午8:45
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionErrorHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorBody> error(SecurityException e) {
        log.warn("SecurityException ", e);
        ResponseEntity<ErrorBody> responseEntity = new ResponseEntity<ErrorBody>(
            ErrorBody.builder().status(HttpStatus.UNAUTHORIZED.value()).body(e.getMessage()).build(),
            HttpStatus.UNAUTHORIZED
        );
        return responseEntity;
    }


}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorBody {

    private String body;
    private int status;
}

