package pl.kruczek;

import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;
import static org.assertj.core.api.Assertions.assertThat;
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
    void shouldRunParserOnExactlyOneParam() throws Exception {
        //given
        final String[] params = {"* * * * * /test.exe"};

        //when
        final String output = tapSystemOutNormalized(() -> Main.main(params));

        //then
        assertThat(output).isEqualTo(
                """
                        minute         0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59
                        hour           0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
                        day of month   1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
                        month          1 2 3 4 5 6 7 8 9 10 11 12
                        day of week    1 2 3 4 5 6 7
                        command        /test.exe
                        """
        );
    }

    @Test
    void shouldPrintDataFromExampleInTask() throws Exception {
        //given
        final String[] params = {"*/15 0 1,15 * 1-5 /usr/bin/find"};

        //when
        final String output = tapSystemOutNormalized(() -> Main.main(params));

        // then
        assertThat(output).isEqualTo(
                """
                        minute         0 15 30 45
                        hour           0
                        day of month   1 15
                        month          1 2 3 4 5 6 7 8 9 10 11 12
                        day of week    1 2 3 4 5
                        command        /usr/bin/find
                        """
        );
    }

    @Test
    void shouldPrintDataFromMoarComplicatedExampleThanThatInTask() throws Exception {
        //given
        final String[] params = {"*/15 0 1-15/3 1,3,1-5,6,12 1-8/3 /usr/bin/find"};

        //when
        final String output = tapSystemOutNormalized(() -> Main.main(params));

        // then
        assertThat(output).isEqualTo(
                """
                        minute         0 15 30 45
                        hour           0
                        day of month   1 4 7 10 13
                        month          1 2 3 4 5 6 12
                        day of week    1 4 7
                        command        /usr/bin/find
                        """
        );
    }
}