package pl.kruczek.cron.expression;

import org.apache.commons.lang3.StringUtils;

class Command implements CronArgument {
    private static final String COMMAND_CHAR = "#";

    private final String command;

    Command(String command) {
        if (StringUtils.isBlank(command)) {
            throw new IllegalArgumentException("Command cannot be blank");
        }
        if (command.contains(COMMAND_CHAR)) {
            final int commandCharIndex = command.indexOf(COMMAND_CHAR);
            final int charBeforeCommentChar = commandCharIndex - 1;
            this.command = command.substring(0, charBeforeCommentChar);
        } else {
            this.command = command;
        }
    }

    @Override
    public String prepareHeader() {
        return "command";
    }

    @Override
    public String prepareValues() {
        return command;
    }

    @Override
    public boolean shouldBeVisible() {
        return true;
    }
}
