package pl.kruczek.cron.definition;

import io.vavr.collection.Set;

class DayOfWeek implements CronArgument {

    private final Set<Integer> dayOfWeek;

    DayOfWeek(String dayOfWeekArgs) {
        this.dayOfWeek = CronDefinitionUtil.parseArgs(dayOfWeekArgs, CronDefinitionUtil.Unit.DAY_OF_WEEK);
    }

    @Override
    public String prepareHeader() {
        return "day of week";
    }

    @Override
    public String prepareValues() {
        return dayOfWeek.mkString(", ");
    }
}
