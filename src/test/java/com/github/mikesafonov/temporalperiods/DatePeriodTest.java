package com.github.mikesafonov.temporalperiods;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mike Safonov
 */
public class DatePeriodTest {

    @Nested
    class Creation {

        @Test
        void shouldCreatePeriodFromTwoDates() {
            LocalDate from = LocalDate.of(2020, 1, 1);
            LocalDate to = LocalDate.of(2020, 2, 1);

            DatePeriod period = new DatePeriod(from, to);
            DatePeriod ofPeriod = DatePeriod.of(from, to);

            assertEquals(from, period.getFrom());
            assertEquals(to, period.getTo());
            assertEquals(period, ofPeriod);
        }

        @Test
        void shouldCreateFromYearMonthDate() {
            LocalDate from = LocalDate.of(2020, 1, 1);
            LocalDate to = LocalDate.of(2020, 2, 1);

            DatePeriod period = DatePeriod.of(2020, 1, 1, 2020, 2, 1);

            assertEquals(from, period.getFrom());
            assertEquals(to, period.getTo());
        }
    }

    @Nested
    class Between {

        @Test
        void shouldReturnExpectedPeriod() {
            DatePeriod period = DatePeriod.of(2020, 1, 1, 2020, 2, 1);
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
                    LocalDate.of(2020, 1, 1),
                    LocalDate.of(2020, 1, 31),
                    false),
                Arguments.of(
                    LocalDate.of(2020, 1, 31),
                    LocalDate.of(2020, 1, 31),
                    false),
                Arguments.of(
                    LocalDate.of(2020, 2, 1),
                    LocalDate.of(2020, 1, 31),
                    true)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(LocalDate from, LocalDate to, boolean expected) {
            DatePeriod period = new DatePeriod(from, to);
            assertEquals(expected, period.isReverse());
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsSequential {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    LocalDate.of(2020, 1, 1),
                    LocalDate.of(2020, 1, 31),
                    true),
                Arguments.of(
                    LocalDate.of(2020, 1, 31),
                    LocalDate.of(2020, 1, 31),
                    true),
                Arguments.of(
                    LocalDate.of(2020, 2, 1),
                    LocalDate.of(2020, 1, 31),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(LocalDate from, LocalDate to, boolean expected) {
            DatePeriod period = new DatePeriod(from, to);
            assertEquals(expected, period.isSequential());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsAfter {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2019, 1, 1, 2019, 1, 31),
                    true),
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2020, 1, 1, 2020, 1, 20),
                    false),
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2019, 1, 1, 2020, 1, 20),
                    false),
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2019, 1, 1, 2020, 2, 20),
                    false),
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2021, 1, 1, 2021, 2, 20),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(DatePeriod one, DatePeriod two, boolean expected) {
            assertEquals(expected, one.isAfter(two));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsBefore {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2019, 1, 1, 2019, 1, 31),
                    false),
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2020, 1, 1, 2020, 1, 20),
                    false),
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2019, 1, 1, 2020, 1, 20),
                    false),
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2019, 1, 1, 2020, 2, 20),
                    false),
                Arguments.of(
                    DatePeriod.of(2020, 1, 1, 2020, 1, 31),
                    DatePeriod.of(2021, 1, 1, 2021, 2, 20),
                    true)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(DatePeriod one, DatePeriod two, boolean expected) {
            assertEquals(expected, one.isBefore(two));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Contains {

        private DatePeriod period = DatePeriod.of(
            2020, 1, 1,
            2020, 1, 31
        );

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    LocalDate.of(2020, 1, 1),
                    true),
                Arguments.of(
                    LocalDate.of(2020, 1, 31),
                    true),
                Arguments.of(
                    LocalDate.of(2020, 1, 20),
                    true),
                Arguments.of(
                    LocalDate.of(2019, 1, 31),
                    false),
                Arguments.of(
                    LocalDate.of(2020, 2, 1),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(LocalDate point, boolean expected) {
            assertEquals(expected, period.contains(point));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsAtBorders {

        private DatePeriod period = DatePeriod.of(
            2020, 1, 1,
            2020, 1, 31
        );

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    LocalDate.of(2020, 1, 1),
                    true),
                Arguments.of(
                    LocalDate.of(2020, 1, 31),
                    true),
                Arguments.of(
                    LocalDate.of(2020, 1, 20),
                    false),
                Arguments.of(
                    LocalDate.of(2019, 1, 31),
                    false),
                Arguments.of(
                    LocalDate.of(2020, 2, 1),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(LocalDate point, boolean expected) {
            assertEquals(expected, period.isAtBorders(point));
        }
    }


}
