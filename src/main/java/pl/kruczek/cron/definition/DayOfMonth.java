package pl.kruczek.cron.definition;

import io.vavr.collection.Set;

class DayOfMonth implements CronArgument {

    private final Set<Integer> dayOfMonth;

    DayOfMonth(String dayOfMonthArgs) {
        this.dayOfMonth = CronDefinitionUtil.parseArgs(dayOfMonthArgs, CronDefinitionUtil.Unit.HOURS);
    }

    @Override
    public String preparePrint() {
        return "day of month\t\t";
    }
}
