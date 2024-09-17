package com.sqy.tracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "ДТО статистики измерений")
public record MeasureStatistic(
        @Schema(description = "Тип статистики")
        MeasureType type,
        @Schema(description = "Значение")
        double value
){
}
