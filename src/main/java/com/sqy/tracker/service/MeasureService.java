package com.sqy.tracker.service;

import com.sqy.tracker.domain.Measure;
import com.sqy.tracker.dto.MeasureType;
import com.sqy.tracker.dto.MeasureDto;
import com.sqy.tracker.dto.MeasureStatistic;
import com.sqy.tracker.repository.MeasureRepository;
import com.sqy.tracker.service.mapper.MeasureMapper;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class MeasureService {
    private static final Logger logger = LoggerFactory.getLogger(MeasureService.class);

    private final MeasureRepository measureRepository;
    private final MeasureMapper measureMapper;
    private final ExecutorService executorService;

    public MeasureService(MeasureRepository measureRepository, MeasureMapper measureMapper) {
        this.measureRepository = measureRepository;
        this.measureMapper = measureMapper;
        this.executorService = Executors.newCachedThreadPool();
    }

    public void saveMeasure(MeasureDto measureDto) {
        logger.debug("Invoke saveMeasure({}).", measureDto);
        Measure measure = measureMapper.toEntity(measureDto);
        measureRepository.saveMeasure(measure);
    }

    public void saveMeasureAsync(MeasureDto measureDto) {
        logger.debug("Invoke saveMeasureAsync({}).", measureDto);
        executorService.submit(() -> {
            Measure measure = measureMapper.toEntity(measureDto);
            measureRepository.saveMeasure(measure);
        });
    }

    public ResponseEntity<MeasureStatistic> getStatistic(MeasureType measureType) {
        logger.debug("Invoke getStatistic({}).", measureType);

        Optional<Double> result = switch (measureType) {
            case AVG -> measureRepository.getAverage();
            case TOTAL -> measureRepository.getTotal();
            case TOTAL_UNSUCCESSFUL -> measureRepository.getTotalUnsuccessful();
            case null, default -> throw new IllegalArgumentException("Unrecognized measure statistic type: " + measureType);
        };

        return result.map(value -> new MeasureStatistic(measureType, value))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PreDestroy
    public void shutdownExecutorService() {
        executorService.shutdown();
    }
}
