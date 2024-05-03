package pl.kruczek.cron.definition;

public interface CronArgument {
    String prepareHeader();
    String prepareValues();
}
