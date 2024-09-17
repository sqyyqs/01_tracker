package com.sqy.tracker.repository;

import com.sqy.tracker.domain.Measure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.EmptySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MeasureRepository {
    private static final Logger logger = LoggerFactory.getLogger(MeasureRepository.class);
    private static final String SQL_SELECT_TOTAL = """
            SELECT SUM(value)
            FROM measure
            WHERE is_successful
            """;
    private static final String SQL_SELECT_AVERAGE = """
            SELECT AVG(value)
            FROM measure
            WHERE is_successful
            """;
    private static final String SQL_INSERT_MEASURE = """
            INSERT INTO measure(value, is_successful)
            VALUES (:value, :status);
            """;
    private static final String SQL_SELECT_UNSUCCESSFUL_TOTAL = """
            SELECT COUNT(id)
            FROM measure
            WHERE is_successful = FALSE
            """;

    private final NamedParameterJdbcTemplate npjdbc;

    public MeasureRepository(NamedParameterJdbcTemplate npjdbc) {
        this.npjdbc = npjdbc;
    }

    public Optional<Double> getAverage() {
        return statisticQuery(SQL_SELECT_AVERAGE, "Invoke getAverage() with exception.");
    }

    public Optional<Double> getTotal() {
        return statisticQuery(SQL_SELECT_TOTAL, "Invoke getTotal() with exception.");
    }

    public Optional<Double> getTotalUnsuccessful() {
        return statisticQuery(SQL_SELECT_UNSUCCESSFUL_TOTAL, "Invoke getTotalUnsuccessful() with exception.");
    }

    public void saveMeasure(Measure measure) {
        npjdbc.update(SQL_INSERT_MEASURE, new MapSqlParameterSource()
                .addValue("value", measure.value())
                .addValue("status", measure.isSuccessful())
        );
    }

    private Optional<Double> statisticQuery(String query, String exceptionLogMessage) {
        try {
            return Optional.ofNullable(npjdbc.queryForObject(
                    query, EmptySqlParameterSource.INSTANCE, Double.TYPE));
        } catch (DataAccessException e) {
            logger.warn(exceptionLogMessage, e);
        }
        return Optional.empty();
    }
}
