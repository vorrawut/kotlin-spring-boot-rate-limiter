package com.example.ratelimiter.model

import java.time.Duration

data class RateLimitPolicy(
    val name: String,               // Policy name, e.g., "SimplePolicy"
    val maxRequests: Long,          // Maximum allowed requests
    val duration: Duration          // Duration for the rate limit, e.g., 1 minute
)