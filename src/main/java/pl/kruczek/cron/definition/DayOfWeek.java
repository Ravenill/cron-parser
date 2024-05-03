package pl.kruczek.cron.definition;

import io.vavr.collection.Set;

class DayOfWeek implements CronArgument {

    private final Set<Integer> dayOfWeek;

    DayOfWeek(String dayOfWeekArgs) {
        this.dayOfWeek = CronDefinitionUtil.parseArgs(dayOfWeekArgs, CronDefinitionUtil.Unit.HOURS);
    }

    @Override
    public String preparePrint() {
        return "day of week\t\t" + dayOfWeek.mkString(", ");
    }
}
