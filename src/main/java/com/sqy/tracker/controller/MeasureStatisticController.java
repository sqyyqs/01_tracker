package com.sqy.tracker.controller;

import com.sqy.tracker.dto.MeasureStatistic;
import com.sqy.tracker.dto.MeasureType;
import com.sqy.tracker.service.MeasureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/measure", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "API статистики")
public class MeasureStatisticController {
    private static final Logger logger = LoggerFactory.getLogger(MeasureStatisticController.class);

    private final MeasureService measureService;

    public MeasureStatisticController(MeasureService measureService) {
        this.measureService = measureService;
    }

    @PostMapping("/statistic")
    @Operation(summary = "Получить статистику, тип статистики отправлять в body просто вида \"average\" без {}")
    public ResponseEntity<MeasureStatistic> measureStatistic(@RequestBody MeasureType measureType) {
        logger.debug("Invoke measureStatistic: {}", measureType);
        return measureService.getStatistic(measureType);
    }

}
