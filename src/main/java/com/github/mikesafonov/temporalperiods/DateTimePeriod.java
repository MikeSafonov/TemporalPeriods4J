package com.github.mikesafonov.temporalperiods;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * {@link TemporalPeriod} implementation with {@link LocalDateTime}
 *
 * @author Mike Safonov
 */
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public final class DateTimePeriod implements TemporalPeriod<LocalDateTime> {

    private final LocalDateTime from;
    private final LocalDateTime to;

    public static DateTimePeriod of(LocalDateTime from, LocalDateTime to) {
        return new DateTimePeriod(
            from, to
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Duration between() {
        return Duration.between(from, to);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReverse() {
        return to.isBefore(from);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAfter(@NotNull TemporalPeriod<LocalDateTime> other) {
        return from.isAfter(other.getTo()) && to.isAfter(other.getTo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBefore(@NotNull TemporalPeriod<LocalDateTime> other) {
        return to.isBefore(other.getFrom()) && from.isBefore(other.getFrom());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(LocalDateTime point) {
        return !from.isAfter(point) && !to.isBefore(point);
    }
}
