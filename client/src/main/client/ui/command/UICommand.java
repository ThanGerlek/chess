package client.ui.command;

public class UICommand extends Command {
    private final String description;

    UICommand(String commandString, String description) {
        super(commandString);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCommandString() {
        return this.getCommandID();
    }
}
