package pl.kruczek.cron.definition;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ParserUtilTest {

    @Nested
    class SingleValues {

        private static Stream<Arguments> correctArgumentsTest() {
            return Stream.of(
                    Arguments.of("0", ParserUtil.Unit.MINUTES, HashSet.of(0)),
                    Arguments.of("60", ParserUtil.Unit.MINUTES, HashSet.of(60)),
                    Arguments.of("0", ParserUtil.Unit.HOURS, HashSet.of(0)),
                    Arguments.of("23", ParserUtil.Unit.HOURS, HashSet.of(23)),
                    Arguments.of("1", ParserUtil.Unit.DAY_OF_MONTH, HashSet.of(1)),
                    Arguments.of("31", ParserUtil.Unit.DAY_OF_MONTH, HashSet.of(31)),
                    Arguments.of("1", ParserUtil.Unit.MONTHS, HashSet.of(1)),
                    Arguments.of("12", ParserUtil.Unit.MONTHS, HashSet.of(12)),
                    Arguments.of("1", ParserUtil.Unit.DAY_OF_WEEK, HashSet.of(1)),
                    Arguments.of("7", ParserUtil.Unit.DAY_OF_WEEK, HashSet.of(7))
            );
        }

        @ParameterizedTest
        @MethodSource("correctArgumentsTest")
        void shouldParseSomeSimpleDefinitions(String valueToParse, ParserUtil.Unit unit, Set<Integer> expectedValues) {
            // given

            // when
            final Set<Integer> result = ParserUtil.parse(valueToParse, unit);

            // then
            assertThat(result).containsExactlyElementsOf(expectedValues);
        }

        private static Stream<Arguments> incorrectArgumentsTest() {
            return Stream.of(
                    Arguments.of("-1", ParserUtil.Unit.MINUTES),
                    Arguments.of("61", ParserUtil.Unit.MINUTES),
                    Arguments.of("-1", ParserUtil.Unit.HOURS, HashSet.of(0)),
                    Arguments.of("24", ParserUtil.Unit.HOURS, HashSet.of(23)),
                    Arguments.of("0", ParserUtil.Unit.DAY_OF_MONTH, HashSet.of(1)),
                    Arguments.of("32", ParserUtil.Unit.DAY_OF_MONTH, HashSet.of(31)),
                    Arguments.of("0", ParserUtil.Unit.MONTHS, HashSet.of(1)),
                    Arguments.of("13", ParserUtil.Unit.MONTHS, HashSet.of(12)),
                    Arguments.of("-2", ParserUtil.Unit.DAY_OF_WEEK, HashSet.of(1)),
                    Arguments.of("8", ParserUtil.Unit.DAY_OF_WEEK, HashSet.of(7))
            );
        }

        @ParameterizedTest
        @MethodSource("incorrectArgumentsTest")
        void shouldThrowExceptionOnSomeSimpleIncorrectDefinitions(String valueToParse, ParserUtil.Unit unit) {
            // given

            // when then
            assertThatThrownBy(() -> ParserUtil.parse(valueToParse, unit))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }


}