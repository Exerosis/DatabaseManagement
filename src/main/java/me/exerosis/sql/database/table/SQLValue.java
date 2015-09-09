package me.exerosis.sql.database.table;

public class SQLValue {
    private String _collumName;
    private int _index;
    private Object _value;

    public SQLValue(String collumName, int index, Object value) {
        _collumName = collumName;
        _index = index;
        _value = value;
    }

    public String getCollumName() {
        return _collumName;
    }

    public int getIndex() {
        return _index;
    }

    public Object getValue() {
        return _value;
    }

    public <T> T getValue(Class<T> type) {
        return (T) _value;
    }
}
