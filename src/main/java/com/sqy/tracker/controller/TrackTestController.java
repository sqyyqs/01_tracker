package com.sqy.tracker.controller;

import com.sqy.tracker.annotation.TrackTime;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "/api/track", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrackTestController {
    private static final Logger logger = LoggerFactory.getLogger(TrackTestController.class);

    @GetMapping("/test")
    @TrackTime
    @Operation(summary = "Запускает for-loop длящийся в среднем 3-5 секунд")
    public void test() {
        long sum = 0;
        int jMax = ThreadLocalRandom.current().nextInt(5_000, 10_000);
        for (int idx = 0; idx < 10_000_00; idx++) {
            for (int jdx = 0; jdx < jMax; jdx++) {
                sum += idx;
            }
        }
        logger.info("Calculations stopped. Sum: {}.", sum);
    }

}
