package pl.kruczek;

import io.vavr.control.Try;
import pl.kruczek.cron.definition.CronDefinition;
import pl.kruczek.cron.parser.CronParser;

public class Main {

    private final static CronParser cronParser = new CronParser();

    public static void main(String[] args) {
        final int argumentsCount = args.length;
        if (argumentsCount > 1 || argumentsCount == 0) {
            promptHelp();
            throw new IllegalArgumentException("Invalid arguments");
        }

        Try.of(() -> cronParser.parseDefinition(args[0]))
                .onFailure(ex -> promptHelp())
                .onSuccess(CronDefinition::printDefinition)
                .get();
    }

    private static void promptHelp() {
        System.err.println("Invalid arguments...");
        System.err.println("Valid is: <minutes> <hours> <day of month> <month> <day of week> <command>");
        System.err.println("Eg. call: java -jar cron-arser-1.0-SNAPSHOT.jar \"*/15 0 1,15 * 1-5 /usr/bin/find\"");
    }
}