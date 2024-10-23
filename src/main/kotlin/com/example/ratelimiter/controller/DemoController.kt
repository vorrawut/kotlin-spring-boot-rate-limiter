package com.example.ratelimiter.controller

import com.example.ratelimiter.model.RateLimitPolicy
import com.example.ratelimiter.service.RateLimitService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

@RestController
@RequestMapping("/api")
class DemoController(private val rateLimitService: RateLimitService
) {

//    @RateLimit(maxRequests = 5, durationInMinutes = 1)
    @GetMapping("/limited")
    suspend fun limitedEndpoint(): ResponseEntity<String> {
        // Use customerKey based on context (JWT, session ID, etc.)
        val customerKey = "unique-customer-key"
        val policies = listOf(RateLimitPolicy("StrictPolicy", 5, Duration.ofMinutes(1)))
        rateLimitService.checkRequestExceedsThreshold(policies, customerKey)

        return ResponseEntity.ok("You've successfully accessed a rate-limited endpoint!")
    }
}
