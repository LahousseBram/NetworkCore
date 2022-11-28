package me.cosmic.networkcore.sql;

import me.cosmic.networkcore.NetworkCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Players {

    private NetworkCore networkCore;

    public Players(NetworkCore nc) {
        networkCore = nc;
    }

    public void createPlayer(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("insert into players(UUID) values(?)");
            ps.setString(1, uuid.toString());
            int resultSet = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createFriends(Player player, Player secondPlayer) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("insert into friends(Player, FriendsWith) values(?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, secondPlayer.getUniqueId().toString());
            int resultSet = ps.executeUpdate();
            PreparedStatement ps1 = this.networkCore.mySQL.getConnection().prepareStatement("insert into friends(Player, FriendsWith) values(?, ?)");
            ps1.setString(1, secondPlayer.getUniqueId().toString());
            ps1.setString(2, uuid.toString());
            int resultSet1 = ps1.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createFriendRequest(Player player, Player target) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("insert into friendrequests(Origin, Target) values(?, ?)");
            ps.setString(1, uuid.toString());
            ps.setString(2, target.getUniqueId().toString());
            int resultSet = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeFriendRequest(Player player, Player target) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("delete from friendrequests where Origin=? and Target=?");
            ps.setString(1, uuid.toString());
            ps.setString(2, target.getUniqueId().toString());
            int resultSet = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsFriendRequest(Player player, Player target) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("select * from friendrequests where Origin=? and Target=?");
            ps.setString(1, uuid.toString());
            ps.setString(2, target.getUniqueId().toString());
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean exists(Player player) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("select * from players where UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void getAllPlayers() {
        try {
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("select UUID from players");
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                Bukkit.getLogger().info(UUID.fromString(resultSet.getString("UUID")).toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean areTheyFriends(Player player, Player secondPlayer) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("select * from friends where Player=? and FriendsWith=? or Player=? and FriendsWith=?");
            ps.setString(1, uuid.toString());
            ps.setString(2, secondPlayer.getUniqueId().toString());
            ps.setString(3, secondPlayer.getUniqueId().toString());
            ps.setString(4, uuid.toString());
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    public List<String> getFriends(Player player) {
        List<String> names = new ArrayList<>();
        try {
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("select FriendsWith from friends where Player=?");
            ps.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                names.add(Bukkit.getOfflinePlayer(UUID.fromString(resultSet.getString("FriendsWith"))).getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public void deleteFriends(Player player, Player target) {
        try {
            UUID uuid = player.getUniqueId();
            PreparedStatement ps = this.networkCore.mySQL.getConnection().prepareStatement("delete from friends where Player=? and FriendsWith=?");
            ps.setString(1, uuid.toString());
            ps.setString(2, target.getUniqueId().toString());
            int resultSet = ps.executeUpdate();
            PreparedStatement ps2 = this.networkCore.mySQL.getConnection().prepareStatement("delete from friends where Player=? and FriendsWith=?");
            ps2.setString(1, target.getUniqueId().toString());
            ps2.setString(2, uuid.toString());
            int resultSet2 = ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
