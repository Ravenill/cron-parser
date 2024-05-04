package pl.kruczek.cron.expression;

import io.vavr.collection.Set;

class Hours implements CronArgument {

    private final Set<Integer> hours;

    Hours(String hoursArgs) {
        this.hours = CronExpressionUtil.parseArgs(hoursArgs, CronExpressionUtil.Unit.HOURS);
    }

    @Override
    public String prepareHeader() {
        return "hour";
    }

    @Override
    public String prepareValues() {
        return hours.mkString(" ");
    }
}
