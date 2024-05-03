package pl.kruczek.cron.definition;

import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CronDefinition {

    private final List<CronArgument> cronArguments;

    public static class CronDefinitionBuilder {
        private Minutes minutes;
        private Hours hours;
        private DayOfMonth dayOfMonth;
        private Months months;
        private DayOfWeek dayOfWeek;
        private Command command;

        public CronDefinitionBuilder withMinutes(String minutesArgs) {
            this.minutes = new Minutes(minutesArgs);
            return this;
        }

        public CronDefinitionBuilder withHours(String hoursArgs) {
            this.hours = new Hours(hoursArgs);
            return this;
        }

        public CronDefinitionBuilder withDayOfMonth(String dayOfMonthArgs) {
            this.dayOfMonth = new DayOfMonth(dayOfMonthArgs);
            return this;
        }

        public CronDefinitionBuilder withMonth(String monthArgs) {
            this.months = new Months(monthArgs);
            return this;
        }

        public CronDefinitionBuilder withDayOfWeek(String dayOfWeekArgs) {
            this.dayOfWeek = new DayOfWeek(dayOfWeekArgs);
            return this;
        }

        public CronDefinitionBuilder withCommand(String commandArgs) {
            this.command = new Command(commandArgs);
            return this;
        }

        public CronDefinition build() {
            return new CronDefinition(
                    List.of( // order of args is binding
                            minutes,
                            hours,
                            dayOfMonth,
                            months,
                            dayOfWeek,
                            command
                    )
            );
        }
    }

    public void printDefinition() {
        cronArguments.forEach(
                arg -> System.out.printf("%-15s%s%n", arg.prepareHeader(), arg.prepareValues())
        );
    }
}
