package pl.kruczek.cron;

import io.vavr.control.Try;
import pl.kruczek.cron.expression.CronExpression;

public class CronParser {
    private static final int STANDARD_SUPPORTED_CRON_ARGUMENTS = 6;
    private static final int EXTENDED_SUPPORTED_CRON_ARGUMENTS = 7;

    public CronExpression parseExpression(String inlinedCronArguments) {
        return Try.of(() -> extendedCronExpression(inlinedCronArguments.split(" ", EXTENDED_SUPPORTED_CRON_ARGUMENTS)))
                .recover(ex -> standardCronExpression(inlinedCronArguments.split(" ", STANDARD_SUPPORTED_CRON_ARGUMENTS)))
                .getOrElseThrow(() -> new IllegalArgumentException("Unsupported argument count..."));
    }

    private CronExpression standardCronExpression(String[] cronArguments) {
        return new CronExpression.CronExpressionBuilder()
                .withMinutes(cronArguments[0])
                .withHours(cronArguments[1])
                .withDayOfMonth(cronArguments[2])
                .withMonth(cronArguments[3])
                .withDayOfWeek(cronArguments[4])
                .withCommand(cronArguments[5])
                .withComment(cronArguments[5])
                .build();
    }

    private CronExpression extendedCronExpression(String[] cronArguments) {
        return new CronExpression.CronExpressionBuilder()
                .withMinutes(cronArguments[0])
                .withHours(cronArguments[1])
                .withDayOfMonth(cronArguments[2])
                .withMonth(cronArguments[3])
                .withDayOfWeek(cronArguments[4])
                .withYears(cronArguments[5])
                .withCommand(cronArguments[6])
                .withComment(cronArguments[6])
                .build();
    }
}
