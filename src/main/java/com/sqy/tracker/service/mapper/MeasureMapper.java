package com.sqy.tracker.service.mapper;

import com.sqy.tracker.domain.Measure;
import com.sqy.tracker.dto.MeasureDto;
import org.springframework.stereotype.Component;

@Component
public class MeasureMapper {

    public MeasureDto toDto(Measure measure) {
        return new MeasureDto(measure.value(), measure.isSuccessful());
    }

    public Measure toEntity(MeasureDto measureDto) {
        return new Measure(null, measureDto.value(), measureDto.isSuccessful());
    }
}
