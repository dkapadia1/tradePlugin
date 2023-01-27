package tradeplugin.tradeplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.*;
import tradeplugin.tradeplugin.TradePlugin;

public class AddMoney implements CommandExecutor {
        public TradePlugin plugin;
        NamespacedKey money;

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            //Make sure you have args
            if(args.length < 2) {
                return false;
            }
            //TRY  to convert strings to int and player. Just sends error message if fails
           try {Player p = sender.getServer().getPlayerExact(args[0]);
               TradePlugin.setMoney(p, Integer.parseInt(args[1]));}
           catch (IllegalArgumentException iae){sender.sendMessage(ChatColor.RED + iae.toString()); return false; }
           return true;


        }
    }

