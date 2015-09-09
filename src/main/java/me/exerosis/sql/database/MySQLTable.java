package me.exerosis.sql.database;

import me.exerosis.sql.SQLAPI;
import me.exerosis.sql.queue.MySQLQueue;
import me.exerosis.sql.queue.command.CommandPriority;
import me.exerosis.sql.queue.command.CommandType;
import me.exerosis.sql.queue.command.SQLCommand;
import me.exerosis.sql.queue.util.QueueUtil;
import me.exerosis.sql.queue.util.TableState;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLTable extends MySQL implements Listener {
    private String tableName;
    private MySQLQueue queue;
    private TableState state;

    //TODO add copyright statement

    /**
     * DO NOT CREATE TABLES! Use SQLAPI.TableManager.getTable(*Table Name*); & SQLAPI.TableManager.addTable(*Table Name*, *Table Password*);
     *
     * @param password
     * @param tableName
     */
    public MySQLTable(String dbName, String tableName) {
        super(dbName);
        this.tableName = tableName;
        queue = MySQLQueue.getTableQueue(this);
        Bukkit.getPluginManager().registerEvents(this, SQLAPI.getPlugin());
        state = TableState.SMART_QUEUING;
    }

    /**
     * Tells the table to execute a command. If the database is offline or the command fails, the command will be queued.
     *
     * @param command
     */
    public void executeCommand(SQLCommand command) {
        if (state.equals(TableState.SMART_QUEUING)) {
            if (!command.getPriority().equals(CommandPriority.MANDATORY)) {
                //if(queue.size() > 0){
                System.out.print("[SQL] Command queued!");
                queue.add(command);
                /*
                if(QueueUtil.getPermGenRemaining() <= 10000){
					queue.pushToDatabase();
					System.out.println("[SQL][SEVERE] Perm gen check critical, sending queue to .dat!");
				}*/
                return;
                //}
            }
            System.out.print("[SQL] Command run!");
            runCommand(command);
            //if (!isOnline())
            //throw new IllegalStateException("A MANDATORY command was run while the SQL was not online!");
        } else if (state.equals(TableState.FORCE_QUEUE)) {
            System.out.print("[SQL] Command queued!");
            queue.add(command);
        } else {
            System.out.print("[SQL] Command run!");
            runCommand(command);
        }
    }

    /**
     * For the queue ONLY! Runs a command, can cause duplicate entries if used! Use executeCommand(SQLCommand command) to execute a command!
     *
     * @param command
     * @return true if the command was run successfuly.
     */
    public boolean runCommand(SQLCommand command) {
        if (command.getType().equals(CommandType.UPDATE))
            return updateTable(command);
        if (command.getType().equals(CommandType.QUERY))
            return queryTable(command);
        return false;
    }

    /**
     * @return The table's name as a string.
     * Not necessarily the same as the SQL's table name.
     */
    public String getName() {
        return tableName;
    }

    /**
     * @return TableState, this tables state.
     */
    public TableState getTableState() {
        return state;
    }

    /**
     * Sets how the table handles new commands by default SMART_QUEUING.
     *
     * @param state
     */
    public void setTableState(TableState state) {
        this.state = state;
    }

    /**
     * Forces this tables queue to import all saved queues from Query Backups/*Table Name*.
     */
    public void forceQueueRead() {
        queue.loadFromArchive();
    }

    /**
     * Forces this tables queue to save all queued commands to Query Backups/*Table Name*.
     */
    public void forceQueueWrite() {
        queue.saveToArchive();
    }

    /**
     * Forces this tables queue to try to push all queued commands to the SQL server.
     */
    public void pushToDatabase() {
        queue.pushToDatabase();
    }

    public boolean updateTable(SQLCommand command) {
        try {
            command.returnValue(null);
            String stringCommand = command.getCommand().replace("{table}", tableName);
            if (SQLAPI.isDebugMode())
                System.out.println(stringCommand);
            updateSQL(stringCommand);
            return true;
        } catch (SQLException e) {
            if (SQLAPI.isDebugMode())
                e.printStackTrace();
            queue.add(0, command);
            return false;
        }
    }

    private boolean queryTable(SQLCommand command) {
        QueueUtil.getPermGenRemaining();
        try {
            String stringCommand = command.getCommand().replace("{table}", tableName);
            if (SQLAPI.isDebugMode())
                System.out.println(stringCommand);
            ResultSet result = querySQL(stringCommand);
            if (result == null) {
                queue.add(0, command);
                return false;
            } else {
                command.returnValue(result);
                return true;
            }
        } catch (SQLException e) {
            if (SQLAPI.isDebugMode())
                e.printStackTrace();
        }
        return false;
    }
}
