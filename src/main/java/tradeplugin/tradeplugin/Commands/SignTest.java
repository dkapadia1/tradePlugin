package tradeplugin.tradeplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tradeplugin.tradeplugin.GUI.SignGUI;
import tradeplugin.tradeplugin.TradePlugin;
import com.comphenix.protocol.*;
import com.comphenix.protocol.reflect.*;

import java.util.Arrays;

public class SignTest implements CommandExecutor {

    public TradePlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Bukkit.getLogger().info("we start");
       try {plugin.sign.open(sender.getServer().getPlayerExact(args[0]), new String[]{"test0", "test1", "test2", "test3"}, new SignGUI.SignGUIListener() {
            @Override
            public void onSignDone(Player player, String[] lines) {
                // do something with the input
                sender.sendMessage(lines[0] + "nooooo");
            }

        });}
       catch(FieldAccessException e){
           Bukkit.getLogger().info(Arrays.toString(e.getStackTrace()));
       }
        return true;
    }
}
