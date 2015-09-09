package me.exerosis.sql.database.table;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLRow {
    private HashMap<Integer, Object> rowInt = new HashMap<>();
    private HashMap<String, Object> rowString = new HashMap<>();
    private List<SQLValue> _values = new ArrayList<>();

    public SQLRow(ResultSet results) {
        try {
            ResultSetMetaData metaData = results.getMetaData();
            for (int x = 1; x <= metaData.getColumnCount(); x++)
                _values.add(new SQLValue(metaData.getColumnName(x), x, results.getObject(x)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object getValue(String columnName) {
        for (SQLValue value : _values) {
            if (value.getCollumName().contains(columnName))
                return value.getValue();
        }
        return null;
    }

    public Object getValue(int columnPosition) {
        for (SQLValue value : _values) {
            if (value.getIndex() == columnPosition)
                return value.getValue();
        }
        return null;
    }
}
