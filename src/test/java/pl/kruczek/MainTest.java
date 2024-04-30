package pl.kruczek;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class MainTest {

    @Test
    void shouldNotRunParserOnToManyParams() {
        //given
        final String[] params = {"* * * * *", "/test.exe"};

        //when then
        assertThatThrownBy(() -> Main.main(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid arguments");
    }

    @Test
    void shouldNotRunParserOnNoneParams() {
        //given
        final String[] params = {};

        //when then
        assertThatThrownBy(() -> Main.main(params))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid arguments");
    }

    @Test
    void shouldRunParserOnExactlyOneParam() {
        //given
        final String[] params = {"* * * * * /test.exe"};

        //when then
        assertThatCode(() -> Main.main(params))
                .doesNotThrowAnyException();
    }
}