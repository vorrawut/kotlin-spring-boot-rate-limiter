package com.example.ratelimiter.exception

class RateLimitExceedsException(
    message: String,
    val errorCode: ErrorCode
) : RuntimeException(message)
