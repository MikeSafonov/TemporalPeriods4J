package com.github.mikesafonov.temporalperiods;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mike Safonov
 */
public class DateTimePeriodTest {

    @Nested
    class Creation {

        @Test
        void shouldCreatePeriodFromTwoDates() {
            LocalDateTime from = LocalDateTime.of(2020, 1, 1, 0, 1, 1);
            LocalDateTime to = LocalDateTime.of(2020, 2, 1, 0, 1, 1);

            DateTimePeriod period = new DateTimePeriod(from, to);
            DateTimePeriod ofPeriod = DateTimePeriod.of(from, to);

            assertEquals(from, period.getFrom());
            assertEquals(to, period.getTo());
            assertEquals(period, ofPeriod);
        }
    }

    @Nested
    class Between {

        @Test
        void shouldReturnExpectedPeriod() {
            LocalDateTime from = LocalDateTime.of(2020, 1, 1, 0, 1, 1);
            LocalDateTime to = LocalDateTime.of(2020, 1, 1, 1, 1, 1);

            DateTimePeriod period = new DateTimePeriod(from, to);
            Duration between = period.between();
            assertEquals(1, between.toHours());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsReverse {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                    LocalDateTime.of(2020, 1, 1, 1, 0, 0),
                    false),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 31, 1, 0, 0),
                    LocalDateTime.of(2020, 1, 31, 1, 0, 0),
                    false),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 1, 5, 0, 0),
                    LocalDateTime.of(2020, 1, 1, 1, 0, 0),
                    true)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(LocalDateTime from, LocalDateTime to, boolean expected) {
            DateTimePeriod period = new DateTimePeriod(from, to);
            assertEquals(expected, period.isReverse());
        }

    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsSequential {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                    LocalDateTime.of(2020, 1, 1, 1, 0, 0),
                    true),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 31, 1, 0, 0),
                    LocalDateTime.of(2020, 1, 31, 1, 0, 0),
                    true),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 1, 5, 0, 0),
                    LocalDateTime.of(2020, 1, 1, 1, 0, 0),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(LocalDateTime from, LocalDateTime to, boolean expected) {
            DateTimePeriod period = new DateTimePeriod(from, to);
            assertEquals(expected, period.isSequential());
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsAfter {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2019, 1, 31, 0, 0, 0)
                    ),
                    true),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 20, 0, 0, 0)
                    ),
                    false),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 20, 0, 0, 0)
                    ),
                    false),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 2, 20, 0, 0, 0)
                    ),
                    false),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2021, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2021, 2, 20, 0, 0, 0)
                    ),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(DateTimePeriod one, DateTimePeriod two, boolean expected) {
            assertEquals(expected, one.isAfter(two));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsBefore {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2019, 1, 31, 0, 0, 0)
                    ),
                    false),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 20, 0, 0, 0)
                    ),
                    false),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 20, 0, 0, 0)
                    ),
                    false),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 2, 20, 0, 0, 0)
                    ),
                    false),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2021, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2021, 2, 20, 0, 0, 0)
                    ),
                    true)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(DateTimePeriod one, DateTimePeriod two, boolean expected) {
            assertEquals(expected, one.isBefore(two));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Contains {

        private DateTimePeriod period = DateTimePeriod.of(
            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
            LocalDateTime.of(2020, 1, 31, 0, 0, 0)
        );

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                    true),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 31, 0, 0, 0),
                    true),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 20, 0, 0, 0),
                    true),
                Arguments.of(
                    LocalDateTime.of(2019, 1, 31, 0, 0, 0),
                    false),
                Arguments.of(
                    LocalDateTime.of(2020, 2, 1, 0, 0, 0),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(LocalDateTime point, boolean expected) {
            assertEquals(expected, period.contains(point));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsAtBorders {

        private DateTimePeriod period = DateTimePeriod.of(
            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
            LocalDateTime.of(2020, 1, 31, 0, 0, 0)
        );

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                    true),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 31, 0, 0, 0),
                    true),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 20, 0, 0, 0),
                    false),
                Arguments.of(
                    LocalDateTime.of(2019, 1, 31, 0, 0, 0),
                    false),
                Arguments.of(
                    LocalDateTime.of(2020, 2, 1, 0, 0, 0),
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(LocalDateTime point, boolean expected) {
            assertEquals(expected, period.isAtBorders(point));
        }
    }


}