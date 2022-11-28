package me.cosmic.networkcore.listeners.friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import me.cosmic.networkcore.NetworkCore;
import me.cosmic.networkcore.sql.Players;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class InitializeFriendsOnPlayerJoin implements Listener {
    private NetworkCore nc;

    public InitializeFriendsOnPlayerJoin(NetworkCore nc) {
        this.nc = nc;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!this.nc.getFriendsManager().getFriendsList().containsKey(event.getPlayer().getUniqueId())) {
            Bukkit.broadcastMessage(event.getPlayer().getName());
            HashMap<UUID, List<UUID>> friendsList = this.nc.getFriendsManager().getFriendsList();
            friendsList.put(event.getPlayer().getUniqueId(), new ArrayList<>());
            this.nc.getFriendsManager().setFriendsList(friendsList);
        }

        Players pl = new Players(nc);
        if (nc.mySQL.isConnected() && !pl.exists(event.getPlayer())) {
            Bukkit.getLogger().log(Level.INFO, "NetworkCore >> New player added to the database!");
            pl.createPlayer(event.getPlayer());
        }
    }
}
