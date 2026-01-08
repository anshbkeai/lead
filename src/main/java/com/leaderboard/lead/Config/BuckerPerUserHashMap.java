package com.leaderboard.lead.Config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BuckerPerUserHashMap {

    private final Map<String,Bucket> map= new ConcurrentHashMap<>();

    public boolean isAllowed(String userId) {
        Bucket bucket = map.computeIfAbsent(userId, this::newBucket);
        return bucket.tryConsume(1);
    }

    public Bucket newBucket(String userid) {
        log.info("Creating  the Bucket for the User id {}" , userid);
        Bucket bucket = Bucket.builder()
                            .addLimit(Bandwidth.classic(2, Refill.intervally(2, Duration.ofMinutes(2))))
                            .build();
        return bucket;
    }
}
