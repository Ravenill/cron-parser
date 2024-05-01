package pl.kruczek.cron.definition;

class DayOfMonth implements CronArgument {
    DayOfMonth(String dayOfMonth) {

    }

    @Override
    public String preparePrint() {
        return "day of month\t\t";
    }
}
