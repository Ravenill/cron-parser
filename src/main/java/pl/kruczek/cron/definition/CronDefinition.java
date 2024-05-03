package pl.kruczek.cron.definition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CronDefinition {

    // TODO: Refactor to LinkedList
    private final Minutes minutes;
    private final Hours hours;
    private final DayOfMonth dayOfMonth;
    private final Months months;
    private final DayOfWeek dayOfWeek;
    private final Command command;

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
                    minutes,
                    hours,
                    dayOfMonth,
                    months,
                    dayOfWeek,
                    command
            );
        }
    }

    // TODO: Refactor to LinkedList.forEach
    public void printDefinition() {
        System.out.println(minutes.preparePrint());
        System.out.println(hours.preparePrint());
        System.out.println(dayOfMonth.preparePrint());
        System.out.println(months.preparePrint());
        System.out.println(dayOfWeek.preparePrint());
        System.out.println(command.preparePrint());
    }
}
