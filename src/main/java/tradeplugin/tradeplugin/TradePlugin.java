package tradeplugin.tradeplugin;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import tradeplugin.tradeplugin.Commands.Trade;
import tradeplugin.tradeplugin.GUI.TradeGUI;
import tradeplugin.tradeplugin.Handlers.PlayerJoinHandler;

public final class TradePlugin extends JavaPlugin {
    PlayerJoinHandler ph;
    public static NamespacedKey money;
    public TradeGUI tGUI;
    public Trade trade;
    public static TradePlugin plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        ph = new PlayerJoinHandler();
        tGUI = new TradeGUI();
        tGUI.plugin = this;
        trade = new Trade();
        trade.plugin =this;
        Bukkit.getPluginManager().registerEvents(ph,this);
        Bukkit.getPluginManager().registerEvents(tGUI,this);
        getCommand("trade").setExecutor(trade);
        money = new NamespacedKey(this, "money");




    }
    public static Integer getMoney(Player p)
    {
        return(p.getPersistentDataContainer().get(money, PersistentDataType.INTEGER));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
