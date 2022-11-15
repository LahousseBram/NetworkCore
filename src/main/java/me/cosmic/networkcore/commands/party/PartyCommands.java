package me.cosmic.networkcore.commands.party;

import java.util.List;

import me.cosmic.networkcore.NetworkCore;
import me.cosmic.networkcore.managers.PartyManager;
import me.cosmic.networkcore.systems.Party;
import me.cosmic.networkcore.systems.PartyInvite;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PartyCommands implements CommandExecutor {
    private final NetworkCore core;

    public PartyCommands(NetworkCore core) {
        this.core = core;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final PartyManager pm = this.core.getPartyManager();
        List<Party> parties = pm.getParties();
        if (!(sender instanceof Player))
            return false;
        final Player player = (Player)sender;
        Player target = null;
        try {
            target = Bukkit.getPlayer(args[1]);
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "That player either doesn't exist or isn't online at the moment. Please try again and check your spelling.");
        }
        if (args[0].equalsIgnoreCase("invite")) {
            boolean exists = false;
            for (Party p : parties) {
                if (p.getLeader() == player.getUniqueId() || p.getMembers().contains(player.getUniqueId())) {
                    exists = true;
                    break;
                }
            }
            if (!exists && target != null) {
                target.sendMessage(ChatColor.LIGHT_PURPLE + "You have received a party invite from " + ChatColor.YELLOW + player.getName() + ChatColor.LIGHT_PURPLE + ", type " + ChatColor.GREEN + "/p accept " + ChatColor.LIGHT_PURPLE + "to accept the party invite.");
                final PartyInvite invite = pm.createInvite(player.getUniqueId(), target.getUniqueId());
                final Player finalTarget = target;
                (new BukkitRunnable() {
                    public void run() {
                        pm.removeInvite(invite);
                        try {
                            finalTarget.sendMessage(ChatColor.LIGHT_PURPLE + "The invite from " + ChatColor.YELLOW + player.getName() + ChatColor.LIGHT_PURPLE + " has expired.");
                        } catch (Exception exception) {}
                    }
                }).runTaskLater((Plugin)this.core, 1200L);
            } else {
                player.sendMessage(ChatColor.RED + "You're already in a party, please leave your current one and try again.");
            }
        }
        return false;
    }
}
