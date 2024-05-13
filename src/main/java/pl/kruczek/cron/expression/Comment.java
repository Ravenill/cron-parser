package pl.kruczek.cron.expression;

class Comment implements CronArgument  {

    private static final String COMMAND_CHAR = "#";
    private final String comment;

    Comment(String commandArg) {
        if (commandArg.contains(COMMAND_CHAR)) {
            final int commandCharIndex = commandArg.indexOf(COMMAND_CHAR);
            final int nextCharAfterCommentChar = commandCharIndex + 1;
            this.comment = commandArg.substring(nextCharAfterCommentChar);
        } else {
            this.comment = null;
        }
    }

    @Override
    public String prepareHeader() {
        return "comment";
    }

    @Override
    public String prepareValues() {
        return comment;
    }

    @Override
    public boolean shouldBeVisible() {
        return comment != null;
    }
}
