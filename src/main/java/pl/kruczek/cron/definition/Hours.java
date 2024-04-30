package pl.kruczek.cron.definition;

class Hours implements CronArgument {
    Hours(String hours) {

    }

    @Override
    public void validate() {

    }

    @Override
    public String preparePrint() {
        return "hour\t\t";
    }
}
