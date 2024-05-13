package pl.kruczek.cron.expression;

import io.vavr.collection.Set;

class Years implements CronArgument {

    private final Set<Integer> years;

    public Years(String yearsArgs) {
        this.years = CronExpressionUtil.parseArgs(yearsArgs, CronExpressionUtil.Unit.YEARS);
    }

    @Override
    public String prepareHeader() {
        return "year";
    }

    @Override
    public String prepareValues() {
        return years.mkString(" ");
    }

    @Override
    public boolean shouldBeVisible() {
        return true;
    }
}
