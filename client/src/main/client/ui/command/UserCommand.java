package client.ui.command;

public class UserCommand extends Command {
    private final String description;

    UserCommand(String commandString, String description) {
        super(commandString);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
