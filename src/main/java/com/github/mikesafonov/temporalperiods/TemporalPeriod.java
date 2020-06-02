package com.github.mikesafonov.temporalperiods;

import javax.validation.constraints.NotNull;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;

/**
 * This is the base interface type for date, date-time and year-month periods.
 *
 * @author Mike Safonov
 */
public interface TemporalPeriod<T extends Temporal & Comparable<? super T>> {

    /**
     * @return periods start
     */
    @NotNull
    T getFrom();

    /**
     * @return periods end
     */
    @NotNull
    T getTo();

    /**
     * Periods duration. Implementation depends on {@link TemporalPeriod}`s implementation.
     *
     * @return periods duration
     */
    @NotNull
    TemporalAmount between();

    /**
     * @return true if {@code from} greater then {@code to}
     */
    boolean isReverse();

    /**
     * @return true if {@code from} not greater then {@code to}
     */
    default boolean isSequential() {
        return !isReverse();
    }

    /**
     * Checks if this period is after the specified period.
     * <p>
     * This checks to see if this period after the other period.
     * <pre>
     *   DatePeriod a = DatePeriod.of(2020, 1, 1, 2020, 1, 30);
     *   DatePeriod b = DatePeriod.of(2020, 1, 31, 2020, 2, 2);
     *   a.isAfter(b) == false
     *   a.isAfter(a) == false
     *   b.isAfter(a) == true
     * </pre>
     * <p>
     *
     * @param other the other period
     * @return true if this period is after the specified period
     */
    boolean isAfter(@NotNull TemporalPeriod<T> other);

    /**
     * Checks if this period is before the specified period.
     * <p>
     * This checks to see if this period before the other period.
     * <pre>
     *   DatePeriod a = DatePeriod.of(2020, 1, 1, 2020, 1, 30);
     *   DatePeriod b = DatePeriod.of(2020, 1, 31, 2020, 2, 2);
     *   a.isBefore(b) == true
     *   a.isBefore(a) == false
     *   b.isBefore(a) == true
     * </pre>
     * <p>
     *
     * @param other the other period
     * @return true if this period is before the specified period
     */
    boolean isBefore(@NotNull TemporalPeriod<T> other);

    /**
     * Checks if this period contains specified temporal point.
     * <p>
     * This checks to see if this period contains specified temporal point.
     * <pre>
     *   DatePeriod a = DatePeriod.of(2020, 1, 1, 2020, 1, 30);
     *   a.contains(LocalDate.of(2020, 1, 20)) == true
     *   a.contains(LocalDate.of(2020, 1, 30)) == true
     *   a.contains(LocalDate.of(2020, 1, 31)) == false
     * </pre>
     * <p>
     *
     * @param point the temporal point
     * @return true if this period contains specified temporal point
     */
    boolean contains(@NotNull T point);

    /**
     * Checks if this period`s start or end is equals to specified temporal point
     * <p>
     * This checks to see if specified temporal point at the borders of this period
     * <pre>
     *   DatePeriod a = DatePeriod.of(2020, 1, 1, 2020, 1, 30);
     *   a.isAtBorders(LocalDate.of(2020, 1, 20)) == false
     *   a.isAtBorders(LocalDate.of(2020, 1, 30)) == true
     *   a.isAtBorders(LocalDate.of(2020, 1, 1)) == true
     *   a.isAtBorders(LocalDate.of(2020, 1, 31)) == false
     * </pre>
     * <p>
     *
     * @param point the temporal point
     * @return true if this period`s start or end is equals to specified temporal point
     */
    default boolean isAtBorders(@NotNull T point) {
        return getFrom().equals(point) || getTo().equals(point);
    }

    /**
     * Checks if this period intersect the specified period.
     * <p>
     * This checks to see if this period intersect the other period.
     * <pre>
     *   DatePeriod a = DatePeriod.of(2020, 1, 1, 2020, 1, 30);
     *   a.isIntersect(DatePeriod.of(2020, 1, 31, 2020, 2, 2)) == false
     *   a.isIntersect(DatePeriod.of(2019, 1, 31, 2019, 2, 2)) == false
     *   a.isIntersect(a) == true
     *   a.isIntersect(DatePeriod.of(2020, 1, 20, 2020, 2, 2)) == true
     *   a.isIntersect(DatePeriod.of(2019, 1, 20, 2020, 1, 2)) == true
     * </pre>
     * <p>
     *
     * @param other the other period
     * @return true if this period intersect the specified period
     */
    default boolean isIntersect(@NotNull TemporalPeriod<T> other) {
        return !this.isAfter(other) && !this.isBefore(other);
    }

    /**
     * Checks if this period sequentially with the specified period (with default step).
     * <p>
     * This checks to see if this period sequentially with the other period.
     * <pre>
     *   DatePeriod a = DatePeriod.of(2020, 1, 1, 2020, 1, 30);
     *   a.isSequentiallyWith(DatePeriod.of(2019, 1, 1, 2019, 12, 31)) == true
     *   a.isSequentiallyWith(DatePeriod.of(2020, 1, 31, 2020, 2, 2)) == true
     *   DatePeriod.of(2020, 1, 31, 2020, 2, 2).isSequentiallyWith(a) == true
     *   a.isSequentiallyWith(DatePeriod.of(2019, 1, 31, 2019, 2, 2)) == false
     *   a.isSequentiallyWith(a) == false
     *   a.isSequentiallyWith(DatePeriod.of(2020, 1, 20, 2020, 2, 2)) == false
     *   a.isSequentiallyWith(DatePeriod.of(2019, 1, 20, 2020, 1, 2)) == false
     * </pre>
     * <p>
     * <p>
     * Default step is depends on implementation.
     *
     * @param other the other period
     * @return true if this period sequentially with the specified period
     */
    boolean isSequentiallyWith(@NotNull TemporalPeriod<T> other);

    /**
     * Checks if this period sequentially with the specified period with specified step.
     * <p>
     * This checks to see if this period sequentially with the other period.
     * <pre>
     *   DatePeriod a = DatePeriod.of(2020, 1, 1, 2020, 1, 30);
     *   a.isSequentiallyWith(DatePeriod.of(2019, 1, 1, 2019, 12, 31), ChronoUnit.DAYS) == true
     *   a.isSequentiallyWith(DatePeriod.of(2020, 1, 31, 2020, 2, 2), ChronoUnit.DAYS) == true
     *   DatePeriod.of(2020, 1, 31, 2020, 2, 2).isSequentiallyWith(a, ChronoUnit.DAYS) == true
     *   DatePeriod.of(2020, 1, 31, 2020, 2, 2).isSequentiallyWith(a, ChronoUnit.MONTHS) == false
     * </pre>
     * <p>
     *
     * @param other the other period
     * @param step  temporal unit step
     * @return true if this period sequentially with the specified period
     */
    default boolean isSequentiallyWith(@NotNull TemporalPeriod<T> other, @NotNull TemporalUnit step) {
        return getTo().plus(1, step).equals(other.getFrom()) ||
            getFrom().minus(1, step).equals(other.getTo());
    }

    /**
     * Creates new period from {@code min(this.from, other.from)} to {@code max(this.to, other.to)}
     * <p>
     * Examples:
     * <pre>
     *     DatePeriod.of(2020, 1, 1, 2020, 1, 30).combineWith(DatePeriod.of(2019, 1, 1, 2020, 1, 20)) ==
     *      2019-01-01 -- 2020-01-30
     * </pre>
     * <p>
     *
     * @param other the other period
     * @return new period from this and the other periods
     */
    @NotNull
    TemporalPeriod<T> combineWith(@NotNull TemporalPeriod<T> other);

    /**
     * Splits this period around specified point.
     * <p></p>
     * Split date period '2020-01-01 -- 2020-01-31' :
     *
     * <table >
     *   <tr>
     *     <td> point </td>  <td> result </td>
     *   </tr>
     *   <tr>
     *     <td> 2020-01-01 </td>  <td> {2020-01-01 -- 2020-01-31}</td>
     *   </tr>
     *   <tr>
     *     <td> 2020-01-31 </td>  <td> {2020-01-01 -- 2020-01-30, 2020-01-31 -- 2020-01-31}</td>
     *   </tr>
     *   <tr>
     *     <td> 2020-01-10 </td>  <td> {2020-01-01 -- 2020-01-09, 2020-01-10 -- 2020-01-31}</td>
     *   </tr>
     *   <tr>
     *     <td> 2020-02-01 </td>  <td> {2020-01-01 -- 2020-01-31}</td>
     *   </tr>
     * </table>
     *
     * @param point the temporal splitting point
     * @return the array of periods computed by splitting this period by specified point
     */
    @NotNull
    TemporalPeriod<T>[] split(@NotNull T point);

}
