package me.exerosis.sql.queue.command;

import java.util.Map;

public class CommandMaker {
    StringBuilder builder = new StringBuilder();

    public CommandMaker() {
    }

    public CommandMaker(String string) {
        builder.append(string);
    }

    public CommandMaker append(Object object) {
        builder.append(object);
        return this;
    }

    public CommandMaker appendCommand(Type type, Map<String, Object> input, String args) {
        StringBuilder commandBuilder = new StringBuilder();
        switch (type) {
            case INSERT:
                commandBuilder.append("INSERT INTO {table} (");
                commandBuilder.append(commaSplit(input.keySet().toArray()));
                commandBuilder.append(") VALUES (");
                commandBuilder.append(commaSplitQuote(input.values().toArray()));
                commandBuilder.append(')');
                break;
            case TRUNCATE:
                builder.append("TRUNCATE {table}");
                return this;
            case UPDATE:
                commandBuilder.append("UPDATE {table} SET ");
                commandBuilder.append(equalsSplit(input));
                break;
            case REPLACE:
                commandBuilder.append("REPLACE INTO {table} SET ");
                commandBuilder.append(equalsSplit(input));
                break;
        }

        if (!args.equals("")) {
            commandBuilder.append(" WHERE ");
            commandBuilder.append(args);
        }

        builder.append(commandBuilder.toString());
        return this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    private String equalsSplit(Map<String, Object> input) {
        Object[] keys = input.keySet().toArray();
        Object[] values = input.values().toArray();
        StringBuilder builder = new StringBuilder();

        builder.append(keys[0]);
        builder.append("=");
        builder.append("'");
        builder.append(values[0]);
        builder.append("'");
        for (int x = 1; x < keys.length; x++) {
            builder.append(',');
            builder.append(keys[x]);
            builder.append("=");
            builder.append("'");
            builder.append(values[x]);
            builder.append("'");
        }
        return builder.toString();
    }

    private String commaSplit(Object[] values) {
        StringBuilder builder = new StringBuilder();
        builder.append(values[0]);
        for (int x = 1; x < values.length; x++) {
            builder.append(',');
            builder.append(values[x]);
        }
        return builder.toString();
    }

    private String commaSplitQuote(Object[] values) {
        StringBuilder builder = new StringBuilder();
        builder.append("'");
        builder.append(values[0]);

        for (int x = 1; x < values.length; x++) {
            builder.append("','");
            builder.append(values[x]);
        }
        builder.append("'");
        return builder.toString();
    }

}
