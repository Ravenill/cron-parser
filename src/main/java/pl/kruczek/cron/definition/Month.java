package pl.kruczek.cron.definition;

class Month implements CronArgument {
    Month(String month) {
    }

    @Override
    public void validate() {

    }

    @Override
    public String preparePrint() {
        return "month\t\t";
    }
}
