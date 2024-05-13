package pl.kruczek.cron.expression;

import io.vavr.collection.Set;

class DayOfWeek implements CronArgument {

    private final Set<Integer> dayOfWeek;

    DayOfWeek(String dayOfWeekArgs) {
        this.dayOfWeek = CronExpressionUtil.parseArgs(dayOfWeekArgs, CronExpressionUtil.Unit.DAY_OF_WEEK);
    }

    @Override
    public String prepareHeader() {
        return "day of week";
    }

    @Override
    public String prepareValues() {
        return dayOfWeek.mkString(" ");
    }

    @Override
    public boolean shouldBeVisible() {
        return true;
    }
}
