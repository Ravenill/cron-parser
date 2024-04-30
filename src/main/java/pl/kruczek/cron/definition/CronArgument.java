package pl.kruczek.cron.definition;

public interface CronArgument {
    void validate();
    String preparePrint();
}
