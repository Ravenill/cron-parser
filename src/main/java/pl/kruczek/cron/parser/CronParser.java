package pl.kruczek.cron.parser;

import pl.kruczek.cron.definition.CronDefinition;

public class CronParser {
    private static final int STANDARD_SUPPORTED_CRON_ARGUMENTS = 6;

    public CronDefinition parseDefinition(String inlinedCronArguments) {
        final String[] cronArguments = inlinedCronArguments.split(" ");
        return getCronDefinition(cronArguments);
    }

    private static CronDefinition getCronDefinition(String[] cronArguments) {
        return switch (cronArguments.length) {
            case STANDARD_SUPPORTED_CRON_ARGUMENTS -> new CronDefinition.CronDefinitionBuilder()
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
