package client.ui.command;

import client.AuthorizationLevel;

public class UICommand extends Command {
    private final String description;

    UICommand(String commandString, String description) {
        this(commandString, description, AuthorizationLevel.ANY);
    }

    UICommand(String commandString, String description, AuthorizationLevel minRequiredAuthLevel) {
        super(commandString, minRequiredAuthLevel);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCommandString() {
        return this.getCommandID();
    }
}
