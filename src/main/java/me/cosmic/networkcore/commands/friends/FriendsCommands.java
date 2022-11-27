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
                /*
                if (target.getUniqueId() == player.getUniqueId()) return true;
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
                    if (request.getTarget() == target.getUniqueId() && request .getOrigin() == player.getUniqueId()) {
                        hasAlreadySentRequest = true;
                        break;
                    }
                }
                */
                if (!pl.areTheyFriends(player, target) && !pl.existsFriendRequest(player, target)) {
                    /*
                    FriendRequest request = new FriendRequest(target.getUniqueId(), player.getUniqueId());
                    List<FriendRequest> f = this.friendsManager.getFriendRequests();
                    f.add(request);
                    this.friendsManager.setFriendRequests(f);
                    return true;
                     */

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
                /*boolean hasFriendRequest = false;
                List<FriendRequest> f = this.friendsManager.getFriendRequests();
                for (FriendRequest request : f) {
                    if (request.getName().equalsIgnoreCase(target.getName())) {
                        hasFriendRequest = true;
                        f.remove(request);
                        break;
                    }
                }
                 */
                if (pl.existsFriendRequest(player, target)) {
                    /*
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
                     */

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

                if (target.getUniqueId() == player.getUniqueId()) return true;
                boolean hasFriend = false;
                List<UUID> f = this.friendsManager.getFriendsList().get(player.getUniqueId());
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
                /*
                List<UUID> f = this.friendsManager.getFriendsList().get(player.getUniqueId());
                 */
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
            /*
            List<UUID> f = this.friendsManager.getFriendsList().get(player.getUniqueId());
             */
            List<String> f = this.pl.getFriends(player);
            if (f.size() < 1) {
                player.sendMessage(ChatColor.RED + "FRIENDS >> You don't have any friends yet.");
            } else {
                player.sendMessage("");
<<<<<<< HEAD
                player.sendMessage(ChatColor.LIGHT_PURPLE + "----------------------------------------------");
                for (String friend : f)
                    player.sendMessage(ChatColor.YELLOW + friend);
                player.sendMessage(ChatColor.LIGHT_PURPLE + "----------------------------------------------");
=======
                player.sendMessage(ChatColor.LIGHT_PURPLE + "FRIENDS >> Your friends list:");
                player.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------");
                for (UUID friend : f)
                    player.sendMessage(ChatColor.YELLOW + Bukkit.getPlayer(friend).getName());
                player.sendMessage(ChatColor.DARK_GRAY + "----------------------------------------------");
>>>>>>> f22a0588ab59f4c1a6ef9bef9368dfce517ec035
                player.sendMessage("");
            }
            return true;
        }
        return true;
    }
}
