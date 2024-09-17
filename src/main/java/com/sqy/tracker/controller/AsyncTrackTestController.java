package com.sqy.tracker.controller;

import com.sqy.tracker.annotation.TrackAsyncTime;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "/api/trackAsync", produces = MediaType.APPLICATION_JSON_VALUE)
@TrackAsyncTime
public class AsyncTrackTestController {
    private static final Logger logger = LoggerFactory.getLogger(AsyncTrackTestController.class);

    @GetMapping("/test")
    @Operation(summary = "Из-за аннотации @TrackAsyncTime на классе выполняется ассинхронно," +
                         " запускает for-loop длящийся в среднем 3-5 секунд")
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
