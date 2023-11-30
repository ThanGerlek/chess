package client.ui.command;

import client.AuthorizationRole;

public class UICommand extends Command {
    private final String description;

    UICommand(String commandString, String description) {
        this(commandString, description, AuthorizationRole.GUEST);
    }

    UICommand(String commandString, String description, AuthorizationRole minRequiredAuthRole) {
        super(commandString, minRequiredAuthRole);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getCommandString() {
        return this.getCommandID();
    }
}
