package pl.kruczek.cron;

import pl.kruczek.cron.expression.CronExpression;

public class CronParser {
    private static final int STANDARD_SUPPORTED_CRON_ARGUMENTS = 6;

    public CronExpression parseExpression(String inlinedCronArguments) {
        final String[] cronArguments = inlinedCronArguments.split(" ");
        return createCronExpression(cronArguments);
    }

    private static CronExpression createCronExpression(String[] cronArguments) {
        return switch (cronArguments.length) {
            case STANDARD_SUPPORTED_CRON_ARGUMENTS -> new CronExpression.CronExpressionBuilder()
                    .withMinutes(cronArguments[0])
                    .withHours(cronArguments[1])
                    .withDayOfMonth(cronArguments[2])
                    .withMonth(cronArguments[3])
                    .withDayOfWeek(cronArguments[4])
                    .withCommand(cronArguments[5])
                    .build();
            default -> throw new IllegalArgumentException("Unsupported argument count...");
        };

    }
}
