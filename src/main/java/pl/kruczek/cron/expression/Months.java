package pl.kruczek.cron.expression;

import io.vavr.collection.Set;

class Months implements CronArgument {

    private final Set<Integer> months;

    Months(String monthsArgs) {
        this.months = CronExpressionUtil.parseArgs(monthsArgs, CronExpressionUtil.Unit.MONTHS);
    }

    @Override
    public String prepareHeader() {
        return "month";
    }

    @Override
    public String prepareValues() {
        return months.mkString(" ");
    }
}
