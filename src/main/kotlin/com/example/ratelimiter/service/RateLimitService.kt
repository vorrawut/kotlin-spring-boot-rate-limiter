package com.example.ratelimiter.service

import com.example.ratelimiter.exception.ErrorCode
import com.example.ratelimiter.exception.RateLimitExceedsException
import com.example.ratelimiter.model.RateLimitPolicy
import com.example.ratelimiter.repository.RateLimitRepository
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Service

@Service
class RateLimitService(
    private val registry: MeterRegistry,
    private val rateLimitRepository: RateLimitRepository // We'll define this next
) {

    suspend fun checkRequestExceedsThreshold(
        policies: List<RateLimitPolicy>,
        customerKey: String
    ): Map<RateLimitPolicy, Long> {
        require(customerKey.isNotEmpty()) { "Customer key cannot be empty" }
        val countMap = mutableMapOf<RateLimitPolicy, Long>()

        val isRequestDisallowed = policies.any { policy ->
            val (isAllowed, count) = rateLimitRepository.isRequestAllowed("${customerKey}_${policy.name}", policy)
            countMap[policy] = count
            !isAllowed
        }

        if (isRequestDisallowed) {
            registry.counter("rate_limit_violations", "policy", policies.first().name).increment()
            throw RateLimitExceedsException(
                "Rate limit exceeded for customer: [$customerKey]",
                errorCode = ErrorCode.RATE_LIMIT_EXCEED
            )
        }

        return countMap
    }
}
