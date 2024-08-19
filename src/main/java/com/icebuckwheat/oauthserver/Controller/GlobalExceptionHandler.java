package com.icebuckwheat.oauthserver.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

//    @ExceptionHandler(FeignException.class)
//    public ResponseEntity<Object> handleFeignException(FeignException e) {
//        if(HttpStatus.BAD_REQUEST.value() == e.status()) {
//            return new ResponseEntity<>("실패 사유를 아는 실패 : " + e.contentUTF8(), HttpStatus.BAD_REQUEST);
//        } else {
//            return new ResponseEntity<>("실패 사유를 모르는 실패", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
