package pl.kruczek.cron.expression;

import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CronExpression {

    private final List<CronArgument> cronArguments;

    public static class CronExpressionBuilder {
        private Minutes minutes;
        private Hours hours;
        private DayOfMonth dayOfMonth;
        private Months months;
        private DayOfWeek dayOfWeek;
        private Command command;
        private Years years;
        private Comment comment;

        public CronExpressionBuilder withMinutes(String minutesArgs) {
            this.minutes = new Minutes(minutesArgs);
            return this;
        }

        public CronExpressionBuilder withHours(String hoursArgs) {
            this.hours = new Hours(hoursArgs);
            return this;
        }

        public CronExpressionBuilder withDayOfMonth(String dayOfMonthArgs) {
            this.dayOfMonth = new DayOfMonth(dayOfMonthArgs);
            return this;
        }

        public CronExpressionBuilder withMonth(String monthArgs) {
            this.months = new Months(monthArgs);
            return this;
        }

        public CronExpressionBuilder withDayOfWeek(String dayOfWeekArgs) {
            this.dayOfWeek = new DayOfWeek(dayOfWeekArgs);
            return this;
        }

        public CronExpressionBuilder withCommand(String commandArgs) {
            this.command = new Command(commandArgs);
            return this;
        }

        public CronExpressionBuilder withYears(String yearsArgs) {
            this.years = new Years(yearsArgs);
            return this;
        }

        public CronExpressionBuilder withComment(String comment) {
            this.comment = new Comment(comment);
            return this;
        }

        public CronExpression build() {
            return new CronExpression(
                    List.of( // order of args is binding
                                    minutes,
                                    hours,
                                    dayOfMonth,
                                    months,
                                    dayOfWeek,
                                    years,
                                    command,
                                    comment
                            )
                            .filter(Objects::nonNull)
            );
        }
    }

    public void printDescription() {
        cronArguments
                .filter(CronArgument::shouldBeVisible)
                .forEach(
                        arg -> System.out.printf("%-14s%s%n", arg.prepareHeader(), arg.prepareValues())
                );
    }
}
