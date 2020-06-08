package com.github.mikesafonov.temporalperiods;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
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

        @Test
        void shouldCreateFromDatePeriodWithTime() {
            DatePeriod datePeriod = DatePeriod.of(2020, 1, 1, 2020, 1, 2);
            LocalTime time = LocalTime.of(2, 1, 0);
            DateTimePeriod period = DateTimePeriod.from(datePeriod, time);

            assertEquals(LocalDateTime.of(2020, 1, 1, 2, 1, 0), period.getFrom());
            assertEquals(LocalDateTime.of(2020, 1, 2, 2, 1, 0), period.getTo());
        }

        @Test
        void shouldCreateFromDatePeriodAtStartOfDay() {
            DatePeriod datePeriod = DatePeriod.of(2020, 1, 1, 2020, 1, 2);
            DateTimePeriod period = DateTimePeriod.from(datePeriod);

            assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0, 0), period.getFrom());
            assertEquals(LocalDateTime.of(2020, 1, 2, 0, 0, 0), period.getTo());
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


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsIntersect {

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
                    true),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 20, 0, 0, 0)
                    ),
                    true),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 2, 20, 0, 0, 0)
                    ),
                    true),
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
                        LocalDateTime.of(2020, 1, 31, 0, 0, 1),
                        LocalDateTime.of(2020, 2, 20, 0, 0, 0)
                    ),
                    true),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2019, 12, 31, 23, 59, 59)
                    ),
                    true),
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
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2019, 1, 31, 0, 0, 0)
                    ),
                    ChronoUnit.MINUTES,
                    false),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 31, 0, 1, 0),
                        LocalDateTime.of(2020, 2, 20, 0, 0, 0)
                    ),
                    ChronoUnit.MINUTES,
                    true),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2019, 12, 31, 23, 59, 0)
                    ),
                    ChronoUnit.MINUTES,
                    true),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2021, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2021, 2, 20, 0, 0, 0)
                    ),
                    ChronoUnit.MINUTES,
                    false)
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(DateTimePeriod one, DateTimePeriod two, TemporalUnit step, boolean expected) {
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
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2019, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    )
                ),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 31, 0, 1, 0),
                        LocalDateTime.of(2020, 2, 20, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 2, 20, 0, 0, 0)
                    )
                ),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2019, 12, 31, 23, 59, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2019, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    )
                ),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 31, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2021, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2021, 2, 20, 0, 0, 0)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2021, 2, 20, 0, 0, 0)
                    )
                )
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(DateTimePeriod one, DateTimePeriod two, DateTimePeriod expected) {
            assertEquals(expected, one.combineWith(two));
            assertEquals(expected, two.combineWith(one));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Split {

        private DateTimePeriod period = DateTimePeriod.of(
            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
            LocalDateTime.of(2020, 1, 31, 23, 59, 59)
        );

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                    new DateTimePeriod[]{period}
                ),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 31, 23, 59, 59),
                    new DateTimePeriod[]{
                        DateTimePeriod.of(
                            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                            LocalDateTime.of(2020, 1, 31, 23, 59, 58)
                        ),
                        DateTimePeriod.of(
                            LocalDateTime.of(2020, 1, 31, 23, 59, 59),
                            LocalDateTime.of(2020, 1, 31, 23, 59, 59)
                        )
                    }
                ),
                Arguments.of(
                    LocalDateTime.of(2020, 1, 10, 0, 0, 0),
                    new DateTimePeriod[]{
                        DateTimePeriod.of(
                            LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                            LocalDateTime.of(2020, 1, 9, 23, 59, 59)
                        ),
                        DateTimePeriod.of(
                            LocalDateTime.of(2020, 1, 10, 0, 0, 0),
                            LocalDateTime.of(2020, 1, 31, 23, 59, 59)
                        )
                    }
                ),
                Arguments.of(
                    LocalDateTime.of(2020, 2, 1, 0, 0, 0),
                    new DateTimePeriod[]{period}
                ),
                Arguments.of(
                    LocalDateTime.of(2019, 12, 31, 23, 59, 59),
                    new DateTimePeriod[]{period}
                )
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(LocalDateTime point, DateTimePeriod[] expected) {
            assertThat(period.split(point)).containsOnly(expected);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IntersectionWith {

        private Stream<Arguments> datePeriodsProvider() {
            return Stream.of(
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 23, 59, 59)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 2, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 20, 59, 59)
                    ),
                    Optional.of(
                        DateTimePeriod.of(
                            LocalDateTime.of(2020, 1, 1, 2, 0, 0),
                            LocalDateTime.of(2020, 1, 1, 20, 59, 59)
                        )
                    )
                ),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 20, 59, 59)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 2, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 23, 59, 59)
                    ),
                    Optional.of(
                        DateTimePeriod.of(
                            LocalDateTime.of(2020, 1, 1, 2, 0, 0),
                            LocalDateTime.of(2020, 1, 1, 20, 59, 59)
                        )
                    )
                ),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 20, 59, 59)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 20, 59, 59),
                        LocalDateTime.of(2020, 1, 1, 23, 59, 59)
                    ),
                    Optional.of(
                        DateTimePeriod.of(
                            LocalDateTime.of(2020, 1, 1, 20, 59, 59),
                            LocalDateTime.of(2020, 1, 1, 20, 59, 59)
                        )
                    )
                ),
                Arguments.of(
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 20, 59, 59)
                    ),
                    DateTimePeriod.of(
                        LocalDateTime.of(2020, 1, 1, 21, 0, 0),
                        LocalDateTime.of(2020, 1, 1, 23, 59, 59)
                    ),
                    Optional.empty()
                )
            );
        }

        @ParameterizedTest
        @MethodSource("datePeriodsProvider")
        void shouldReturnExpected(DateTimePeriod first, DateTimePeriod second, Optional<DateTimePeriod> expected) {
            assertThat(first.intersectionWith(second)).isEqualTo(expected);
            assertThat(second.intersectionWith(first)).isEqualTo(expected);
        }
    }
}
