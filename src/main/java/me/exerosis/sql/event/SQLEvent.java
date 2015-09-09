package me.exerosis.sql.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SQLEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    public SQLEvent() {
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}
