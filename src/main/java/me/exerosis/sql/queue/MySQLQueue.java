package me.exerosis.sql.queue;

import me.exerosis.sql.SQLAPI.TableManager;
import me.exerosis.sql.database.MySQLTable;
import me.exerosis.sql.event.table.SQLTableOfflineEvent;
import me.exerosis.sql.event.table.SQLTableOnlineEvent;
import me.exerosis.sql.queue.command.SQLCommand;
import me.exerosis.sql.queue.util.ExTaskAsync;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.HashSet;

public class MySQLQueue extends SQLQueue implements Runnable {
    private static final long serialVersionUID = 2030379640711426410L;

    private static HashMap<MySQLTable, MySQLQueue> queues = new HashMap<>();

    private MySQLTable table;
    private boolean online = true;
    private int i = 0;

    private MySQLQueue(MySQLTable table) {
        super(table.getName());
        this.table = table;
        ExTaskAsync.startTask(this, 1, 1);
    }

    public static MySQLQueue getTableQueue(String tableName) {
        return getTableQueue(TableManager.getTable(tableName));
    }

    public static MySQLQueue getTableQueue(MySQLTable table) {
        if (queues.containsKey(table))
            return queues.get(table);
        MySQLQueue queue = new MySQLQueue(table);
        queues.put(table, queue);
        return queue;
    }

    @Override
    public void run() {
        i++;
        if (!(i % 40 == 0))
            return;
        if (i % 400 == 0)
            System.out.println("[SQL] " + size() + " commands queued! Server state: " + (table.isOnline() ? "online!" : "offline!"));
        if (!(size() > 0))
            return;
        if (!table.isOnline()) {
            if (online)
                Bukkit.getPluginManager().callEvent(new SQLTableOfflineEvent(table));
            online = false;
            return;
        }
        if (!online) {
            online = true;
            Bukkit.getPluginManager().callEvent(new SQLTableOnlineEvent(table));
            System.out.println("[SQL] The server is online again, loading from backed up queues and pushing all!");
            return;
        }

        System.out.println("[SQL] Trying to push command!");
        if (!executeFirst())
            System.out.println("[SQL] Failed to push command!");
        else
            System.out.println("[SQL] Pushed command!");
    }

    /**
     * Forces this queue to try to push all queued commands to the SQL server.
     */
    public void pushToDatabase() {
        HashSet<SQLCommand> allCommands = new HashSet<SQLCommand>();
        allCommands.addAll(this);
        for (SQLCommand command : allCommands) {
            table.runCommand(command);
            remove(command);
        }
        allCommands.clear();
        if (size() > 0)
            saveToArchive();
    }

    private boolean executeFirst() {
        SQLCommand command = firstElement();
        remove(command);
        return table.runCommand(command);
    }
}
