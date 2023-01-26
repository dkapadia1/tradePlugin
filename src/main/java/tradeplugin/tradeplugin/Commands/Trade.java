package tradeplugin.tradeplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import tradeplugin.tradeplugin.GUI.TradeGUI;
import tradeplugin.tradeplugin.TradePlugin;

public class Trade implements CommandExecutor {

    public TradePlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0)
        {
            return false;
        }
       plugin.tGUI.openInventory(sender.getServer().getPlayerExact(sender.getName()), true);
        plugin.tGUI.openInventory(sender.getServer().getPlayer(args[0]), false);
        Bukkit.getLogger().info(sender.getServer().getPlayerExact(sender.getName()).getName());

        return true;
    }
}
