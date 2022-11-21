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
            boolean hasAlreadySentRequest = false;
            //check if they're already friends
            if (friends.get(player.getUniqueId()).size() >= 1) {
                for (UUID f : friends.get(player.getUniqueId())) {
                    if (f == target.getUniqueId()) {
                        isFriends = true;
                        break;
                    }
                }
            }
            List<FriendRequest> fr = this.friendsManager.getFriendRequests();
            for (FriendRequest request : fr) {
                if (request.getName().equalsIgnoreCase(target.getName())) {
                    hasAlreadySentRequest = true;
                    break;
                }
            }
            if (!isFriends && !hasAlreadySentRequest) {
                FriendRequest request = new FriendRequest(target.getUniqueId(), player.getUniqueId());
                List<FriendRequest> f = this.friendsManager.getFriendRequests();
                f.add(request);
                this.friendsManager.setFriendRequests(f);
                return true;
            } else if (hasAlreadySentRequest) {
                player.sendMessage(ChatColor.RED + "You've already sent a friend request to that player.");
                return true;
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
                //add the target as a friend of the player
                List<UUID> playerFriendsList = map.get(player.getUniqueId());
                if (playerFriendsList == null)
                    playerFriendsList = new ArrayList<>();
                playerFriendsList.add(target.getUniqueId());
                map.put(player.getUniqueId(), playerFriendsList);
                //add the player as a friend of the target
                List<UUID> targetFriendsList = map.get(target.getUniqueId());
                if (targetFriendsList == null)
                    targetFriendsList = new ArrayList<>();
                targetFriendsList.add(player.getUniqueId());
                map.put(target.getUniqueId(), targetFriendsList);
                this.friendsManager.setFriendsList(map);

                player.sendMessage(ChatColor.LIGHT_PURPLE + "You are now friends with " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.LIGHT_PURPLE + "You are now friends with " + ChatColor.YELLOW + player.getName());
            } else {
                player.sendMessage(ChatColor.RED + "Looks like you don't a friend request from that player, try checking your spelling and try again.");
            }
            return true;
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
                //removing the target from the friends list
                List<UUID> friendsList = map.get(player.getUniqueId());
                if (friendsList == null)
                    friendsList = new ArrayList<>();
                friendsList.remove(target.getUniqueId());
                map.put(player.getUniqueId(), friendsList);
                //removing the player from the targets list
                List<UUID> targetFriendsList = map.get(target.getUniqueId());
                if (targetFriendsList == null)
                    targetFriendsList = new ArrayList<>();
                targetFriendsList.remove(player.getUniqueId());
                map.put(target.getUniqueId(), targetFriendsList);
                this.friendsManager.setFriendsList(map);
                player.sendMessage(ChatColor.LIGHT_PURPLE + "You are no longer friends with " + ChatColor.YELLOW + target.getName());
                target.sendMessage(ChatColor.LIGHT_PURPLE + "You are no longer friends with " + ChatColor.YELLOW + player.getName());
            } else {
                player.sendMessage(ChatColor.RED + "Looks like you don't have a friends request from that player, try checking your spelling and try again.");
            }
            return true;
        } else {
            List<UUID> f = this.friendsManager.getFriendsList().get(player);
            if (f.size() < 1) {
                player.sendMessage(ChatColor.RED + "FRIENDS >> You don't have any friends yet.");
            } else {
                player.sendMessage("");
                player.sendMessage(ChatColor.LIGHT_PURPLE + "----------------------------------------------");
                for (UUID friend : f)
                    player.sendMessage(ChatColor.YELLOW + Bukkit.getPlayer(friend).getName());
                player.sendMessage(ChatColor.LIGHT_PURPLE + "----------------------------------------------");
                player.sendMessage("");
            }
            return true;
        }
    }
}
