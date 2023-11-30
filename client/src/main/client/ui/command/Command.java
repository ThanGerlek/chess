package client.ui.command;

import client.AuthorizationRole;

import java.util.Objects;

public class Command {
    private final String commandID;
    private final AuthorizationRole minRequiredAuthLevel;

    Command(String commandID, AuthorizationRole minRequiredAuthLevel) {
        this.commandID = commandID;
        this.minRequiredAuthLevel = minRequiredAuthLevel;
    }

    String getCommandID() {
        return this.commandID;
    }

    public AuthorizationRole getMinRequiredAuthLevel() {
        return minRequiredAuthLevel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Command command)) return false;
        return Objects.equals(commandID, command.commandID);
    }
}