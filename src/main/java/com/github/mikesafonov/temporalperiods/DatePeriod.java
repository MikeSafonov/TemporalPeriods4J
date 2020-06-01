package com.github.mikesafonov.temporalperiods;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;

/**
 * {@link TemporalPeriod} implementation with {@link LocalDate}
 *
 * @author Mike Safonov
 */
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public final class DatePeriod implements TemporalPeriod<LocalDate> {

    private final LocalDate from;
    private final LocalDate to;

    public static DatePeriod of(LocalDate from, LocalDate to) {
        return new DatePeriod(
            from, to
        );
    }

    public static DatePeriod of(int yearFrom, int monthFrom, int dayFrom, int yearTo, int monthTo, int dayTo) {
        return of(
            LocalDate.of(yearFrom, monthFrom, dayFrom),
            LocalDate.of(yearTo, monthTo, dayTo)
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getFrom() {
        return from;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocalDate getTo() {
        return to;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Period between() {
        return Period.between(from, to);
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
    public boolean isAfter(@NotNull TemporalPeriod<LocalDate> other) {
        return from.isAfter(other.getTo()) && to.isAfter(other.getTo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBefore(@NotNull TemporalPeriod<LocalDate> other) {
        return to.isBefore(other.getFrom()) && from.isBefore(other.getFrom());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(LocalDate point) {
        return !from.isAfter(point) && !to.isBefore(point);
    }
}
