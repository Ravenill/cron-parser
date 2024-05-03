package pl.kruczek.cron.definition;

import io.vavr.collection.Set;

class Minutes implements CronArgument {

    private final Set<Integer> minutes;

    Minutes(String minutesArgs) {
        this.minutes = CronDefinitionUtil.parseArgs(minutesArgs, CronDefinitionUtil.Unit.MINUTES);
    }

    @Override
    public String preparePrint() {
        return "minute\t\t" + minutes.mkString(", ");
    }
}
