package pl.kruczek.cron.definition;

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

class CronDefinitionUtilTest {

    @Nested
    class SingleValues {

        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of("0", CronDefinitionUtil.Unit.MINUTES, HashSet.of(0)),
                    Arguments.of("59", CronDefinitionUtil.Unit.MINUTES, HashSet.of(59)),
                    Arguments.of("0", CronDefinitionUtil.Unit.HOURS, HashSet.of(0)),
                    Arguments.of("23", CronDefinitionUtil.Unit.HOURS, HashSet.of(23)),
                    Arguments.of("1", CronDefinitionUtil.Unit.DAY_OF_MONTH, HashSet.of(1)),
                    Arguments.of("31", CronDefinitionUtil.Unit.DAY_OF_MONTH, HashSet.of(31)),
                    Arguments.of("1", CronDefinitionUtil.Unit.MONTHS, HashSet.of(1)),
                    Arguments.of("12", CronDefinitionUtil.Unit.MONTHS, HashSet.of(12)),
                    Arguments.of("1", CronDefinitionUtil.Unit.DAY_OF_WEEK, HashSet.of(1)),
                    Arguments.of("7", CronDefinitionUtil.Unit.DAY_OF_WEEK, HashSet.of(7))
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeSimpleDefinitions(String valueToParse, CronDefinitionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronDefinitionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("-1", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("60", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("-1", CronDefinitionUtil.Unit.HOURS),
                    Arguments.of("24", CronDefinitionUtil.Unit.HOURS),
                    Arguments.of("0", CronDefinitionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("32", CronDefinitionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("0", CronDefinitionUtil.Unit.MONTHS),
                    Arguments.of("13", CronDefinitionUtil.Unit.MONTHS),
                    Arguments.of("-2", CronDefinitionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("8", CronDefinitionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeSimpleIncorrectDefinitions(String valueToParse, CronDefinitionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronDefinitionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class SpecialCharacters {

        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of("*", CronDefinitionUtil.Unit.MINUTES, HashSet.ofAll(IntStream.rangeClosed(0, 59).toArray()).toSortedSet()),
                    Arguments.of("*", CronDefinitionUtil.Unit.HOURS, HashSet.ofAll(IntStream.rangeClosed(0, 23).toArray()).toSortedSet()),
                    Arguments.of("*", CronDefinitionUtil.Unit.DAY_OF_MONTH, HashSet.ofAll(IntStream.rangeClosed(1, 31).toArray()).toSortedSet()),
                    Arguments.of("*", CronDefinitionUtil.Unit.MONTHS, HashSet.ofAll(IntStream.rangeClosed(1, 12).toArray()).toSortedSet()),
                    Arguments.of("*", CronDefinitionUtil.Unit.DAY_OF_WEEK, HashSet.ofAll(IntStream.rangeClosed(1, 7).toArray()).toSortedSet())
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeSpecialCharacterDefinitions(String valueToParse, CronDefinitionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronDefinitionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("^", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("#", CronDefinitionUtil.Unit.HOURS),
                    Arguments.of("!", CronDefinitionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("&", CronDefinitionUtil.Unit.MONTHS),
                    Arguments.of("(", CronDefinitionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeSpecialCharacterIncorrectDefinitions(String valueToParse, CronDefinitionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronDefinitionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Step {
        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of("*/10", CronDefinitionUtil.Unit.MINUTES, HashSet.of(0, 10, 20, 30, 40, 50).toSortedSet()),
                    Arguments.of("*/80", CronDefinitionUtil.Unit.MINUTES, HashSet.of(0).toSortedSet()),
                    Arguments.of("20-23/2", CronDefinitionUtil.Unit.HOURS, HashSet.of(20, 22).toSortedSet()),
                    Arguments.of("*/10", CronDefinitionUtil.Unit.DAY_OF_MONTH, HashSet.of(1, 11, 21, 31).toSortedSet()),
                    Arguments.of("20-34/5", CronDefinitionUtil.Unit.DAY_OF_MONTH, HashSet.of(20, 25, 30).toSortedSet()),
                    Arguments.of("3-12/2", CronDefinitionUtil.Unit.MONTHS, HashSet.of(3, 5, 7, 9, 11).toSortedSet()),
                    Arguments.of("1-7/1", CronDefinitionUtil.Unit.DAY_OF_WEEK, HashSet.of(1, 2, 3, 4, 5, 6, 7).toSortedSet())
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeStepDefinitions(String valueToParse, CronDefinitionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronDefinitionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("-/5", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("*/5/5", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("70/60", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("70-100/5", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("25/5", CronDefinitionUtil.Unit.HOURS),
                    Arguments.of("14/5", CronDefinitionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("20-35/5", CronDefinitionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("-20/20", CronDefinitionUtil.Unit.MONTHS),
                    Arguments.of("10-20/5", CronDefinitionUtil.Unit.MONTHS),
                    Arguments.of("a/10", CronDefinitionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("10/7", CronDefinitionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("*//2", CronDefinitionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeStepIncorrectDefinitions(String valueToParse, CronDefinitionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronDefinitionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class FromTo {
        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of("58-59", CronDefinitionUtil.Unit.MINUTES, HashSet.of(58, 59).toSortedSet()),
                    Arguments.of("0-4", CronDefinitionUtil.Unit.HOURS, HashSet.of(0, 1, 2, 3, 4).toSortedSet()),
                    Arguments.of("30-31", CronDefinitionUtil.Unit.DAY_OF_MONTH, HashSet.of(30, 31).toSortedSet()),
                    Arguments.of("1-2", CronDefinitionUtil.Unit.MONTHS, HashSet.of(1, 2).toSortedSet()),
                    Arguments.of("7-7", CronDefinitionUtil.Unit.DAY_OF_WEEK, HashSet.of(7).toSortedSet())
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeFromToDefinitions(String valueToParse, CronDefinitionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronDefinitionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("1--2", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("-1-0", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("20-24", CronDefinitionUtil.Unit.HOURS),
                    Arguments.of("20-", CronDefinitionUtil.Unit.HOURS),
                    Arguments.of("30-40", CronDefinitionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("-20-3", CronDefinitionUtil.Unit.MONTHS),
                    Arguments.of("8-8", CronDefinitionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("a-g", CronDefinitionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("5-g", CronDefinitionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeFromToIncorrectDefinitions(String valueToParse, CronDefinitionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronDefinitionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Groups {
        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of(
                            "0,1-2,10-14,20", CronDefinitionUtil.Unit.MINUTES,
                            HashSet.of(0, 1, 2, 10, 11, 12, 13, 14, 20).toSortedSet()
                    ),
                    Arguments.of(
                            "0,1-5,2-6,4-6,10", CronDefinitionUtil.Unit.MINUTES,
                            HashSet.of(0, 1, 2, 3, 4, 5, 6, 10).toSortedSet()
                    ),
                    Arguments.of(
                            "0,15-30/10,30,31,40-42", CronDefinitionUtil.Unit.MINUTES,
                            HashSet.of(0, 15, 25, 30, 31, 40, 41, 42).toSortedSet()
                    ),
                    Arguments.of(
                            "0,5-10,13", CronDefinitionUtil.Unit.HOURS,
                            HashSet.of(0, 5, 6, 7, 8, 9, 10, 13)
                    ),
                    Arguments.of(
                            "1,3,5,10,29-31", CronDefinitionUtil.Unit.DAY_OF_MONTH,
                            HashSet.of(1, 3, 5, 10, 29, 30, 31)
                    ),
                    Arguments.of(
                            "1-11", CronDefinitionUtil.Unit.MONTHS,
                            HashSet.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
                    ),
                    Arguments.of(
                            "1-5,7", CronDefinitionUtil.Unit.DAY_OF_WEEK,
                            HashSet.of(1, 2, 3, 4, 5, 7)
                    )
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeGroupDefinitions(String valueToParse, CronDefinitionUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = CronDefinitionUtil.parseArgs(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("5,60-120", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("-20-100", CronDefinitionUtil.Unit.MINUTES),
                    Arguments.of("-1-2", CronDefinitionUtil.Unit.HOURS),
                    Arguments.of("3--10", CronDefinitionUtil.Unit.HOURS),
                    Arguments.of("7-30,32", CronDefinitionUtil.Unit.DAY_OF_MONTH),
                    Arguments.of("-10--5", CronDefinitionUtil.Unit.MONTHS),
                    Arguments.of("10,1-2,7", CronDefinitionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of("3,", CronDefinitionUtil.Unit.DAY_OF_WEEK),
                    Arguments.of(",3", CronDefinitionUtil.Unit.DAY_OF_WEEK)
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeGroupIncorrectDefinitions(String valueToParse, CronDefinitionUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> CronDefinitionUtil.parseArgs(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}