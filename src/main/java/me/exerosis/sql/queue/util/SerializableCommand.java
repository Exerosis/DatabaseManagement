package me.exerosis.sql.queue.util;

import me.exerosis.sql.queue.command.CommandMaker;
import me.exerosis.sql.queue.command.CommandRunner;
import me.exerosis.sql.queue.command.CommandType;
import me.exerosis.sql.queue.command.SQLCommand;

import java.io.Serializable;

public class SerializableCommand implements Serializable {
    private String command;
    private CommandType type;
    private CommandRunner runnable;

    public SerializableCommand(String command, CommandType type, CommandRunner runnable) {
        this.command = command;
        this.type = type;
        this.runnable = runnable;
    }

    public SQLCommand toSQLCommand() {
        return new SQLCommand(new CommandMaker(command), type, null, runnable);
    }

    public String getCommand() {
        return command;
    }

    public CommandRunner getRunnable() {
        return runnable;
    }

    public CommandType getType() {
        return type;
    }
}
