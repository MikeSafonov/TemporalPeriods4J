package com.github.mikesafonov.temporalperiods;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * {@link TemporalPeriod} implementation with {@link YearMonth}
 *
 * @author Mike Safonov
 */
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public final class YearMonthPeriod implements TemporalPeriod<YearMonth> {

    private final YearMonth from;
    private final YearMonth to;

    public static YearMonthPeriod of(YearMonth from, YearMonth to) {
        return new YearMonthPeriod(
            from, to
        );
    }

    public static YearMonthPeriod of(int yearFrom, int monthFrom, int yearTo, int monthTo) {
        return of(
            YearMonth.of(yearFrom, monthFrom),
            YearMonth.of(yearTo, monthTo)
        );
    }

    public static YearMonthPeriod from(LocalDate from, LocalDate to){
        return of(
            YearMonth.of(from.getYear(), from.getMonth()),
            YearMonth.of(to.getYear(), to.getMonth())
        );
    }

    public static YearMonthPeriod from(LocalDateTime from, LocalDateTime to){
        return of(
            YearMonth.of(from.getYear(), from.getMonth()),
            YearMonth.of(to.getYear(), to.getMonth())
        );
    }

    public static YearMonthPeriod from(DatePeriod datePeriod){
        return from(
            datePeriod.getFrom(),
            datePeriod.getTo()
        );
    }

    public static YearMonthPeriod from(DateTimePeriod dateTimePeriod){
        return from(
            dateTimePeriod.getFrom(),
            dateTimePeriod.getTo()
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public YearMonth getFrom() {
        return from;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public YearMonth getTo() {
        return to;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Period between() {

        return Period.between(from.atDay(1), to.atDay(1));
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
    public boolean isAfter(@NotNull TemporalPeriod<YearMonth> other) {
        return from.isAfter(other.getTo()) && to.isAfter(other.getTo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBefore(@NotNull TemporalPeriod<YearMonth> other) {
        return to.isBefore(other.getFrom()) && from.isBefore(other.getFrom());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(YearMonth point) {
        return !from.isAfter(point) && !to.isBefore(point);
    }

    /**
     * {@inheritDoc}
     * <p></p>Default step is {@link ChronoUnit#MONTHS}
     */
    @Override
    public boolean isSequentiallyWith(TemporalPeriod<YearMonth> other) {
        return isSequentiallyWith(other, ChronoUnit.MONTHS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemporalPeriod<YearMonth> combineWith(TemporalPeriod<YearMonth> other) {
        YearMonth newFrom = from.isBefore(other.getFrom()) ? from : other.getFrom();
        YearMonth newTo = to.isAfter(other.getTo()) ? to : other.getTo();
        return YearMonthPeriod.of(newFrom, newTo);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TemporalPeriod<YearMonth>[] split(YearMonth point) {
        if ((from.isBefore(point) && to.isAfter(point)) || to.equals(point)) {
            return new YearMonthPeriod[]{
                YearMonthPeriod.of(from, point.minus(1, ChronoUnit.MONTHS)),
                YearMonthPeriod.of(point, to)
            };
        }
        return new YearMonthPeriod[]{this};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<YearMonthPeriod> intersectionWith(@NotNull TemporalPeriod<YearMonth> other) {
        if (isIntersect(other)) {
            YearMonth intersectionFrom = contains(other.getFrom()) ? other.getFrom() : getFrom();
            YearMonth intersectionTo = contains(other.getTo()) ? other.getTo() : getTo();
            return Optional.of(YearMonthPeriod.of(intersectionFrom, intersectionTo));
        }
        return Optional.empty();
    }
}
