package pl.kruczek.cron.definition;

class Minutes implements CronArgument {
    Minutes(String minutes) {

    }

    @Override
    public void validate() {

    }

    @Override
    public String preparePrint() {
        return "minute\t\t";
    }
}
