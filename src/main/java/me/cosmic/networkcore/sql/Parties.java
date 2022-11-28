package me.cosmic.networkcore.sql;

import me.cosmic.networkcore.NetworkCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.PrintStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Parties {

    private final NetworkCore networkCore;

    public Parties(NetworkCore nc) {
        networkCore = nc;
    }

    public void createParty(Player player, Player target) {
        if (isPlayerInParty(player)) {
            player.sendMessage(ChatColor.RED + "Party >> You are already in a party. Leave your current party to create a new one.");
            return;
        }
        try {
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("insert into parties(PartyLeader) values(?)");
            ps.setString(1, player.getUniqueId().toString());
            ps.executeUpdate();

            PreparedStatement ps1 = this.networkCore.mySQL.getConnection().prepareStatement("select * from parties where PartyLeader=?");
            ps1.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps1.executeQuery();
            int partyID = -1;
            if (rs.next()) {
                partyID = rs.getInt("PartyID");
            }

            PreparedStatement ps2 = this.networkCore.mySQL.getConnection().prepareStatement("update players set PartyID=? where UUID=?");
            ps2.setInt(1, partyID);
            ps2.setString(2, player.getUniqueId().toString());
            ps2.executeUpdate();

            PreparedStatement ps3 = this.networkCore.mySQL.getConnection().prepareStatement("select * from players where PartyID=?");
            ps3.setInt(1, partyID);
            ResultSet r3 = ps3.executeQuery();
            while (r3.next()) {
                try {
                    Player partyMember = Bukkit.getPlayer(UUID.fromString(r3.getString("UUID")));
                    partyMember.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.LIGHT_PURPLE + "was added to the party.");
                } catch (Exception ignored) {
                    // a player from the party isn't online, so it'll throw an error.
                    ignored.printStackTrace();
                }
            }
        } catch (SQLException e) {
            player.sendMessage(ChatColor.RED + "Something went wrong there. Please try again later or contact an administrator.");
        }
    }

    public boolean isPlayerInParty(Player player) {
        try {
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("select * from players where UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();

            return (rs.getInt("PartyID") != -1); //-1 meaning the player is not in a party

        } catch (SQLException e) {
            player.sendMessage(ChatColor.RED + "Something went wrong there. Please try again later or contact an administrator.");
        }
        return false;
    }

    public void removeAllPlayersFromParties() {
        try {
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("update players set PartyID=?");
            ps.setInt(1, -1);
            if (ps.execute()) {
                Bukkit.getLogger().log(Level.INFO, ChatColor.GREEN + "NetworkCore >> Successfully removed all players from party! This is an intended thing on every restart/crash, don't worry!");
            } else {
                Bukkit.getLogger().log(Level.SEVERE, ChatColor.RED + "<<| |CRITICAL ERROR| |>> Could not remove all players from their party. This could throw errors in the future.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UUID> getAllPlayersInParty(Player player) {
        try {
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("select * from players where UUID=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet rs = ps.executeQuery();
            int pid = rs.getInt("PartyID");

            PreparedStatement ps1 = this.networkCore.mySQL.getConnection().prepareStatement("select * from parties where PartyID=?");
            ps1.setInt(1, pid);
            ResultSet rs1 = ps1.executeQuery();
            List<UUID> uids = new ArrayList<>();
            while (rs1.next()) {
                uids.add(UUID.fromString(rs1.getString("UUID")));
            }
            return uids;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void disbandParty(Player player) {
        try {

            PreparedStatement partyStatement = this.networkCore.mySQL.getConnection().prepareStatement("select * from players where UUID=?");
            partyStatement.setString(1, player.getUniqueId().toString());
            ResultSet rs = partyStatement.executeQuery();
            int partyID = -1;

            if (rs.next()) {
                partyID = rs.getInt("");
            }

            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("update players set PartyID=? where PartyID=?");
            ps.setInt(1, -1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
