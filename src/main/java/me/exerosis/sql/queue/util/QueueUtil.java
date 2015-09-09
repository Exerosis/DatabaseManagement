package me.exerosis.sql.queue.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.Iterator;

public class QueueUtil {

    public static long getPermGenRemaining() {
        Iterator<MemoryPoolMXBean> iterator = ManagementFactory.getMemoryPoolMXBeans().iterator();
        while (iterator.hasNext()) {
            MemoryPoolMXBean item = iterator.next();
            String name = item.getName();
            MemoryUsage usage = item.getUsage();
            if (name.equals("PS Perm Gen")) {
                long remaining = usage.getMax() - usage.getUsed();
                System.out.println("[SQL] Perm Gen Check Status: " + ((remaining >= 10000) ? "Ok" : "Critical"));
                return remaining;
            }
        }
        return 0;
    }

}
