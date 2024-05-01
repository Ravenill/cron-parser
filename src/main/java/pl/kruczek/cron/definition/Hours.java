package pl.kruczek.cron.definition;

class Hours implements CronArgument {
    Hours(String hours) {

    }

    @Override
    public String preparePrint() {
        return "hour\t\t";
    }
}
