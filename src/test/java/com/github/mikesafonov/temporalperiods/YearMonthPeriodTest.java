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
import java.util.stream.Stream;

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


}
