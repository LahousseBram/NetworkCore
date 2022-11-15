package me.cosmic.networkcore;

import me.cosmic.networkcore.listeners.friends.InitializeFriendsOnPlayerJoin;
import me.cosmic.networkcore.commands.friends.FriendsCommands;
import me.cosmic.networkcore.managers.FriendsManager;
import me.cosmic.networkcore.managers.PartyManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class NetworkCore extends JavaPlugin {
    private final PartyManager partyManager = new PartyManager();

    private final FriendsManager friendsManager = new FriendsManager();

    public void onEnable() {
        getCommand("friend").setExecutor((CommandExecutor)new FriendsCommands(this));
        getServer().getPluginManager().registerEvents((Listener)new InitializeFriendsOnPlayerJoin(this), (Plugin)this);
    }

    public void onDisable() {}

    public PartyManager getPartyManager() {
        return this.partyManager;
    }

    public FriendsManager getFriendsManager() {
        return this.friendsManager;
    }
}
