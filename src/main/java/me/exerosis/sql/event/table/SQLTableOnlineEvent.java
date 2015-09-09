package me.exerosis.sql.event.table;

import me.exerosis.sql.database.MySQLTable;

public class SQLTableOnlineEvent extends TableEvent {
    public SQLTableOnlineEvent(MySQLTable table) {
        super(table);
    }
}
