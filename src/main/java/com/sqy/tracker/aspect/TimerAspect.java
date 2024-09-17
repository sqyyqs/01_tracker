package com.sqy.tracker.aspect;

import com.sqy.tracker.dto.MeasureDto;
import com.sqy.tracker.service.MeasureService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Component
@Aspect
public class TimerAspect {
    private static final Logger logger = LoggerFactory.getLogger(TimerAspect.class);

    private final MeasureService measureService;

    public TimerAspect(MeasureService measureService) {
        this.measureService = measureService;
    }

    @Pointcut("execution(public * *(..)) && " +
            "(@within(com.sqy.tracker.annotation.TrackTime) || @annotation(com.sqy.tracker.annotation.TrackTime))")
    public void trackTimePointCut() {}

    @Pointcut("execution(public * *(..)) && " +
            "(@within(com.sqy.tracker.annotation.TrackAsyncTime) || @annotation(com.sqy.tracker.annotation.TrackAsyncTime))")
    public void trackAsyncTimePointCut() {}

    @Around("trackTimePointCut()")
    public Object takeMeasure(ProceedingJoinPoint joinPoint) {
        Object result = null;
        boolean status = true;
        long start = System.currentTimeMillis();

        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            logger.warn("Exception while proceeding!");
            status = false;
        }

        long end = System.currentTimeMillis();

        MeasureDto measureDto = new MeasureDto(start, end, status);
        measureService.saveMeasure(measureDto);

        return result;
    }

    @Around("trackAsyncTimePointCut()")
    public Object takeAsyncMeasure(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        return CompletableFuture.supplyAsync(() -> {
            Object result = null;
            boolean status = true;

            try {
                result = joinPoint.proceed();
            } catch (Throwable e) {
                logger.warn("Exception while proceeding!", e);
                status = false;
            }

            long end = System.currentTimeMillis();
            MeasureDto measureDto = new MeasureDto(start, end, status);

            measureService.saveMeasureAsync(measureDto);
            return result;
        }).exceptionally(e -> {
            long end = System.currentTimeMillis();
            boolean status = false;

            MeasureDto measureDto = new MeasureDto(start, end, status);

            measureService.saveMeasureAsync(measureDto);
            throw new CompletionException(e);
        });
    }
}
