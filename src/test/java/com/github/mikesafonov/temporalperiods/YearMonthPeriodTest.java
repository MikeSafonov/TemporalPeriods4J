package com.github.mikesafonov.temporalperiods;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mike Safonov
 */
public class YearMonthPeriodTest {

    @Nested
    class Creation {

        @Test
        void shouldCreatePeriodFromTwoDates() {
            YearMonth from = YearMonth.of(2020, 1);
            YearMonth to = YearMonth.of(2020, 2);

            YearMonthPeriod period = new YearMonthPeriod(from, to);
            YearMonthPeriod ofPeriod = YearMonthPeriod.of(from, to);

            assertEquals(from, period.getFrom());
            assertEquals(to, period.getTo());
            assertEquals(period, ofPeriod);
        }

        @Test
        void shouldCreateFromYearMonth() {
            YearMonth from = YearMonth.of(2020, 1);
            YearMonth to = YearMonth.of(2020, 2);

            YearMonthPeriod period = YearMonthPeriod.of(2020, 1, 2020, 2);

            assertEquals(from, period.getFrom());
            assertEquals(to, period.getTo());
        }

        @Test
        void shouldCreateFromLocalDate() {
            LocalDate from = LocalDate.of(2020, 1, 1);
            LocalDate to = LocalDate.of(2020, 2, 2);

            YearMonthPeriod period = YearMonthPeriod.from(from, to);
            assertEquals(YearMonth.of(2020, 1), period.getFrom());
            assertEquals(YearMonth.of(2020, 2), period.getTo());
        }

        @Test
        void shouldCreateFromLocalDateTime() {
            LocalDateTime from = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
            LocalDateTime to = LocalDateTime.of(2020, 2, 2, 0, 0, 0);

            YearMonthPeriod period = YearMonthPeriod.from(from, to);
            assertEquals(YearMonth.of(2020, 1), period.getFrom());
            assertEquals(YearMonth.of(2020, 2), period.getTo());
        }

        @Test
        void shouldCreateFromDatePeriod() {
            DatePeriod timePeriod = DatePeriod.of(
                2020, 1, 1,
                2020, 2, 2
            );
            YearMonthPeriod period = YearMonthPeriod.from(timePeriod);
            assertEquals(YearMonth.of(2020, 1), period.getFrom());
            assertEquals(YearMonth.of(2020, 2), period.getTo());
        }

        @Test
        void shouldCreateFromDateTimePeriod() {
            DateTimePeriod timePeriod = DateTimePeriod.of(
                LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                LocalDateTime.of(2020, 2, 2, 0, 0, 0)
            );
            YearMonthPeriod period = YearMonthPeriod.from(timePeriod);
            assertEquals(YearMonth.of(2020, 1), period.getFrom());
            assertEquals(YearMonth.of(2020, 2), period.getTo());
        }
    }

    @Nested
    class Between {

        @Test
        void shouldReturnExpectedPeriod() {
            YearMonthPeriod period = YearMonthPeriod.of(2020, 1, 2020, 2);
            Period between = period.between();
            assertEquals(1, between.getMonths());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsReverse {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonth.of(2020, 1),
                    YearMonth.of(2020, 2),
                    false),
                Arguments.of(
                    YearMonth.of(2020, 1),
                    YearMonth.of(2020, 1),
                    false),
                Arguments.of(
                    YearMonth.of(2020, 2),
                    YearMonth.of(2020, 1),
                    true)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonth from, YearMonth to, boolean expected) {
            YearMonthPeriod period = new YearMonthPeriod(from, to);
            assertEquals(expected, period.isReverse());
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsSequential {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonth.of(2020, 1),
                    YearMonth.of(2020, 2),
                    true),
                Arguments.of(
                    YearMonth.of(2020, 1),
                    YearMonth.of(2020, 1),
                    true),
                Arguments.of(
                    YearMonth.of(2020, 2),
                    YearMonth.of(2020, 1),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonth from, YearMonth to, boolean expected) {
            YearMonthPeriod period = new YearMonthPeriod(from, to);
            assertEquals(expected, period.isSequential());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsAfter {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2019, 1),
                    true),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2020, 1),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2020, 2),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2021, 1, 2021, 2),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonthPeriod one, YearMonthPeriod two, boolean expected) {
            assertEquals(expected, one.isAfter(two));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsBefore {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2019, 1),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2020, 1),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2020, 2),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2021, 1, 2021, 2),
                    true)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonthPeriod one, YearMonthPeriod two, boolean expected) {
            assertEquals(expected, one.isBefore(two));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Contains {

        private YearMonthPeriod period = YearMonthPeriod.of(
            2020, 1,
            2020, 3
        );

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonth.of(2020, 1),
                    true),
                Arguments.of(
                    YearMonth.of(2020, 3),
                    true),
                Arguments.of(
                    YearMonth.of(2020, 2),
                    true),
                Arguments.of(
                    YearMonth.of(2019, 1),
                    false),
                Arguments.of(
                    YearMonth.of(2020, 4),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonth point, boolean expected) {
            assertEquals(expected, period.contains(point));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsAtBorders {

        private YearMonthPeriod period = YearMonthPeriod.of(
            2020, 1,
            2020, 3
        );

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonth.of(2020, 1),
                    true),
                Arguments.of(
                    YearMonth.of(2020, 3),
                    true),
                Arguments.of(
                    YearMonth.of(2020, 2),
                    false),
                Arguments.of(
                    YearMonth.of(2019, 4),
                    false),
                Arguments.of(
                    YearMonth.of(2020, 4),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonth point, boolean expected) {
            assertEquals(expected, period.isAtBorders(point));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsIntersect {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2019, 1),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    true),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2020, 1),
                    true),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2020, 2),
                    true),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2021, 1, 2021, 2),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonthPeriod one, YearMonthPeriod two, boolean expected) {
            assertEquals(expected, one.isIntersect(two));
            assertEquals(expected, two.isIntersect(one));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsSequentiallyWithDefault {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2019, 1),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2019, 12),
                    true),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 2, 2020, 3),
                    true),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2021, 1, 2021, 2),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonthPeriod one, YearMonthPeriod two, boolean expected) {
            assertEquals(expected, one.isSequentiallyWith(two));
            assertEquals(expected, two.isSequentiallyWith(one));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsSequentiallyWith {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2019, 1),
                    ChronoUnit.YEARS,
                    true),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    ChronoUnit.YEARS,
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 12, 2021, 1),
                    YearMonthPeriod.of(2019, 1, 2019, 12),
                    ChronoUnit.YEARS,
                    true),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 2, 2020, 3),
                    ChronoUnit.YEARS,
                    false),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2021, 1, 2021, 2),
                    ChronoUnit.YEARS,
                    true)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonthPeriod one, YearMonthPeriod two, TemporalUnit step, boolean expected) {
            assertEquals(expected, one.isSequentiallyWith(two, step));
            assertEquals(expected, two.isSequentiallyWith(one, step));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class CombineWith {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2019, 1, 2019, 1),
                    YearMonthPeriod.of(2019, 1, 2020, 1)
                ),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 1, 2020, 1)
                ),
                Arguments.of(
                    YearMonthPeriod.of(2020, 12, 2021, 1),
                    YearMonthPeriod.of(2019, 1, 2019, 12),
                    YearMonthPeriod.of(2019, 1, 2021, 1)
                ),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2020, 2, 2020, 3),
                    YearMonthPeriod.of(2020, 1, 2020, 3)
                ),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 1),
                    YearMonthPeriod.of(2021, 1, 2021, 2),
                    YearMonthPeriod.of(2020, 1, 2021, 2)
                )
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonthPeriod one, YearMonthPeriod two, YearMonthPeriod expected) {
            assertEquals(expected, one.combineWith(two));
            assertEquals(expected, two.combineWith(one));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Split {

        private YearMonthPeriod period = YearMonthPeriod.of(2020, 1, 2020, 3);

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonth.of(2020, 1),
                    new YearMonthPeriod[]{period}
                ),
                Arguments.of(
                    YearMonth.of(2020, 3),
                    new YearMonthPeriod[]{
                        YearMonthPeriod.of(2020, 1, 2020, 2),
                        YearMonthPeriod.of(2020, 3, 2020, 3)
                    }
                ),
                Arguments.of(
                    YearMonth.of(2020, 2),
                    new YearMonthPeriod[]{
                        YearMonthPeriod.of(2020, 1, 2020, 1),
                        YearMonthPeriod.of(2020, 2, 2020, 3)
                    }
                ),
                Arguments.of(
                    YearMonth.of(2020, 4),
                    new YearMonthPeriod[]{period}
                ),
                Arguments.of(
                    YearMonth.of(2019, 12),
                    new YearMonthPeriod[]{period}
                )
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonth point, YearMonthPeriod[] expected) {
            assertThat(period.split(point)).containsOnly(expected);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IntersectionWith {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 4),
                    YearMonthPeriod.of(2020, 2, 2020, 3),
                    Optional.of(YearMonthPeriod.of(2020, 2, 2020, 3))
                ),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 4),
                    YearMonthPeriod.of(2020, 2, 2020, 5),
                    Optional.of(YearMonthPeriod.of(2020, 2, 2020, 4))
                ),
                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 4),
                    YearMonthPeriod.of(2020, 4, 2020, 5),
                    Optional.of(YearMonthPeriod.of(2020, 4, 2020, 4))
                ),

                Arguments.of(
                    YearMonthPeriod.of(2020, 1, 2020, 3),
                    YearMonthPeriod.of(2020, 4, 2020, 5),
                    Optional.empty()
                )
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(YearMonthPeriod first, YearMonthPeriod second, Optional<YearMonthPeriod> expected) {
            assertThat(first.intersectionWith(second)).isEqualTo(expected);
            assertThat(second.intersectionWith(first)).isEqualTo(expected);
        }
    }
}
