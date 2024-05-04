package pl.kruczek.cron.expression;

import io.vavr.collection.Set;

class DayOfMonth implements CronArgument {

    private final Set<Integer> dayOfMonth;

    DayOfMonth(String dayOfMonthArgs) {
        this.dayOfMonth = CronExpressionUtil.parseArgs(dayOfMonthArgs, CronExpressionUtil.Unit.DAY_OF_MONTH);
    }

    @Override
    public String prepareHeader() {
        return "day of month";
    }

    @Override
    public String prepareValues() {
        return dayOfMonth.mkString(" ");
    }
}
