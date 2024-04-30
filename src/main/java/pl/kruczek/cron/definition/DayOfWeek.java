package pl.kruczek.cron.definition;

class DayOfWeek implements CronArgument {
    DayOfWeek(String dayOfWeek) {

    }

    @Override
    public void validate() {

    }

    @Override
    public String preparePrint() {
        return "day of week\t\t";
    }
}
