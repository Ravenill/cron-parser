package pl.kruczek.cron.expression;

import io.vavr.collection.Set;

class Minutes implements CronArgument {

    private final Set<Integer> minutes;

    Minutes(String minutesArgs) {
        this.minutes = CronExpressionUtil.parseArgs(minutesArgs, CronExpressionUtil.Unit.MINUTES);
    }

    @Override
    public String prepareHeader() {
        return "minute";
    }

    @Override
    public String prepareValues() {
        return minutes.mkString(" ");
    }

    @Override
    public boolean shouldBeVisible() {
        return true;
    }
}
