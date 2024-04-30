package pl.kruczek.cron.definition;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CronDefinition {
    private final Minutes minutes;
    private final Hours hours;
    private final DayOfMonth dayOfMonth;
    private final Month month;
    private final DayOfWeek dayOfWeek;
    private final Command command;

    public static class CronDefinitionBuilder {
        private Minutes minutes;
        private Hours hours;
        private DayOfMonth dayOfMonth;
        private Month month;
        private DayOfWeek dayOfWeek;
        private Command command;

        public CronDefinitionBuilder withMinutes(String minutes) {
            this.minutes = new Minutes(minutes);
            return this;
        }

        public CronDefinitionBuilder withHours(String hours) {
            this.hours = new Hours(hours);
            return this;
        }

        public CronDefinitionBuilder withDayOfMonth(String dayOfMonth) {
            this.dayOfMonth = new DayOfMonth(dayOfMonth);
            return this;
        }

        public CronDefinitionBuilder withMonth(String month) {
            this.month = new Month(month);
            return this;
        }

        public CronDefinitionBuilder withDayOfWeek(String dayOfWeek) {
            this.dayOfWeek = new DayOfWeek(dayOfWeek);
            return this;
        }

        public CronDefinitionBuilder withCommand(String command) {
            this.command = new Command(command);
            return this;
        }

        public CronDefinition build() {
            return new CronDefinition(
                    minutes,
                    hours,
                    dayOfMonth,
                    month,
                    dayOfWeek,
                    command
            );
        }
    }

    public void validateDefinition() {
        minutes.validate();
        hours.validate();
        dayOfMonth.validate();
        month.validate();
        dayOfWeek.validate();
        command.validate();
    }

    public void printDefinition() {
        System.out.println(minutes.preparePrint());
        System.out.println(hours.preparePrint());
        System.out.println(dayOfMonth.preparePrint());
        System.out.println(month.preparePrint());
        System.out.println(dayOfWeek.preparePrint());
        System.out.println(command.preparePrint());
    }
}
