package pl.kruczek.cron.definition;

import io.vavr.collection.Set;

class Minutes implements CronArgument {

    private final Set<Integer> minutes;

    Minutes(String minutes) {
        this.minutes = ParserUtil.parse(minutes, ParserUtil.Unit.MINUTES);
    }

    @Override
    public String preparePrint() {
        return "minute\t\t";
    }
}
