package pl.kruczek.cron.definition;

import io.vavr.collection.Set;

class DayOfMonth implements CronArgument {

    private final Set<Integer> dayOfMonth;

    DayOfMonth(String dayOfMonthArgs) {
        this.dayOfMonth = CronDefinitionUtil.parseArgs(dayOfMonthArgs, CronDefinitionUtil.Unit.DAY_OF_MONTH);
    }

    @Override
    public String prepareHeader() {
        return "day of month";
    }

    @Override
    public String prepareValues() {
        return dayOfMonth.mkString(", ");
    }
}
