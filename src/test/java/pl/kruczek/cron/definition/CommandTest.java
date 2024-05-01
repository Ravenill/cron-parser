package pl.kruczek.cron.definition;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommandTest {

    @Test
    void shouldThrowExceptionOnInvalidCommandArgument() {
        // given
        final String command = "";

        // when then
        assertThatThrownBy(() -> new Command(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Command cannot be blank");
    }

    @Test
    void shouldNotThrowExceptionOnValidCommandArgument() {
        // given
        final String command = "./test.exe";

        // when then
        assertThatCode(() -> new Command(command))
                .doesNotThrowAnyException();
    }

    @Test
    void shouldPrintCorrectOutput() {
        // given
        final String command = "./test.exe";

        // when
        final String result = new Command(command).preparePrint();

        // then
        assertThat(result).isEqualTo("command\t\t./test.exe");
    }
}