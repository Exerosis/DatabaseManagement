package me.exerosis.sql.database.table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLResult {
    private List<SQLRow> rows = new ArrayList<>();
    private ResultSet _resultSet;

    public SQLResult(ResultSet resultSet) {
        _resultSet = resultSet;
        try {
            if (resultSet.isBeforeFirst()) {
                resultSet.beforeFirst();
                while (resultSet.next())
                    rows.add(new SQLRow(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getResultSet() {
        return _resultSet;
    }

    public List<SQLRow> getRows() {
        return rows;
    }

    public int size() {
        return rows.size();
    }

    public SQLRow getRow(int rowIndex) {
        return rows.get(rowIndex);
    }
}