package pl.kruczek.cron.definition;

import io.vavr.collection.Set;

class Months implements CronArgument {

    private final Set<Integer> months;

    Months(String monthsArgs) {
        this.months = CronDefinitionUtil.parseArgs(monthsArgs, CronDefinitionUtil.Unit.MONTHS);
    }

    @Override
    public String preparePrint() {
        return "month\t\t";
    }
}
