package pl.kruczek;

import io.vavr.control.Try;
import pl.kruczek.cron.CronParser;
import pl.kruczek.cron.expression.CronExpression;

public class Main {

    private final static CronParser cronParser = new CronParser();

    public static void main(String[] args) {
        final int argumentsCount = args.length;
        if (argumentsCount > 1 || argumentsCount == 0) {
            promptCauseOfFailure(new IllegalArgumentException("Invalid arguments"));
            promptHelp();
            return;
        }

        Try.of(() -> cronParser.parseExpression(args[0]))
                .onFailure(Main::promptCauseOfFailure)
                .onFailure(ex -> promptHelp())
                .onSuccess(CronExpression::printDescription);
    }

    private static void promptCauseOfFailure(Throwable ex) {
        System.err.println(ex.getMessage());
        System.err.println("\n");
    }

    private static void promptHelp() {
        System.err.println("""
                Valid argument format: <minutes> <hours> <day of month> <month> <day of week> <command>
                ---
                Eg call:
                    java -jar cron-parser-jar-with-dependencies.jar \"*/15 0 1,15 * 1-5 /usr/bin/find\"
                """
        );
    }
}