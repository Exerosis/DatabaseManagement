package me.exerosis.sql.event.table;

import me.exerosis.sql.database.MySQLTable;

public class SQLTableOfflineEvent extends TableEvent {
    public SQLTableOfflineEvent(MySQLTable table) {
        super(table);
    }
}
