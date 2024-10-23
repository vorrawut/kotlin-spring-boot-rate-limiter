package com.example.ratelimiter.repository

import com.example.ratelimiter.model.RateLimitPolicy
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap

@Repository
class RateLimitRepository {

    private val requestCounts = ConcurrentHashMap<String, MutableList<Instant>>()

    fun isRequestAllowed(key: String, policy: RateLimitPolicy): Pair<Boolean, Long> {
        val now = Instant.now()
        val timestamps = requestCounts.computeIfAbsent(key) { mutableListOf() }
        timestamps.removeIf { it.isBefore(now.minusSeconds(policy.duration.toSeconds())) }

        return if (timestamps.size < policy.maxRequests) {
            timestamps.add(now)
            Pair(true, timestamps.size.toLong())
        } else {
            Pair(false, timestamps.size.toLong())
        }
    }
}
