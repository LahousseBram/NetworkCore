package me.cosmic.networkcore.commands.friends;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.cosmic.networkcore.NetworkCore;
import me.cosmic.networkcore.managers.FriendsManager;
import me.cosmic.networkcore.sql.Players;
import me.cosmic.networkcore.systems.FriendRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FriendsCommands implements CommandExecutor {

    //TODO
    // - /f command still gives internal error: ERROR: Caused by: java.lang.ArrayIndexOutOfBoundsException: 1
    //        at me.cosmic.networkcore.commands.friends.FriendsCommands.onCommand(FriendsCommands.java:36)
    // - I can still send f requests from player if target has already accepted. Friend request from target to player after target has accepted player request.
    // - I can send friend requests to myself
    // That should be mostly it... More testing required.

    private NetworkCore networkcore;

    private FriendsManager friendsManager;
    private Players pl;

    public FriendsCommands(NetworkCore core) {
        this.networkcore = core;
        this.friendsManager = this.networkcore.getFriendsManager();
        this.pl = new Players(this.networkcore);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target;
        if (!(sender instanceof Player))
            return false;
        Player player = (Player)sender;
        if (args.length >= 1) {
            HashMap<UUID, List<UUID>> friends = this.friendsManager.getFriendsList();
            if (args[0].equalsIgnoreCase("add")) {
                try {
                    target = Bukkit.getPlayer(args[1]);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "That player either doesn't exist or isn't online at the moment. Please try again and check your spelling.");
                    return true;
                }
                if (!pl.areTheyFriends(player, target) && !pl.existsFriendRequest(player, target)) {

                    pl.createFriendRequest(player, target);

                    target.sendMessage(ChatColor.LIGHT_PURPLE + "-----------------------------------------------------");
                    target.sendMessage(ChatColor.LIGHT_PURPLE + "");
                    target.sendMessage(ChatColor.WHITE + "You have received a friend request from " + ChatColor.YELLOW + player.getName());
                    target.sendMessage(ChatColor.WHITE + "Type " + ChatColor.YELLOW + "/f accept " + player.getName() + ChatColor.WHITE + " to accept this request and become friends.");
                    target.sendMessage(ChatColor.LIGHT_PURPLE + "");
                    target.sendMessage(ChatColor.LIGHT_PURPLE + "-----------------------------------------------------");
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "Friend request sent to " + ChatColor.YELLOW + target.getName());

                } else if (pl.existsFriendRequest(player, target)) {
                    player.sendMessage(ChatColor.RED + "You've already sent a friend request to that player.");
                    return true;
                } else {
                    player.sendMessage(ChatColor.RED + "That player is already on your friends list.");
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("accept")) {
                try {
                    target = Bukkit.getPlayer(args[1]);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "That player either doesn't exist or isn't online at the moment. Please try again and check your spelling.");
                    return true;
                }

                if (target.getUniqueId() == player.getUniqueId()) return true;
                if (pl.existsFriendRequest(player, target)) {

                    pl.createFriends(player, target);

                    player.sendMessage(ChatColor.LIGHT_PURPLE + "You are now friends with " + ChatColor.YELLOW + target.getName());
                    target.sendMessage(ChatColor.LIGHT_PURPLE + "You are now friends with " + ChatColor.YELLOW + player.getName());

                    pl.removeFriendRequest(player, target);
                } else {
                    player.sendMessage(ChatColor.RED + "Looks like you don't a friend request from that player, try checking your spelling and try again.");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("remove")) {
                try {
                    target = Bukkit.getPlayer(args[1]);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.RED + "That player either doesn't exist or isn't online at the moment. Please try again and check your spelling.");
                    return true;
                }
                if (this.pl.areTheyFriends(player, target)) {
                    this.pl.deleteFriends(player, target);
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "You are no longer friends with " + ChatColor.YELLOW + target.getName());
                    target.sendMessage(ChatColor.LIGHT_PURPLE + "You are no longer friends with " + ChatColor.YELLOW + player.getName());
                } else {
                    player.sendMessage(ChatColor.RED + "Looks like you don't have a friends request from that player, try checking your spelling and try again.");
                }
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.LIGHT_PURPLE + "FRIENDS >> Help Menu");
                player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------");
                player.sendMessage(ChatColor.YELLOW + "/f add [player]" + ChatColor.WHITE + ": Send a friend request to a player");
                player.sendMessage(ChatColor.YELLOW + "/f remove [player]" + ChatColor.WHITE + ": Remove a player from your friends list");
                player.sendMessage(ChatColor.YELLOW + "/f accept [player]" + ChatColor.WHITE + ": Accept a friend request from a player. Note: requires friend request from that player");
                player.sendMessage(ChatColor.YELLOW + "/f " + ChatColor.WHITE + ": Show your friend list");
                player.sendMessage(ChatColor.YELLOW + "/f help" + ChatColor.WHITE + ": Display this help menu");
                player.sendMessage(ChatColor.DARK_GRAY + "-----------------------------------");

                return true;
            } else if (args[0].equalsIgnoreCase("list")) {
                List<String> f = this.pl.getFriends(player);
                if (f.size() < 1) {
                    player.sendMessage(ChatColor.RED + "FRIENDS >> You don't have any friends yet.");
                } else {
                    player.sendMessage("");
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "----------------------------------------------");
                    for (String friend : f)
                        player.sendMessage(ChatColor.YELLOW + friend);
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "----------------------------------------------");
                    player.sendMessage("");
                }
            }

        } else {
            List<String> f = this.pl.getFriends(player);
            if (f.size() < 1) {
                player.sendMessage(ChatColor.RED + "FRIENDS >> You don't have any friends yet.");
            } else {
                player.sendMessage("");
                player.sendMessage(ChatColor.LIGHT_PURPLE + "----------------------------------------------");
                player.sendMessage(ChatColor.LIGHT_PURPLE + "FRIENDS >> Your friends list:");
                player.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------");
                for (String friend : f)
                    player.sendMessage(ChatColor.YELLOW + friend);
                player.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------");
                player.sendMessage("");
            }
            return true;
        }
        return true;
    }
}