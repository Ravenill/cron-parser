package pl.kruczek.cron.expression;

import org.apache.commons.lang3.StringUtils;

class Command implements CronArgument {
    private final String command;

    Command(String command) {
        if (StringUtils.isBlank(command)) {
            throw new IllegalArgumentException("Command cannot be blank");
        }
        this.command = command;
    }

    @Override
    public String prepareHeader() {
        return "command";
    }

    @Override
    public String prepareValues() {
        return command;
    }
}
