package client.ui.command;

import java.util.Objects;

public class Command {
    private final String commandID;

    Command(String commandID) {
        this.commandID = commandID;
    }

    String getCommandID() {
        return this.commandID;
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