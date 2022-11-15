package me.cosmic.networkcore.systems;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class FriendRequest {
    private UUID target;

    private UUID origin;

    private String originName;

    public FriendRequest(UUID target, UUID origin) {
        this.target = target;
        this.origin = origin;
        this.originName = Bukkit.getPlayer(origin).getName();
        Bukkit.getPlayer(target).sendMessage(ChatColor.LIGHT_PURPLE + "-----------------------------------------------------");
        Bukkit.getPlayer(target).sendMessage(ChatColor.LIGHT_PURPLE + "");
        Bukkit.getPlayer(target).sendMessage(ChatColor.WHITE + "You have received a friend request from " + ChatColor.YELLOW + Bukkit.getPlayer(origin).getName());
        Bukkit.getPlayer(target).sendMessage(ChatColor.WHITE + "Type " + ChatColor.YELLOW + "/f accept " + Bukkit.getPlayer(origin).getName() + ChatColor.WHITE + " to accept this request and become friends.");
        Bukkit.getPlayer(target).sendMessage(ChatColor.LIGHT_PURPLE + "");
        Bukkit.getPlayer(target).sendMessage(ChatColor.LIGHT_PURPLE + "-----------------------------------------------------");
        Bukkit.getPlayer(origin).sendMessage(ChatColor.LIGHT_PURPLE + "Friend request sent to " + ChatColor.YELLOW + Bukkit.getPlayer(target).getName());
    }

    public UUID getTarget() {
        return this.target;
    }

    public void setTarget(UUID target) {
        this.target = target;
    }

    public UUID getOrigin() {
        return this.origin;
    }

    public void setOrigin(UUID origin) {
        this.origin = origin;
    }

    public String getName() {
        return this.originName;
    }
}
