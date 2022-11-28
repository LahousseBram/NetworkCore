package me.cosmic.networkcore;

import me.cosmic.networkcore.commands.database.DatabaseCommands;
import me.cosmic.networkcore.listeners.friends.InitializeFriendsOnPlayerJoin;
import me.cosmic.networkcore.commands.friends.FriendsCommands;
import me.cosmic.networkcore.managers.FriendsManager;
import me.cosmic.networkcore.managers.PartyManager;
import me.cosmic.networkcore.sql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class NetworkCore extends JavaPlugin {
    private final PartyManager partyManager = new PartyManager();

    public MySQL mySQL;

    private final FriendsManager friendsManager = new FriendsManager();

    public void onEnable() {

        this.mySQL = new MySQL();

        try {
            mySQL.connect();
        } catch (SQLException e) {
            Bukkit.getLogger().warning(ChatColor.RED + "NetworkCore >> Could not connect to database.");
        }

        if (mySQL.isConnected()) Bukkit.getLogger().info(ChatColor.GREEN + "NetworkCore >> Successfully connected to the database.");

        getCommand("friend").setExecutor(new FriendsCommands(this));
        getCommand("executequery").setExecutor(new DatabaseCommands(this));
        getServer().getPluginManager().registerEvents(new InitializeFriendsOnPlayerJoin(this), this);
    }

    public void onDisable() {
        mySQL.disconnect();

        if (!mySQL.isConnected()) Bukkit.getLogger().info(ChatColor.GREEN + "NetworkCore >> Successfully disconnected the database.");
    }

    public PartyManager getPartyManager() {
        return this.partyManager;
    }

    public FriendsManager getFriendsManager() {
        return this.friendsManager;
    }
}
