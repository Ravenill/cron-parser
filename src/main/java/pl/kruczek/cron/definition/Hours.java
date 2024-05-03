package pl.kruczek.cron.definition;

import io.vavr.collection.Set;

class Hours implements CronArgument {

    private final Set<Integer> hours;

    Hours(String hoursArgs) {
        this.hours = CronDefinitionUtil.parseArgs(hoursArgs, CronDefinitionUtil.Unit.HOURS);
    }

    @Override
    public String preparePrint() {
        return "hour\t\t" + hours.mkString(", ");
    }
}
