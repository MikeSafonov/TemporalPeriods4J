package com.github.mikesafonov.temporalperiods;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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

    public static DateTimePeriod from(DatePeriod period, LocalTime time){
        return of(
            period.getFrom().atTime(time),
            period.getTo().atTime(time)
        );
    }

    public static DateTimePeriod from(DatePeriod period){
        return of(
            period.getFrom().atStartOfDay(),
            period.getTo().atStartOfDay()
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

    /**
     * {@inheritDoc}
     * <p></p>Default step is {@link ChronoUnit#SECONDS}
     */
    @Override
    public boolean isSequentiallyWith(TemporalPeriod<LocalDateTime> other) {
        return isSequentiallyWith(other, ChronoUnit.SECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemporalPeriod<LocalDateTime> combineWith(TemporalPeriod<LocalDateTime> other) {
        LocalDateTime newFrom = from.isBefore(other.getFrom()) ? from : other.getFrom();
        LocalDateTime newTo = to.isAfter(other.getTo()) ? to : other.getTo();
        return DateTimePeriod.of(newFrom, newTo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DateTimePeriod[] split(LocalDateTime point) {
        if ((from.isBefore(point) && to.isAfter(point)) || to.equals(point)) {
            return new DateTimePeriod[]{
                DateTimePeriod.of(from, point.minus(1, ChronoUnit.SECONDS)),
                DateTimePeriod.of(point, to)
            };
        }
        return new DateTimePeriod[]{this};
    }
}
