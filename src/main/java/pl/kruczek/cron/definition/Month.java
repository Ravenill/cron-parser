package pl.kruczek.cron.definition;

class Month implements CronArgument {
    Month(String month) {
    }

    @Override
    public String preparePrint() {
        return "month\t\t";
    }
}
