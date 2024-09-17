package com.sqy.tracker.domain;

public record Measure(
        Long id,
        double value,
        boolean isSuccessful
) {
}
