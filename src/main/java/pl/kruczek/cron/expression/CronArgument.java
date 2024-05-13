package pl.kruczek.cron.expression;

public interface CronArgument {
    String prepareHeader();
    String prepareValues();
    boolean shouldBeVisible();
}
