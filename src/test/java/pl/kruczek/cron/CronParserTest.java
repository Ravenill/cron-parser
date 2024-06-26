package pl.kruczek.cron;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pl.kruczek.cron.expression.CronExpression;

import java.util.stream.Stream;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CronParserTest {

    private final CronParser classUnderTest = new CronParser();

    @Test
    @Disabled("leagcy")
    @Deprecated(forRemoval = true)
    void shouldThrowExceptionOnUnsupportedArgumentCount() {
        // given
        final String args = "* * * * * * * * command";

        // when then
        assertThatThrownBy(() -> classUnderTest.parseExpression(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported argument count...");
    }

    @Test
    void shouldNotThrowExceptionOnSupportedArgumentCount() {
        // given
        final String args = "* * * * * command";

        // when then
        assertThatCode(() -> classUnderTest.parseExpression(args))
                .doesNotThrowAnyException();
    }

    private static Stream<Arguments> argumentsTest() {
        return Stream.of(
                Arguments.of(
                        "*/10 12-23/2 10,30 * * /usr/bin/find",
                        """
                                minute        0 10 20 30 40 50
                                hour          12 14 16 18 20 22
                                day of month  10 30
                                month         1 2 3 4 5 6 7 8 9 10 11 12
                                day of week   1 2 3 4 5 6 7
                                command       /usr/bin/find
                                """
                ),
                Arguments.of(
                        "*/15 0 1-15/3 1,3,1-5,6,12 1-8/3 /usr/bin/find",
                        """
                                minute        0 15 30 45
                                hour          0
                                day of month  1 4 7 10 13
                                month         1 2 3 4 5 6 12
                                day of week   1 4 7
                                command       /usr/bin/find
                                """
                ),
                Arguments.of(
                        "*/15 0 1,15 * 1-5 /usr/bin/find",
                        """
                                minute        0 15 30 45
                                hour          0
                                day of month  1 15
                                month         1 2 3 4 5 6 7 8 9 10 11 12
                                day of week   1 2 3 4 5
                                command       /usr/bin/find
                                """
                ),
                Arguments.of(
                        "0 3 1 */2 * /usr/bin/bck",
                        """
                                minute        0
                                hour          3
                                day of month  1
                                month         1 3 5 7 9 11
                                day of week   1 2 3 4 5 6 7
                                command       /usr/bin/bck
                                """
                ),
                Arguments.of(
                        "3,15 3-5,9-22/3 */3 4-10 2,4,6 /firefox.exe",
                        """
                                minute        3 15
                                hour          3 4 5 9 12 15 18 21
                                day of month  1 4 7 10 13 16 19 22 25 28 31
                                month         4 5 6 7 8 9 10
                                day of week   2 4 6
                                command       /firefox.exe
                                """
                )
        );
    }

    @ParameterizedTest
    @MethodSource("argumentsTest")
    void shouldParseSomeFromToDefinitions(String args, String expectedResult) throws Exception {
        // given

        // when
        final CronExpression cronExpression = classUnderTest.parseExpression(args);
        final String output = tapSystemOutNormalized(cronExpression::printDescription);

        // then
        assertThat(output).isEqualTo(expectedResult);
    }
}