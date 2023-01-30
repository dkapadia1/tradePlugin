package tradeplugin.tradeplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tradeplugin.tradeplugin.GUI.SignGUI;
import tradeplugin.tradeplugin.TradePlugin;

public class SignTest implements CommandExecutor {

    public TradePlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        plugin.sign.open(sender.getServer().getPlayerExact(sender.getName()), new String[] { "test0", "test1", "test2", "test3" }, (player, lines) -> sender.sendMessage(lines[0]));
        return true;

    }
}
