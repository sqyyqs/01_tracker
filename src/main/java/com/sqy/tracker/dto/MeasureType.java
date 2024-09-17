package com.sqy.tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MeasureType {
    @JsonProperty("average") AVG,
    @JsonProperty("total") TOTAL,
    @JsonProperty("totalUnsuccessful") TOTAL_UNSUCCESSFUL
}
