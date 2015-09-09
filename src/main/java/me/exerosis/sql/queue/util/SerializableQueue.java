package me.exerosis.sql.queue.util;

import me.exerosis.sql.queue.SQLQueue;
import me.exerosis.sql.queue.command.SQLCommand;

import java.io.Serializable;
import java.util.Vector;

public class SerializableQueue implements Serializable {
    private static final long serialVersionUID = 2560640742868065742L;

    private String tableName;
    private Vector<SerializableCommand> queue = new Vector<SerializableCommand>();

    public SerializableQueue(String tableName, SQLQueue sqlQueue) {
        this.tableName = tableName;
        for (SQLCommand command : sqlQueue) {
            SerializableCommand serializableCommand = new SerializableCommand(command.getCommand(), command.getType(), command.getRunnable());
            this.queue.add(serializableCommand);
        }
    }

    public Vector<SQLCommand> getQueue() {
        Vector<SQLCommand> commands = new Vector<SQLCommand>();
        for (SerializableCommand serializableCommand : queue) {
            commands.add(serializableCommand.toSQLCommand());
        }
        return commands;
    }

    public String getTableName() {
        return tableName;
    }
}
