package me.exerosis.sql.queue.command;

import me.exerosis.sql.database.table.SQLResult;

import java.io.Serializable;
import java.sql.ResultSet;

public class SQLCommand implements Serializable {
    private static final long serialVersionUID = 5569616250202530052L;
    private CommandMaker command;
    private CommandType commandType;
    private CommandPriority priority;
    private CommandRunner runnable;

    public SQLCommand(CommandMaker command, CommandType commandType, CommandPriority priority) {
        this(command, commandType, priority, null);
    }

    public SQLCommand(CommandMaker command, CommandType commandType, CommandPriority priority, CommandRunner runnable) {
        this.command = command;
        this.commandType = commandType;
        this.priority = priority;
        this.runnable = runnable;
    }

    //Primary Methods
    public void returnValue(ResultSet result) {
        if (runnable != null)
            try {
                SQLResult sqlResult = null;
                if (result != null)
                    sqlResult = new SQLResult(result);
                runnable.run(sqlResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


    //Getters and setters.
    public CommandRunner getRunnable() {
        return runnable;
    }

    public String getCommand() {
        return command.toString();
    }

    public CommandPriority getPriority() {
        return priority;
    }

    public CommandType getType() {
        return commandType;
    }
}
