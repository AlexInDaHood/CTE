package com.osterph.cte;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;


import de.fkfabian.api.mc.cloud.SpigotCloud;
import de.fkfabian.api.util.MongoAPI;

import org.bson.Document;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class CloudServerLogin {
    private static MongoDatabase database = null;
    private static MongoCollection<Document> collection = null;
    private static String name;

    public static void login() {
        database = MongoAPI.client.getDatabase("playhills");
        collection = database.getCollection("cte");
        new BukkitRunnable() {
            @Override
            public void run() {
                if (SpigotCloud.getConfiguration() == null)
                    return;

                if (name == null) {
                    name = SpigotCloud.servername;
                    collection.insertOne(new Document("name", name).append("count", 0).append("ingame", false));
                }

                collection.updateOne(Filters.eq("name", name),
                        Updates.combine(
                                Updates.set("count", Bukkit.getOnlinePlayers().size()),
                                Updates.set("ingame",CTE.INSTANCE.getSystem().gamestate != CTESystem.GAMESTATE.STARTING)
                        ));
            }
        }.runTaskTimerAsynchronously(CTE.INSTANCE, 0, 20L);
    }

    public static void logout() {
        collection.deleteOne(Filters.eq("name", name));
    }
}
