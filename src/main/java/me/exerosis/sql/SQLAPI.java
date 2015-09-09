package me.exerosis.sql;

import me.exerosis.sql.database.MySQLTable;
import me.exerosis.sql.queue.command.*;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class SQLAPI extends JavaPlugin implements Listener {
    private static Plugin plugin;
    private static boolean debug;
    private MySQLTable table;

    public static Plugin getPlugin() {
        return plugin;
    }

    public static boolean isDebugMode() {
        return debug;
    }

    public static void setDebugMode(boolean debug) {
        SQLAPI.debug = debug;
    }

    @Override
    public void onEnable() {
        plugin = this;
    }

    public static class TableManager {
        private static HashMap<String, MySQLTable> tables = new HashMap<String, MySQLTable>();

        public static MySQLTable addTable(String dbName, String tableName) {
            tables.put(tableName, new MySQLTable(dbName, tableName));
            return tables.get(tableName);
        }

        public static MySQLTable getTable(String tableName) {
            return tables.get(tableName);
        }

        public static void executeCommand(String tableName, SQLCommand command) {
            getTable(tableName).executeCommand(command);
        }
    }

    public static class CommandManager {
        public static SQLCommand createCommand(CommandMaker command, CommandType type, CommandPriority priority, CommandRunner runnable) {
            return new SQLCommand(command, type, priority, runnable);
        }
    }

}



/*
        table = TableManager.getTable("test");
	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("table")) {

			if(args.length == 1 && args[0].equals("clear")){
				SQLCommand command = new SQLCommand("TRUNCATE TABLE  test", CommandType.QUERY, CommandPriority.CAN_QUEUE, new Command("Cleared!"));
				table.executeCommand(command);

				System.out.println("[SQL] Clear table command queued!");
			}

			else if(args.length == 1 && args[0].equals("ask")){
				SQLCommand command = new SQLCommand("SELECT * FROM test", CommandType.QUERY, CommandPriority.CAN_QUEUE, new Command("Asked!"));
				table.executeCommand(command);

				System.out.println("[SQL] Query command queued!");
			}

			else if(args.length == 3 && args[0].equals("put")){
				SQLCommand command = new SQLCommand("INSERT INTO test (testInt, testIntTwo) VALUES ('" + args[1] + "', '" + args[2] + "')", CommandType.UPDATE, CommandPriority.CAN_QUEUE,  new Command("Added"));
				table.executeCommand(command);

				System.out.println("[SQL] Add command queued!");
			}

			return true;
		} 
		return false; 
	}


	public MySQLTable getTable() {
		return table;
	}
}


class Command implements CommandRunner {
	private static final long serialVersionUID = 5569616250202530053L;
	String msg;
	public Command(String msg) {
		this.msg = msg;
	}

	@Override
	public void run(SQLResult result) {
		System.out.println(msg);

	}
*/	