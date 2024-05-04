package pl.kruczek.cron.expression;

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
        final String commandArg = "./test.exe";
        final Command command = new Command(commandArg);

        // when
        final String resultHeader = command.prepareHeader();
        final String resultValue = command.prepareValues();

        // then
        assertThat(resultHeader).isEqualTo("command");
        assertThat(resultValue).isEqualTo("./test.exe");
    }
}