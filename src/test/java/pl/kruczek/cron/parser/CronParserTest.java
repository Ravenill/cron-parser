package pl.kruczek.cron.parser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CronParserTest {

    private final CronParser classUnderTest = new CronParser();

    @Test
    void shouldThrowExceptionOnUnsupportedArgumentCount() {
        // given
        final String args = "* * * * * * * * command";

        // when then
        assertThatThrownBy(() -> classUnderTest.parseAndValidateDefinition(args))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported argument count...");
    }

    @Test
    void shouldNotThrowExceptionOnSupportedArgumentCount() {
        // given
        final String args = "* * * * * command";

        // when then
        assertThatCode(() -> classUnderTest.parseAndValidateDefinition(args))
                .doesNotThrowAnyException();
    }
}