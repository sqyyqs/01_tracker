package com.sqy.tracker.dto;

public record MeasureDto(
        double value,
        boolean isSuccessful
) {
    private static final int MILLIS_TO_SECONDS = 1000;

    public MeasureDto(long start, long end, boolean isSuccessful) {
        this(
            ((double) (end - start)) / MILLIS_TO_SECONDS,
            isSuccessful
        );
    }

}
