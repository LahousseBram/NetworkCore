package me.cosmic.networkcore.commands.friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.cosmic.networkcore.NetworkCore;
import me.cosmic.networkcore.managers.FriendsManager;
import me.cosmic.networkcore.systems.FriendRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendsCommands implements CommandExecutor {
    private NetworkCore networkcore;

    private FriendsManager friendsManager;

    public FriendsCommands(NetworkCore core) {
        this.networkcore = core;
        this.friendsManager = this.networkcore.getFriendsManager();
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        try {
            target = Bukkit.getPlayer(args[1]);
        } catch (NullPointerException e) {
            player.sendMessage(ChatColor.RED + "That player either doesn't exist or isn't online at the moment. Please try again and check your spelling.");
            return true;
        }
        HashMap<UUID, List<UUID>> friends = this.friendsManager.getFriendsList();
        if (args[0].equalsIgnoreCase("add")) {
            boolean isFriends = false;
            if (((List)friends.get(player.getUniqueId())).size() >= 1)
                for (UUID f : friends.get(player.getUniqueId())) {
                    if (f == target.getUniqueId()) {
                        isFriends = true;
                        break;
                    }
                }
            if (!isFriends) {
                FriendRequest request = new FriendRequest(target.getUniqueId(), player.getUniqueId());
                List<FriendRequest> f = this.friendsManager.getFriendRequests();
                f.add(request);
                this.friendsManager.setFriendRequests(f);
            } else {
                player.sendMessage(ChatColor.RED + "That player is already on your friends list.");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("accept")) {
            boolean hasFriendRequest = false;
            List<FriendRequest> f = this.friendsManager.getFriendRequests();
            for (FriendRequest request : f) {
                if (request.getName().equalsIgnoreCase(target.getName())) {
                    hasFriendRequest = true;
                    break;
                }
            }
            if (hasFriendRequest) {
                HashMap<UUID, List<UUID>> map = this.friendsManager.getFriendsList();
                List<UUID> friendsList = map.get(player.getUniqueId());
                if (friendsList == null)
                    friendsList = new ArrayList<>();
                friendsList.add(target.getUniqueId());
                this.friendsManager.setFriendsList(map);
                player.sendMessage(ChatColor.LIGHT_PURPLE + "You are now friends with " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.LIGHT_PURPLE + "You are now friends with " + ChatColor.YELLOW + player.getName());
            } else {
                player.sendMessage(ChatColor.RED + "Looks like you don't have that person as a friend, try checking your spelling and try again.");
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            boolean hasFriend = false;
            List<UUID> f = (List<UUID>)this.friendsManager.getFriendsList().get(player);
            for (UUID friend : f) {
                if (Bukkit.getPlayer(friend).getName().equalsIgnoreCase(target.getName())) {
                    hasFriend = true;
                    break;
                }
            }
            if (hasFriend) {
                HashMap<UUID, List<UUID>> map = this.friendsManager.getFriendsList();
                List<UUID> friendsList = map.get(player.getUniqueId());
                if (friendsList == null)
                    friendsList = new ArrayList<>();
                friendsList.remove(target.getUniqueId());
                this.friendsManager.setFriendsList(map);
                player.sendMessage(ChatColor.LIGHT_PURPLE + "You are no longer friends with " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.LIGHT_PURPLE + "You are no longer friends with " + ChatColor.YELLOW + player.getName());
            } else {
                player.sendMessage(ChatColor.RED + "Looks like you don't have a friends request from that player, try checking your spelling and try again.");
            }
        } else {
            List<UUID> f = (List<UUID>)this.friendsManager.getFriendsList().get(player);
            for (UUID friend : f)
                player.sendMessage(Bukkit.getPlayer(friend).getName());
        }
        return true;
    }
}
