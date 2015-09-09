package me.exerosis.sql.queue;

import me.exerosis.sql.SQLAPI;
import me.exerosis.sql.SQLAPI.TableManager;
import me.exerosis.sql.database.MySQLTable;
import me.exerosis.sql.queue.command.SQLCommand;
import me.exerosis.sql.queue.util.SerializableQueue;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

import java.io.*;
import java.util.Vector;

public class SQLQueue extends Vector<SQLCommand> implements Listener {
    private static final long serialVersionUID = 5262999220239499415L;
    private String tableName;

    protected SQLQueue(String tableName) {
        super();
        this.tableName = tableName;
        Bukkit.getPluginManager().registerEvents(this, SQLAPI.getPlugin());
        loadFromArchive();
    }

    /**
     * Forces this queue to save all queued commands to Query Backups/*Table Name*.
     */
    public void saveToArchive() {
        try {
            File file = new File("SQL/Query Backups/" + tableName);
            int x = 2;

            if (size() == 0)
                return;

            if (!file.exists()) {
                file.mkdirs();
                System.out.println("[SQL] Created 'SQL/Query Backups/" + tableName + "' folder.");
            }

            file = new File("SQL/Query Backups/" + tableName + "/backup.dat");

            while (file.exists()) {
                file = new File("SQL/Query Backups/" + tableName + "backup(" + x + ").dat");
                x++;
            }

            file.createNewFile();
            System.out.println("[SQL] Created '" + file.getName() + "' backup file.");


            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(new SerializableQueue(tableName, this));
            objectOutputStream.close();

            System.out.println("[SQL] Backed up " + size() + " commands!");
            clear();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Forces this queue to import all saved queues from Query Backups/*Table Name*.
     */
    public void loadFromArchive() {
        File file = new File("SQL/Query Backups/" + tableName);
        if (!file.exists()) {
            file.mkdirs();
            System.out.println("[SQL] Created 'SQL/Query Backups/" + tableName + "' folder.");
        }
        for (File backupFile : file.listFiles()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(backupFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                SerializableQueue backupQueue = (SerializableQueue) objectInputStream.readObject();
                objectInputStream.close();
                System.out.println("[SQL] Imported " + backupQueue.getQueue().size() + " commands!");
                this.addAll(backupQueue.getQueue());
                backupFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onDisable(PluginDisableEvent event) {
        if (!event.getPlugin().equals(SQLAPI.getPlugin()))
            return;
        MySQLTable table = TableManager.getTable(tableName);
        if (table.isOnline())
            table.pushToDatabase();
        saveToArchive();
    }

}
