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
            if(args.length < 2) {
                return false;
            }
            if (TradePlugin.money == null) {
                sender.sendMessage(":wahhhhh");
            }
           Player p = sender.getServer().getPlayerExact(args[0]);
            PersistentDataContainer d = p.getPersistentDataContainer();
            d.set(TradePlugin.money, PersistentDataType.INTEGER, (TradePlugin.getMoney(p) + Integer.parseInt(args[1])));
          //getPersistentDataContainer().set(money, PersistentDataType.INTEGER, ))*/
            sender.sendMessage(String.valueOf((int)d.get(TradePlugin.money, PersistentDataType.INTEGER)));
            TradePlugin.updateMoney(p);
            return true;
        }
    }

