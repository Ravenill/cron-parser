package pl.kruczek.cron.expression;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CronExpressionUtilTest {

    @Nested
    class SingleValues {

        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of("0", CronExpressionUtil.Unit.MINUTES, HashSet.of(0)),
                    Arguments.of("59", CronExpressionUtil.Unit.MINUTES, HashSet.of(59)),
                    Arguments.of("0", CronExpressionUtil.Unit.HOURS, HashSet.of(0)),
                    Arguments.of("23", CronExpressionUtil.Unit.HOURS, HashSet.of(23)),
                    Arguments.of("1", CronExpressionUtil.Unit.DAY_OF_MONTH, HashSet.of(1)),
                    Arguments.of("31", CronExpressionUtil.Unit.DAY_OF_MONTH, HashSet.of(31)),
                    Arguments.of("1", CronExpressionUtil.Unit.MONTHS, HashSet.of(1)),
                    Arguments.of("12", CronExpressionUtil.Unit.MONTHS, HashSet.of(12)),
                    Arguments.of("1", CronExpressionUtil.Unit.DAY_OF_WEEK, HashSet.of(1)),
                    Arguments.of("7", CronExpressionUtil.Unit.DAY_OF_WEEK, HashSet.of(7))
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeSimpleDefinitions(String valueToParse, CronExpressionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronExpressionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("-1", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("60", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("-1", CronExpressionUtil.Unit.HOURS),
                    Arguments.of("24", CronExpressionUtil.Unit.HOURS),
                    Arguments.of("0", CronExpressionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("32", CronExpressionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("0", CronExpressionUtil.Unit.MONTHS),
                    Arguments.of("13", CronExpressionUtil.Unit.MONTHS),
                    Arguments.of("-2", CronExpressionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("8", CronExpressionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeSimpleIncorrectDefinitions(String valueToParse, CronExpressionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronExpressionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class SpecialCharacters {

        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of("*", CronExpressionUtil.Unit.MINUTES, HashSet.ofAll(IntStream.rangeClosed(0, 59).toArray()).toSortedSet()),
                    Arguments.of("*", CronExpressionUtil.Unit.HOURS, HashSet.ofAll(IntStream.rangeClosed(0, 23).toArray()).toSortedSet()),
                    Arguments.of("*", CronExpressionUtil.Unit.DAY_OF_MONTH, HashSet.ofAll(IntStream.rangeClosed(1, 31).toArray()).toSortedSet()),
                    Arguments.of("*", CronExpressionUtil.Unit.MONTHS, HashSet.ofAll(IntStream.rangeClosed(1, 12).toArray()).toSortedSet()),
                    Arguments.of("*", CronExpressionUtil.Unit.DAY_OF_WEEK, HashSet.ofAll(IntStream.rangeClosed(1, 7).toArray()).toSortedSet())
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeSpecialCharacterDefinitions(String valueToParse, CronExpressionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronExpressionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("^", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("#", CronExpressionUtil.Unit.HOURS),
                    Arguments.of("!", CronExpressionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("&", CronExpressionUtil.Unit.MONTHS),
                    Arguments.of("(", CronExpressionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeSpecialCharacterIncorrectDefinitions(String valueToParse, CronExpressionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronExpressionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Step {
        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of("*/10", CronExpressionUtil.Unit.MINUTES, HashSet.of(0, 10, 20, 30, 40, 50).toSortedSet()),
                    Arguments.of("*/80", CronExpressionUtil.Unit.MINUTES, HashSet.of(0).toSortedSet()),
                    Arguments.of("20-23/2", CronExpressionUtil.Unit.HOURS, HashSet.of(20, 22).toSortedSet()),
                    Arguments.of("*/10", CronExpressionUtil.Unit.DAY_OF_MONTH, HashSet.of(1, 11, 21, 31).toSortedSet()),
                    Arguments.of("20-34/5", CronExpressionUtil.Unit.DAY_OF_MONTH, HashSet.of(20, 25, 30).toSortedSet()),
                    Arguments.of("3-12/2", CronExpressionUtil.Unit.MONTHS, HashSet.of(3, 5, 7, 9, 11).toSortedSet()),
                    Arguments.of("1-7/1", CronExpressionUtil.Unit.DAY_OF_WEEK, HashSet.of(1, 2, 3, 4, 5, 6, 7).toSortedSet())
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeStepDefinitions(String valueToParse, CronExpressionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronExpressionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("-/5", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("*/5/5", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("70/60", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("70-100/5", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("25/5", CronExpressionUtil.Unit.HOURS),
                    Arguments.of("14/5", CronExpressionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("20-35/5", CronExpressionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("-20/20", CronExpressionUtil.Unit.MONTHS),
                    Arguments.of("10-20/5", CronExpressionUtil.Unit.MONTHS),
                    Arguments.of("a/10", CronExpressionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("10/7", CronExpressionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("*//2", CronExpressionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeStepIncorrectDefinitions(String valueToParse, CronExpressionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronExpressionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FromTo {
        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of("58-59", CronExpressionUtil.Unit.MINUTES, HashSet.of(58, 59).toSortedSet()),
                    Arguments.of("0-4", CronExpressionUtil.Unit.HOURS, HashSet.of(0, 1, 2, 3, 4).toSortedSet()),
                    Arguments.of("30-31", CronExpressionUtil.Unit.DAY_OF_MONTH, HashSet.of(30, 31).toSortedSet()),
                    Arguments.of("1-2", CronExpressionUtil.Unit.MONTHS, HashSet.of(1, 2).toSortedSet()),
                    Arguments.of("7-7", CronExpressionUtil.Unit.DAY_OF_WEEK, HashSet.of(7).toSortedSet())
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeFromToDefinitions(String valueToParse, CronExpressionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronExpressionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("1--2", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("-1-0", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("20-24", CronExpressionUtil.Unit.HOURS),
                    Arguments.of("20-", CronExpressionUtil.Unit.HOURS),
                    Arguments.of("30-40", CronExpressionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("-20-3", CronExpressionUtil.Unit.MONTHS),
                    Arguments.of("8-8", CronExpressionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("a-g", CronExpressionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("5-g", CronExpressionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeFromToIncorrectDefinitions(String valueToParse, CronExpressionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronExpressionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Groups {
        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of(
                            "0,1-2,10-14,20", CronExpressionUtil.Unit.MINUTES,
                            HashSet.of(0, 1, 2, 10, 11, 12, 13, 14, 20).toSortedSet()
                    ),
                    Arguments.of(
                            "0,1-5,2-6,4-6,10", CronExpressionUtil.Unit.MINUTES,
                            HashSet.of(0, 1, 2, 3, 4, 5, 6, 10).toSortedSet()
                    ),
                    Arguments.of(
                            "0,15-30/10,30,31,40-42", CronExpressionUtil.Unit.MINUTES,
                            HashSet.of(0, 15, 25, 30, 31, 40, 41, 42).toSortedSet()
                    ),
                    Arguments.of(
                            "0,5-10,13", CronExpressionUtil.Unit.HOURS,
                            HashSet.of(0, 5, 6, 7, 8, 9, 10, 13)
                    ),
                    Arguments.of(
                            "1,3,5,10,29-31", CronExpressionUtil.Unit.DAY_OF_MONTH,
                            HashSet.of(1, 3, 5, 10, 29, 30, 31)
                    ),
                    Arguments.of(
                            "1-11", CronExpressionUtil.Unit.MONTHS,
                            HashSet.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
                    ),
                    Arguments.of(
                            "1-5,7", CronExpressionUtil.Unit.DAY_OF_WEEK,
                            HashSet.of(1, 2, 3, 4, 5, 7)
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeGroupDefinitions(String valueToParse, CronExpressionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronExpressionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("5,60-120", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("-20-100", CronExpressionUtil.Unit.MINUTES),
                    Arguments.of("-1-2", CronExpressionUtil.Unit.HOURS),
                    Arguments.of("3--10", CronExpressionUtil.Unit.HOURS),
                    Arguments.of("7-30,32", CronExpressionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("-10--5", CronExpressionUtil.Unit.MONTHS),
                    Arguments.of("10,1-2,7", CronExpressionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("3,", CronExpressionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of(",3", CronExpressionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeGroupIncorrectDefinitions(String valueToParse, CronExpressionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronExpressionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}