package com.example.ratelimiter.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RateLimitExceedsException::class)
    fun handleRateLimitExceedsException(ex: RateLimitExceedsException): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.TOO_MANY_REQUESTS)
            .body("Error: ${ex.message}, Code: ${ex.errorCode}")
    }
}
