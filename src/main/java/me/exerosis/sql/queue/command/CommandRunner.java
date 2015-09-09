package me.exerosis.sql.queue.command;

import me.exerosis.sql.database.table.SQLResult;

import java.io.Serializable;

public interface CommandRunner extends Serializable {
    void run(SQLResult result);
}
