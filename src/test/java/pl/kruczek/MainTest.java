package pl.kruczek;

import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemErrNormalized;
import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;
import static org.assertj.core.api.Assertions.assertThat;


class MainTest {

    @Test
    void shouldNotRunParserOnToManyParams() throws Exception {
        //given
        final String[] params = {"* * * * *", "/test.exe"};

        //when
        final String output = tapSystemErrNormalized(() -> Main.main(params));

        // then
        assertThat(output).isEqualTo(
                """
                        Invalid arguments
                                                
                                                
                        Valid argument format: <minutes> <hours> <day of month> <month> <day of week> <command>
                        ---
                        Eg call:
                            java -jar cron-parser-jar-with-dependencies.jar \"*/15 0 1,15 * 1-5 /usr/bin/find\"
                        
                        """
        );
    }

    @Test
    void shouldNotRunParserOnNoneParams() throws Exception {
        //given
        final String[] params = {};

        //when
        final String output = tapSystemErrNormalized(() -> Main.main(params));

        // then
        assertThat(output).isEqualTo(
                """
                        Invalid arguments
                                                
                                                
                        Valid argument format: <minutes> <hours> <day of month> <month> <day of week> <command>
                        ---
                        Eg call:
                            java -jar cron-parser-jar-with-dependencies.jar \"*/15 0 1,15 * 1-5 /usr/bin/find\"
                        
                        """
        );
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
                        minute        0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59
                        hour          0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23
                        day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
                        month         1 2 3 4 5 6 7 8 9 10 11 12
                        day of week   1 2 3 4 5 6 7
                        command       /test.exe
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
                        minute        0 15 30 45
                        hour          0
                        day of month  1 15
                        month         1 2 3 4 5 6 7 8 9 10 11 12
                        day of week   1 2 3 4 5
                        command       /usr/bin/find
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
                        minute        0 15 30 45
                        hour          0
                        day of month  1 4 7 10 13
                        month         1 2 3 4 5 6 12
                        day of week   1 4 7
                        command       /usr/bin/find
                        """
        );
    }
    @Test
    void shouldPrintDataFromInterviewExample() throws Exception {
        //given
        final String[] params = {"5 4 * * * 1993,1994,1995 /usr/bin/find"};

        //when
        final String output = tapSystemOutNormalized(() -> Main.main(params));

        // then
        assertThat(output).isEqualTo(
                """
                        minute        5
                        hour          4
                        day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
                        month         1 2 3 4 5 6 7 8 9 10 11 12
                        day of week   1 2 3 4 5 6 7
                        year          1993 1994 1995
                        command       /usr/bin/find
                        """
        );
    }

    @Test
    void shouldPrintDataFromInterviewExample2() throws Exception {
        //given
        final String[] params = {"5 4 * * MON-TUE 1993,1994,1995 /usr/bin/find"};

        //when
        final String output = tapSystemOutNormalized(() -> Main.main(params));

        // then
        assertThat(output).isEqualTo(
                """
                        minute        5
                        hour          4
                        day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
                        month         1 2 3 4 5 6 7 8 9 10 11 12
                        day of week   1 2
                        year          1993 1994 1995
                        command       /usr/bin/find
                        """
        );
    }
    @Test
    void shouldPrintDataFromInterviewExample3() throws Exception {
        //given
        final String[] params = {"5 4 * * MON-TUE 1993,1994,1995 /usr/bin/find # comment"};

        //when
        final String output = tapSystemOutNormalized(() -> Main.main(params));

        // then
        assertThat(output).isEqualTo(
                """
                        minute        5
                        hour          4
                        day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
                        month         1 2 3 4 5 6 7 8 9 10 11 12
                        day of week   1 2
                        year          1993 1994 1995
                        command       /usr/bin/find
                        comment        comment
                        """
        );
    }

    @Test
    void shouldPrintDataFromInterviewExample4() throws Exception {
        //given
        final String[] params = {"5 4 * * MON-TUE /usr/bin/find # comment"};

        //when
        final String output = tapSystemOutNormalized(() -> Main.main(params));

        // then
        assertThat(output).isEqualTo(
                """
                        minute        5
                        hour          4
                        day of month  1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
                        month         1 2 3 4 5 6 7 8 9 10 11 12
                        day of week   1 2
                        command       /usr/bin/find
                        comment        comment
                        """
        );
    }
}