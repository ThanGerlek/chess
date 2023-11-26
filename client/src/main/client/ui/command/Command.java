package client.ui.command;

import client.AuthorizationLevel;

import java.util.Objects;

public class Command {
    private final String commandID;
    private final AuthorizationLevel minRequiredAuthLevel;

    Command(String commandID, AuthorizationLevel minRequiredAuthLevel) {
        this.commandID = commandID;
        this.minRequiredAuthLevel = minRequiredAuthLevel;
    }

    String getCommandID() {
        return this.commandID;
    }

    public AuthorizationLevel getMinRequiredAuthLevel() {
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