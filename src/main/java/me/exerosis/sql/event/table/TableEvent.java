package me.exerosis.sql.event.table;

import me.exerosis.sql.database.MySQLTable;
import me.exerosis.sql.event.SQLEvent;

public class TableEvent extends SQLEvent {
    private MySQLTable _table;

    public TableEvent(MySQLTable table) {
        _table = table;
    }

    public MySQLTable getTable() {
        return _table;
    }
}
