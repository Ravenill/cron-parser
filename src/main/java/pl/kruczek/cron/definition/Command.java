package pl.kruczek.cron.definition;

import org.apache.commons.lang3.StringUtils;

class Command implements CronArgument {
    private final String command;

    Command(String command) {
        this.command = command;
    }

    @Override
    public void validate() {
        if (StringUtils.isBlank(command)) {
            throw new IllegalArgumentException("Command cannot be blank");
        }
    }

    @Override
    public String preparePrint() {
        return "command\t\t" + command;
    }
}
