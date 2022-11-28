package me.cosmic.networkcore.commands.database;

import me.cosmic.networkcore.NetworkCore;
import me.cosmic.networkcore.sql.Players;
import me.cosmic.networkcore.sql.Row;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DatabaseCommands implements CommandExecutor {

    private NetworkCore core;

    public DatabaseCommands(NetworkCore core) {
        this.core = core;
    }

    public String filterQuery(String s) {
        String output = "";
        for (int i = 0; i < s.length(); i++) {
            if (s.substring(i,i+1).equalsIgnoreCase("_")) {
                output += " ";
            } else {
                output += s.substring(i, i+1);
            }
        }
        return output;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You don't have access to this command.");
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Please add a query to execute. /executequery [query] [params]");
        }

        String query = filterQuery(args[1]);

        try {
            PreparedStatement ps = this.core.mySQL.getConnection().prepareStatement(query);

            for (int i = 2; i < args.length; i++) {
                try {
                    ps.setString(i, args[i]);
                } catch (Exception e) {
                    return false;
                }
            }

            if (Integer.parseInt(args[0]) == 1) {
                ResultSet rs = ps.executeQuery();

                List<Row> table = new ArrayList<Row>();

                Row.formTable(rs, table);

                for (Row row : table)
                {
                    for (Map.Entry<Object, Class> col: row.row)
                    {
                        sender.sendMessage(" > " + ((col.getValue()).cast(col.getKey())));
                    }
                }
            } else {
                boolean rs = ps.execute();

                sender.sendMessage(rs + "");
            }

            return true;
        } catch (SQLException e) {
            return false;
        }

    }
}
